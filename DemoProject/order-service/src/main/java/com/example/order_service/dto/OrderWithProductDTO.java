package com.example.order_service.dto;

import com.example.order_service.entity.Order;

public class OrderWithProductDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private String status;
    private ProductDTO product;

    public OrderWithProductDTO(Order order, ProductDTO product) {
        this.id = order.getId();
        this.productId = order.getProductId();
        this.quantity = order.getQuantity();
        this.status = order.getStatus();
        this.product = product;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}