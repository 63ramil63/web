package com.example.demo.interfaces;

import com.example.demo.orders.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

public interface IMain {
    default void getOrder(Model model, List<Object> orders) {
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
    default void getProd(Model model, List<Object> products) {
        if(!products.isEmpty()){
            model.addAttribute("products", products);
            model.addAttribute("fail_load", false);
        }else{
            model.addAttribute("fail_load", false);
        }
    }
    default void getAuth(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        boolean isAuth;
        String userEmail;
        String button_path;
        if(!(authentication instanceof AnonymousAuthenticationToken) && authentication != null){
            isAuth = true;
            userEmail = authentication.getName();
            button_path = "/home";
        }else{
            isAuth = false;
            userEmail = "Войти";
            button_path = "/login";
        }
        model.addAttribute("username", userEmail);
        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", button_path);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("path", button_path);
    }
}
