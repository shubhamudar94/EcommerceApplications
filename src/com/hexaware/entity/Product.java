package com.hexaware.entity;

public class Product {
    private int productId;
    private String productName;
    private int price;
    private String description;
    private int stockQuantity;
    
    public Product() {
    }
    
    public Product(int productId) {
        this.productId = productId;
    }
    
    public Product(int productId, String productName, int price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
    
    public Product(int productId, String productName, int price, String description, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;

    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + 
                ", price=" + price +
                ", description=" + description +
                ", stockQuantity=" + stockQuantity + '\'' +
                '}';
    }

}


