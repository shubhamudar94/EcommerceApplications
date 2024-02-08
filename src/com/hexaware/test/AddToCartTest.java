package com.hexaware.test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.hexaware.dao.EcomDAO;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Product;

public class AddToCartTest {

    @Test
    public void testAddToCart() {
        Customer customer = new Customer(1, "Shubham", "shubha@gmail.com", "jfvhnskjvnfj");
        Product product = new Product(3764, "Test Product", 10, "Test Description", 100);
        int quantity = 2;

        boolean success = EcomDAO.addToCart(customer, product, quantity);

        assertTrue("Product should be added to cart successfully", success);
    }
}
