// 7
package com.hexaware.dao;

import java.util.*;
import java.util.Map;
import java.util.Set;

import com.hexaware.entity.Customer;
import com.hexaware.entity.OrderProcessorRepository;
import com.hexaware.entity.Product;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

	private List<Product> products = new ArrayList<>();
	private List<Customer> customers = new ArrayList<>();
	private Map<Customer, Map<Product, Integer>> carts = new HashMap<>();
	private List<Map<Product, Integer>> orders = new ArrayList<>();

	@Override
	public boolean createProduct(Product product) {
		return products.add(product);
	}

	@Override
	public boolean createCustomer(Customer customer) {
		return customers.add(customer);
	}

	@Override
	public boolean deleteProduct(int productId) {
		Iterator<Product> iterator = products.iterator();
		while (iterator.hasNext()) {
			Product product = iterator.next();
			if (product.getProductId() == productId) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		Iterator<Customer> customerIterator = customers.iterator();
		while (customerIterator.hasNext()) {
			Customer customer = customerIterator.next();
			if (customer.getCustomerId() == customerId) {
				customerIterator.remove();
				break;
			}
		}

		Iterator<Map.Entry<Customer, Map<Product, Integer>>> cartIterator = carts.entrySet().iterator();
		while (cartIterator.hasNext()) {
			Map.Entry<Customer, Map<Product, Integer>> entry = cartIterator.next();
			if (entry.getKey().getCustomerId() == customerId) {
				cartIterator.remove();
				break;
			}
		}

		Iterator<Map<Product, Integer>> orderIterator = orders.iterator();
		while (orderIterator.hasNext()) {
			Map<Product, Integer> order = orderIterator.next();
			for (Iterator<Map.Entry<Product, Integer>> productMapIterator = order.entrySet()
					.iterator(); productMapIterator.hasNext();) {
				Map.Entry<Product, Integer> productMapEntry = productMapIterator.next();
			}
			if (order.isEmpty()) {
				orderIterator.remove();
			}
		}

		return true;
	}

	@Override
	public boolean addToCart(Customer customer, Product product, int quantity) {
		Map<Product, Integer> cart = carts.get(customer);
		if (cart == null) {
			cart = new HashMap<>();
			carts.put(customer, cart);
		}
		Integer currentQuantity = cart.get(product);
		if (currentQuantity == null) {
			currentQuantity = 0;
		}
		cart.put(product, currentQuantity + quantity);

		System.out.println("Cart Contents: " + carts);

		return true;
	}

	@Override
	public boolean removeFromCart(Customer customer, Product product) {
		Map<Product, Integer> cart = carts.get(customer);
		if (cart != null) {
			cart.remove(product);
			return true;
		}
		return false;
	}

	@Override
	public List<Product> getAllFromCart(Customer customer) {
		Map<Product, Integer> cart = carts.get(customer);
		if (cart != null) {
			return new ArrayList<>(cart.keySet());
		}
		return Collections.emptyList();
	}

	@Override
	public boolean placeOrder(Customer customer, List<Map<Product, Integer>> cart, String shippingAddress) {
		Map<Product, Integer> mergedCart = new HashMap<>();
		for (Map<Product, Integer> productMap : cart) {
			mergedCart.merge(productMap.keySet().iterator().next(), productMap.values().iterator().next(),
					Integer::sum);
		}
		orders.add(mergedCart);
		carts.remove(customer);
		return true;
	}

	@Override
	public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
		List<Map<Product, Integer>> customerOrders = new ArrayList<>();
		for (Map<Product, Integer> order : orders) {
		}
		return customerOrders;
	}
}