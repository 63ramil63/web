package com.example.demo.services;

import com.example.demo.cacheConfig.CacheConfig;
import com.example.demo.orders.Order;
import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.demo.repository.OrderRepository;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Cacheable(value = "orderCache", key = "#email")                                                    //ключ email позволяет обновлять кэш только для выбранного пользователя
    public List<Order> getOrder(String email){
        System.out.println("order "+email);
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orders;
    }

    @CachePut(value = "orderCache", key = "#email")                                                     //ключ email позволяет обновлять кэш только для выбранного пользователя
    public List<Order> order(Long productId, String email){                                             //RequestParam берет productID из нажатой кнопки
        Product product = productRepository.findById(productId)                                         //Получение продукта по ID
                .orElseThrow(()-> new IllegalArgumentException("Неверный ID продукта: " + productId));  //Выкидывание ошибки при неверном ID
        Order orders = new Order();
        orders.setProduct(product);
        orders.setUserEmail(email);
        orderRepository.save(orders);

        return orderRepository.findByUserEmail(email);                                                  //Возвращаем List<order> для обновления кэша
    }

    @CacheEvict(value = "orderCache", key = "#email")
    public void delete (Long id, String email){
        orderRepository.deleteById(id);                                                                 //Удаление заказа по ID заказа
    }
}
