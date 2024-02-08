package com.hexaware.dao;

import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;
import com.hexaware.util.DBConnection;
import com.hexaware.dao.OrderProcessorRepositoryImpl;
import com.hexaware.myexceptions.CustomerNotFoundException;
import com.hexaware.myexceptions.ProductNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EcomDAO {
	private static Connection connection = DBConnection.getConnection();

	public static boolean registerCustomer(Customer customer) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "INSERT INTO customers (Customer_id, Name, Email, Password) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customer.getCustomerId());
			statement.setString(2, customer.getCustomerName());
			statement.setString(3, customer.getCustomerEmail());
			statement.setString(4, customer.getPassword());
			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createProduct(Product product) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "INSERT INTO products (Product_id, Name, Price, Description, StockQuantity) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, product.getProductId());
			statement.setString(2, product.getProductName());
			statement.setDouble(3, product.getPrice());
			statement.setString(4, product.getDescription());
			statement.setInt(5, product.getStockQuantity());
			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteProduct(int productId) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "DELETE FROM products WHERE Product_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, productId);
			int rowsDeleted = statement.executeUpdate();
			return rowsDeleted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addToCart(Customer customer, Product product, int quantity) {
		try (Connection connection = DBConnection.getConnection()) {
			if (!customerExists(customer.getCustomerId())) {
				throw new CustomerNotFoundException("Customer with ID " + customer.getCustomerId() + " not found");
			}
			if (!productExists(product.getProductId())) {
				throw new ProductNotFoundException("Product with ID " + product.getProductId() + " not found");
			}
			String query = "INSERT INTO cart (Customer_id, Product_id, Quantity) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customer.getCustomerId());
			statement.setInt(2, product.getProductId());
			statement.setInt(3, quantity);
			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean customerExists(int customerId) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "SELECT COUNT(*) FROM customers WHERE Customer_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customerId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean productExists(int productId) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "SELECT COUNT(*) FROM products WHERE Product_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, productId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean isProductExists(Connection connection, int productId) throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM products WHERE Product_id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, productId);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			int count = resultSet.getInt("count");
			return count > 0;
		}
		return false;
	}

	public static List<Product> viewCart(Scanner scanner) {
		List<Product> cartProducts = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			System.out.print("Enter customer ID: ");
			int customerId = scanner.nextInt();

			String query = "SELECT p.Product_id, p.Name, p.Price, p.Description, p.StockQuantity " + "FROM cart c "
					+ "INNER JOIN products p ON c.Product_id = p.Product_id " + "WHERE c.Customer_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customerId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Product product = new Product();
				product.setProductId(resultSet.getInt("Product_id"));
				product.setProductName(resultSet.getString("Name"));
				product.setPrice(resultSet.getInt("Price"));
				product.setDescription(resultSet.getString("Description"));
				product.setStockQuantity(resultSet.getInt("StockQuantity"));
				cartProducts.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartProducts;
	}

	public static boolean placeOrder(Customer customer, Map<Product, Integer> cartContent, String shippingAddress) {
		try (Connection connection = DBConnection.getConnection()) {
			connection.setAutoCommit(false);
			String orderQuery = "INSERT INTO orders (Customer_id, Order_date, Total_price, Shipping_address) VALUES (?, CURRENT_DATE(), ?, ?)";
			PreparedStatement orderStatement = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
			orderStatement.setInt(1, customer.getCustomerId());

			int totalPrice = 0;
			for (Map.Entry<Product, Integer> entry : cartContent.entrySet()) {
				Product product = entry.getKey();
				int quantity = entry.getValue();
				double price = getProductPrice(product.getProductId());
				totalPrice += price * quantity;
			}
			orderStatement.setDouble(2, totalPrice);
			orderStatement.setString(3, shippingAddress);

			int rowsInserted = orderStatement.executeUpdate();
			if (rowsInserted > 0) {
				ResultSet generatedKeys = orderStatement.getGeneratedKeys();
				if (generatedKeys.next()) {
					int orderId = generatedKeys.getInt(1);
					String orderItemQuery = "INSERT INTO order_items (Order_id, Product_id, Quantity) VALUES (?, ?, ?)";
					PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery);
					for (Map.Entry<Product, Integer> entry : cartContent.entrySet()) {
						Product product = entry.getKey();
						int quantity = entry.getValue();
						orderItemStatement.setInt(1, orderId);
						orderItemStatement.setInt(2, product.getProductId());
						orderItemStatement.setInt(3, quantity);
						orderItemStatement.addBatch();
					}
					orderItemStatement.executeBatch();
					connection.commit();
					return true;
				}
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static double getProductPrice(int productId) throws SQLException {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "SELECT Price FROM products WHERE Product_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, productId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getDouble("Price");
			}
		}
		throw new SQLException("Product with ID " + productId + " not found.");
	}

	public static List<Map<Product, Integer>> viewCustomerOrder(int customerId) {
		List<Map<Product, Integer>> customerOrders = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String query = "SELECT oi.Product_id, p.Name, oi.Quantity, p.Price FROM order_items oi "
					+ "INNER JOIN products p ON oi.Product_id = p.Product_id "
					+ "INNER JOIN orders o ON oi.Order_id = o.Order_id " + "WHERE o.Customer_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, customerId);
			ResultSet resultSet = statement.executeQuery();
			Map<Product, Integer> orderMap = new HashMap<>();
			while (resultSet.next()) {
				int productId = resultSet.getInt("Product_id");
				String productName = resultSet.getString("Name");
				int quantity = resultSet.getInt("Quantity");
				int price = resultSet.getInt("Price");
				Product product = new Product(productId, productName, price);
				orderMap.put(product, quantity);
			}
			customerOrders.add(orderMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerOrders;
	}
}
