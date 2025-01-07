package com.example.demo.controller;

import com.example.demo.config.Config;
import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "allCache", key = "'prod'")
    public List<Product> getProducts(){
        System.out.println("Получение базы");
        List<Product> product = productRepository.findAll();
        System.out.println("Полученные продукты " + product);
        return product;
    }
    @Cacheable(value = "saleCache", key = "'sale'")
    public List<Product> getSaleProducts(){
        System.out.println("Получение акции");
        return productRepository.findByIsSaleTrue();
    }

    @GetMapping("/market")
    public String market(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        if(!(authentication instanceof AnonymousAuthenticationToken) && authentication != null){
            isAuth = true;
            username = authentication.getName();
            path = "/home";
        }else{
            isAuth = false;
            username = "Войти";
            path = "/login";
        }
        List<Product> products = productService.getProducts();
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
            path = "/home";
        }else{
            isAuth = false;
            username = "Войти";
            path = "/login";
        }
        List<Product> products = productService.getSaleProduct();
        if(!products.isEmpty()){
            model.addAttribute("products", products);
            fail_load = false;
            model.addAttribute("fail_load", fail_load);
            System.out.println(fail_load);
        }else{
            fail_load = true;
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