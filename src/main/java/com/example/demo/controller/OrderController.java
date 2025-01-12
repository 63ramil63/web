package com.example.demo.controller;

import com.example.demo.interfaces.IMain;
import com.example.demo.orders.Order;
import com.example.demo.product.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.OrderService;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController implements IMain {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    private String userEmail;

    private boolean isAdmin;

    @PostMapping("/order")
    public String order1(@RequestParam Long productId){
        getAuth();
        orderService.order(productId, userEmail);   //Сохранение заказа
        return "redirect:/market";
    }




    @Override
    public void getAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userEmail = authentication.getName();
        isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

    @Override
    public void getProd(Model model, List<Object> orders){
        if(!orders.isEmpty()){
            model.addAttribute("isOrder", true);
            model.addAttribute("orders", orders);
            int cost = 0;
            for(Object order: orders){
                if(order instanceof Order){                                 //Проверка что order является классом Order
                    Order _order = (Order) order;                           //Превращение из Object в Order
                    cost = cost + _order.getProduct().getProduct_price();
                }
                model.addAttribute("cost", cost);
            }
        }else{
            model.addAttribute("isOrder", false);
        }
    }


    @GetMapping("/home")
    public String home(Model model){
        getAuth();  //Получение email пользователя
        List<Order> orders = orderService.getOrder(userEmail);    //Коллекция заказов выбранных по email
        getProd(model, Arrays.asList(orders.toArray()));          //Превращение Order->Object

        model.addAttribute("isAuth", true);
        model.addAttribute("username", userEmail);
        model.addAttribute("path", "/home");
        model.addAttribute("username", userEmail);
        model.addAttribute("isAdmin", isAdmin);
        return "home";
    }

    @PostMapping("/delete")
    public String delete1(@RequestParam Long id){
        getAuth();
        orderService.delete(id, userEmail);
        return "redirect:/home";
    }
}
