package com.example.demo.services;

import com.example.demo.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.demo.repository.OrderRepository;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Cacheable(value = "orderCache", key = "'order'")
    public List<Order> getOrder(String email){
        System.out.println("Вывод заказов");
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orders;
    }
}
