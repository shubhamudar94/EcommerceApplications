package com.hexaware.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.hexaware.dao.EcomDAO;
import com.hexaware.entity.Product;

public class ProductCreationTest {

	@Test
	public void testProductCreation() {
		int productId = 1003;
		String productName = "Test Product";
		int price = 9999;
		String description = "This is a test product";
		int stockQuantity = 10;

		Product product = new Product(productId, productName, price, description, stockQuantity);

		boolean success = EcomDAO.createProduct(product);

		assertTrue("Product creation test passed", success);
	}
}
