package com.example.order_service.controller;

import com.example.order_service.dto.OrderWithProductDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Mono<Order> createOrder(@RequestParam Long productId, @RequestParam int quantity) {
        return orderService.createOrder(productId, quantity);
    }

    @GetMapping
    public Mono<List<OrderWithProductDTO>> getAllOrders() {
        return orderService.getAllOrders();
    }
}