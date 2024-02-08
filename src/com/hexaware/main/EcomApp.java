package com.hexaware.main;

import com.hexaware.dao.EcomDAO;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class EcomApp {
	/**
     * Main method to run the E-commerce application.
     * @param args Command-line arguments.
     * Shubham Udar's Project
     */

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			displayMenu();

			int choice;
			try {
				choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				scanner.nextLine();
				continue;
			}

			switch (choice) {
			case 1:
				registerCustomer(scanner);
				break;
			case 2:
				createProduct(scanner);
				break;
			case 3:
				deleteProduct(scanner);
				break;
			case 4:
				addToCart(scanner);
				break;
			case 5:
				viewCart(scanner);
				break;
			case 6:
				placeOrder(scanner);
				break;
			case 7:
				viewCustomerOrder(scanner);
				break;

			case 0:
				System.out.println("Exiting the application. Goodbye!");
				scanner.close();
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice. Please enter a valid option.");
			}
		}
	}

	private static void displayMenu() {
		System.out.println("\nE-Commerce Application Menu:");
		System.out.println("1. Register Customer");
		System.out.println("2. Create Product");
		System.out.println("3. Delete Product");
		System.out.println("4. Add to Cart");
		System.out.println("5. View Cart");
		System.out.println("6. Place Order");
		System.out.println("7. View Customer Order");
		System.out.println("0. Exit");
		System.out.print("Enter your choice: ");
	}

	private static void registerCustomer(Scanner scanner) {
		System.out.print("Enter customer Id: ");
		int customerId = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Enter customer name: ");
		String customerName = scanner.nextLine();

		System.out.print("Enter customer email: ");
		String customerEmail = scanner.nextLine();

		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		Customer customer = new Customer(customerId, customerName, customerEmail, password);
		boolean success = EcomDAO.registerCustomer(customer);

		if (success) {
			System.out.println("Customer registered successfully!");
		} else {
			System.out.println("Failed to register customer. Please try again.");
		}
	}

	private static void createProduct(Scanner scanner) {
		System.out.print("Enter product name: ");
		String productName = scanner.next();

		System.out.print("Enter product ID: ");
		int productId = scanner.nextInt();

		System.out.print("Enter price: ");
		int price = scanner.nextInt();

		System.out.print("Enter product description: ");
		String description = scanner.next();

		System.out.print("Enter stockQuantity: ");
		int stockQuantity = scanner.nextInt();

		Product product = new Product(productId, productName, price, description, stockQuantity);
		boolean success = EcomDAO.createProduct(product);

		if (success) {
			System.out.println("Product created successfully!");
		} else {
			System.out.println("Failed to create product. Please try again.");
		}
	}

	private static void deleteProduct(Scanner scanner) {
		System.out.print("Enter product ID to delete: ");
		int productId = scanner.nextInt();

		boolean success = EcomDAO.deleteProduct(productId);

		if (success) {
			System.out.println("Product deleted successfully!");
		} else {
			System.out.println("Failed to delete product. Please try again.");
		}
	}

	private static void addToCart(Scanner scanner) {
		System.out.print("Enter customer ID: ");
		int customerId = scanner.nextInt();

		System.out.print("Enter product ID to add to cart: ");
		int productId = scanner.nextInt();

		System.out.print("Enter quantity: ");
		int quantity = scanner.nextInt();

		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		Product product = new Product();
		product.setProductId(productId);

		boolean success = EcomDAO.addToCart(customer, product, quantity);

		if (success) {
			System.out.println("Product added to cart successfully!");
		} else {
			System.out.println("Failed to add product to cart. Please try again.");
		}
	}

	private static void viewCart(Scanner scanner) {
		List<Product> cartProducts = EcomDAO.viewCart(scanner);

		if (cartProducts.isEmpty()) {
			System.out.println("Cart is empty.");
		} else {
			System.out.println("Products in the cart:");
			for (Product product : cartProducts) {
				System.out.println(product);
			}
		}
	}

	private static void placeOrder(Scanner scanner) {
		System.out.print("Enter customer ID: ");
		int customerId = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		System.out.print("Enter shipping address: ");
		String shippingAddress = scanner.nextLine();

		Map<Product, Integer> cartContent = new HashMap<>();

		System.out.println("Enter product IDs and quantities (0 to finish):");
		int productId;
		do {
			System.out.print("Enter product ID: ");
			productId = scanner.nextInt();
			if (productId != 0) {
				System.out.print("Enter quantity: ");
				int quantity = scanner.nextInt();
				cartContent.put(new Product(productId), quantity);
			}
		} while (productId != 0);

		if (EcomDAO.placeOrder(new Customer(customerId), cartContent, shippingAddress)) {
			System.out.println("Order placed successfully!");
		} else {
			System.out.println("Failed to place order. Please try again.");
		}
	}

	private static void viewCustomerOrder(Scanner scanner) {
		System.out.print("Enter customer ID: ");
		int customerId = scanner.nextInt();

		List<Map<Product, Integer>> customerOrders = EcomDAO.viewCustomerOrder(customerId);

		if (customerOrders.isEmpty()) {
			System.out.println("No orders found for the customer.");
		} else {
			System.out.println("Customer Orders:");
			for (Map<Product, Integer> order : customerOrders) {
				for (Map.Entry<Product, Integer> entry : order.entrySet()) {
					Product product = entry.getKey();
					int quantity = entry.getValue();
					System.out.println("Product ID: " + product.getProductId() + ", Product Name: "
							+ product.getProductName() + ", Quantity: " + quantity + ", Price: " + product.getPrice()
							+ ", Total Price: " + (quantity * product.getPrice()));
				}
			}
		}
	}
}
