package com.example.demo.repository;

import com.example.demo.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String userEmail);  //Указываем поиск продуктов по userEmail взятого из Order
}
