package com.hexaware.entity;

public class Customer {
	private int customerId;
	private String customerName;
	private String customerEmail;
	private String password;

	public Customer() {
	}

	public Customer(int customerId) {
		this.customerId = customerId;
	}

	public Customer(int customerId, String customerName, String customerEmail, String password) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.password = password;

	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer{" + "customerId=" + customerId + ", customerName='" + customerName + '\'' + ", customerEmail='"
				+ customerEmail + '\'' + '}';
	}

}
