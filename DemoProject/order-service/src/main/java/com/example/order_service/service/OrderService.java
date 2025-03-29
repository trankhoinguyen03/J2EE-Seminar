package com.example.order_service.service;

import com.example.order_service.dto.OrderWithProductDTO;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(WebClient.Builder webClientBuilder, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "fallback")
    public Mono<Order> createOrder(Long productId, int quantity) {
        Order order = new Order(productId, quantity);
        order.setStatus("SUCCESS"); // Thêm status SUCCESS khi tạo đơn hàng thành công
        return Mono.just(orderRepository.save(order));
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "getAllOrdersFallback")
    public Mono<List<OrderWithProductDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .map(products -> {
                    Map<Long, ProductDTO> productMap = products.stream()
                            .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));
                    return orders.stream()
                            .map(order -> {
                                ProductDTO product = productMap.get(order.getProductId());
                                return new OrderWithProductDTO(order, product);
                            })
                            .collect(Collectors.toList());
                });
    }

    public Mono<Order> fallback(Long productId, int quantity, Throwable t) {
        Order order = new Order(productId, quantity);
        order.setStatus("FAILED");
        return Mono.just(orderRepository.save(order));
    }

    public Mono<List<OrderWithProductDTO>> getAllOrdersFallback(Throwable t) {
        List<Order> orders = orderRepository.findAll();
        List<OrderWithProductDTO> result = orders.stream()
                .map(order -> new OrderWithProductDTO(order, null))
                .collect(Collectors.toList());
        return Mono.just(result);
    }
}