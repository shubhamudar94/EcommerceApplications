package com.hexaware.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.hexaware.dao.EcomDAO;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;
import com.hexaware.myexceptions.CustomerNotFoundException;
import com.hexaware.myexceptions.ProductNotFoundException;

public class AddToCartExceptionTest {

	@Test(expected = CustomerNotFoundException.class)
	public void testAddToCartCustomerNotFound() {
		Customer customer = new Customer(9999, "Non-existing Customer", "nonexisting@example.com", "password");
		Product product = new Product(123, "Test Product", 10, "Test Description", 100);
		int quantity = 2;

		EcomDAO.addToCart(customer, product, quantity);
	}

	@Test(expected = ProductNotFoundException.class)
	public void testAddToCartProductNotFound() {
		Customer customer = new Customer(1, "John", "john@example.com", "password");
		Product product = new Product(9999, "Non-existing Product", 10, "Test Description", 100);
		int quantity = 2;

		EcomDAO.addToCart(customer, product, quantity);
	}

	@Test
	public void testAddToCartExistingCustomerAndProduct() {
		Customer customer = new Customer(90, "Shubham", "shubh@gmail.com", "jfvhnskjvnfj");
		Product product = new Product(1001, "Test Product", 10, "This is a test product", 100);
		int quantity = 2;

		boolean success = EcomDAO.addToCart(customer, product, quantity);

		assertTrue("Product should be added to cart successfully", success);
	}
}
