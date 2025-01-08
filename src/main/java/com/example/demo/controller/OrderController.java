package com.example.demo.controller;

import com.example.demo.orders.Order;
import com.example.demo.product.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/order")
    public String order(@RequestParam Long productId){  //RequestParam берет productID из нажатой кнопки
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();     //Получение пользователя
        String userEmail = authentication.getName();        //Получение email пользователя
        Product product = productRepository.findById(productId)     //Получение продукта по ID
                .orElseThrow(()-> new IllegalArgumentException("Неверный ID продукта: " + productId));  //Выкидывание ошибки при неверном ID
        Order orders = new Order();
        orders.setProduct(product);
        orders.setUserEmail(userEmail);
        orderRepository.save(orders);
        return "redirect:/market";
    }


    @GetMapping("/home")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();     //Получение пользователя
        String userEmail = authentication.getName();    //Получение email пользователя
        List<Order> orders = orderRepository.findByUserEmail(userEmail);    //Коллекция заказов выбранных по email
        if(!orders.isEmpty()){
            model.addAttribute("isOrder", true);
        }else{
            model.addAttribute("isOrder", false);
        }
        int cost = 0;
        for(Order order: orders){   //Подсчет итоговой суммы
            cost = cost + order.getProduct().getProduct_price();
        }
        model.addAttribute("cost", cost);
        model.addAttribute("orders", orders);
        model.addAttribute("isAuth", true);
        model.addAttribute("username", userEmail);
        model.addAttribute("path", "/home");
        model.addAttribute("username", userEmail);
        return "home";
    }

    @PostMapping("/delete")
    public String delete (@RequestParam Long id){
        orderRepository.deleteById(id);     //Удаление заказа по ID заказа
        return "redirect:/home";
    }
}
