package com.example.demo.controller;

import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.interfaces.IMain;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController implements IMain {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private String username;

    private String path;

    private boolean isAuth;

    private boolean fail_load;

    private boolean isAdmin;

    @Override
    public void getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAdmin = authentication.getAuthorities()
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
    }

    @Override
    public void getProd(Model model, List<Object> products){
        if(!products.isEmpty()){
            model.addAttribute("products", products);
            fail_load = false;
            model.addAttribute("fail_load", fail_load);
        }else{
            fail_load = true;
            model.addAttribute("fail_load", fail_load);
        }
    }

    @GetMapping("/market")
    public String market(Model model){
        getAuth();
        List<Product> products = productService.getProducts();  //Получение в виде коллекции всех продуктов
        getProd(model, Arrays.asList(products.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", path);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        return "market";
    }


    @GetMapping("/sales")
    public String sales(Model model){
        getAuth();
        List<Product> products = productService.getSaleProduct();   //Получение в виде коллекции всех продуктов
        getProd(model, Arrays.asList(products.toArray()));          //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", path);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("products", products);
        return "market";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        getAuth();
        model.addAttribute("product", new Product());
        model.addAttribute("username", username);
        model.addAttribute("path", path);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAuth", isAuth);
        return "admin";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product){
        productService.addProduct(product);
        return "redirect:/admin";
    }
}