package com.example.demo.controller;

import com.example.demo.orders.Order;
import com.example.demo.product.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String order(@RequestParam Long productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("Неверный ID продукта: " + productId));
        Order orders = new Order();
        orders.setProduct(product);
        orders.setUserEmail(userEmail);
        orderRepository.save(orders);
        return "redirect:/market";
    }


    @GetMapping("/home")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<Order> orders = orderRepository.findByUserEmail(userEmail);
        if(!orders.isEmpty()){
            model.addAttribute("isOrder", true);
        }else{
            model.addAttribute("isOrder", false);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("isAuth", true);
        model.addAttribute("username", userEmail);
        model.addAttribute("path", "/home");
        model.addAttribute("username", userEmail);
        return "home";
    }

    @PostMapping("/delete")
    public String delete (@RequestParam Long id){
        orderRepository.deleteById(id);
        return "redirect:/home";
    }

}
