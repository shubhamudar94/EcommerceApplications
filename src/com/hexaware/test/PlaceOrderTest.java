package com.hexaware.test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.hexaware.dao.EcomDAO;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrderTest {

	@Test
	public void testPlaceOrder() {
		Customer customer = new Customer(1, "John", "john@example.com", "password");
		Product product1 = new Product(1349, "Test Product 1", 10, "Test Description 1", 100);
		Product product2 = new Product(7249, "Test Product 2", 20, "Test Description 2", 150);
		int quantity1 = 2;
		int quantity2 = 1;

		EcomDAO.addToCart(customer, product1, quantity1);
		EcomDAO.addToCart(customer, product2, quantity2);

		Map<Product, Integer> cartContent = new HashMap<>();
		cartContent.put(product1, quantity1);
		cartContent.put(product2, quantity2);

		boolean success = EcomDAO.placeOrder(customer, cartContent, "Shipping Address");

		assertTrue("Order should be placed successfully", success);
	}
}
