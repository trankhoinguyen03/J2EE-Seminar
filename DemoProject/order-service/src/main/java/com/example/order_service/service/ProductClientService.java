package com.example.order_service.service;

import com.example.order_service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductClientService {

    private final WebClient webClient;

    public ProductClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<ProductDTO> getProductById(Long productId) {
        return webClient.get()
                .uri("/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorResume(e -> Mono.empty()); // Nếu lỗi, trả về empty để không làm gián đoạn
    }
}
