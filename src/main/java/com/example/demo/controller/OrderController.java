package com.example.demo.controller;

import com.example.demo.interfaces.IMain;
import com.example.demo.orders.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController implements IMain {
    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public String order(@RequestParam Long productId, HttpServletRequest request, @RequestParam String email){
        orderService.order(productId, email);                  //Сохранение заказа
        String referer = request.getHeader("Referer");      //Получение ссылки на начальную страницу
        return "redirect:" + referer;
    }

    public String getRightWord(String count){
        char c = count.charAt(count.length()-1);
        int _count = Integer.parseInt(count);
        if (_count > 10 && _count < 20){
            return "товаров";
        }else {
            return switch (c) {
                case '1' -> _count + " товар";
                case '2', '3', '4' -> _count + " товара";
                case '5', '6', '7', '8', '9', '0' -> _count + " товаров";
                default -> "ошибка";
            };
        }
    }


    @GetMapping("/home")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getAuth(model, authentication);                                           //Получение email пользователя
        String email = (String) model.getAttribute("username");
        List<Order> orders = orderService.getOrder(email);    //Коллекция заказов выбранных по email
        getOrder(model, Arrays.asList(orders.toArray()));          //Превращение Order -> Object
        String count = getRightWord(String.valueOf(orders.size()));
        model.addAttribute("count", count);
        return "home";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam String email){
        orderService.delete(id, email);
        return "redirect:/home";
    }
}
