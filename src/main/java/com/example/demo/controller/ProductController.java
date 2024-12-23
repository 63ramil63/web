package com.example.demo.controller;

import com.example.demo.config.Config;
import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private String username;

    private String path;

    private boolean isAuth;

    private boolean fail_load;

    @GetMapping("/market")
    public String market(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        if(!(authentication instanceof AnonymousAuthenticationToken) && authentication != null){
            isAuth = true;
            username = authentication.getName();
            path = "/purchases";
        }else{
            isAuth = false;
            username = "Войти";
            path = "/login";
        }

        List<Product> products = productRepository.findAll();
        if(!products.isEmpty()){
            model.addAttribute("products", products);
            fail_load = false;
            model.addAttribute("fail_load", fail_load);
        }else{
            fail_load = true;
            model.addAttribute("fail_load", fail_load);
        }

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", path);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);


        return "market";
    }


    @GetMapping("/sales")
    public String sales(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        if(!(authentication instanceof AnonymousAuthenticationToken) && authentication != null){   //без аутентификации
            isAuth = true;
            username = authentication.getName();
            path = "/purchases";
        }else{
            isAuth = false;
            username = "Войти";
            path = "/login";
        }


        List<Product> products = productRepository.findByIsSaleTrue();
        if(!products.isEmpty()){
            model.addAttribute("products", products);
            fail_load = false;
            model.addAttribute("fail_load", fail_load);
            System.out.println(fail_load);
        }else{
            fail_load = true;
            System.out.println(fail_load);
            model.addAttribute("fail_load", fail_load);
        }

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", path);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("products", products);
        return "market";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("product", new Product());
        return "admin";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product){
        productService.addProduct(product);
        return "redirect:/admin";
    }
}