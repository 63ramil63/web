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
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public String order1(@RequestParam Long productId, HttpServletRequest request, @RequestParam String email){
        orderService.order(productId, email);                  //Сохранение заказа
        String referer = request.getHeader("Referer");      //Получение ссылки на начальную страницу
        return "redirect:" + referer;
    }




    @Override
    public void getAuth(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        model.addAttribute("username", email);
        model.addAttribute("isAdmin", isAdmin);
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
        getAuth(model);                                           //Получение email пользователя
        String email = (String) model.getAttribute("username");
        List<Order> orders = orderService.getOrder(email);    //Коллекция заказов выбранных по email
        getProd(model, Arrays.asList(orders.toArray()));          //Превращение Order -> Object

        model.addAttribute("isAuth", true);

        model.addAttribute("path", "/home");
        model.addAttribute("username", email);

        return "home";
    }

    @PostMapping("/delete")
    public String delete1(@RequestParam Long id, @RequestParam String email){
        orderService.delete(id, email);
        return "redirect:/home";
    }
}
