package com.example.demo.controller;

import com.example.demo.orders.Orders;
import com.example.demo.product.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/order")
    public String order(@RequestParam Long productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("Неверный ID продукта: " + productId));

        Orders orders = new Orders();
        orders.setProduct(product);
        orders.setUserEmail(userEmail);
        orderRepository.save(orders);
        return "redirect:/market";
    }

}
