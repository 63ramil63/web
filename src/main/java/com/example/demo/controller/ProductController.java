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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProductController implements IMain {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private String username;

    private String button_path;

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
            button_path = "/home";
        }else{
            isAuth = false;
            username = "Войти";
            button_path = "/login";
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

    public void sortProd(Model model, List<Product> products, String sort){
        switch (sort) {
            case "increase":
                products.sort(Comparator.comparing(Product::getProduct_price));
                model.addAttribute("isSorted", true);
                model.addAttribute("firstOption", "increase");
                model.addAttribute("firstOptionText", "по возрастанию");
                model.addAttribute("secondOption", "decrease");
                model.addAttribute("secondOptionText", "по убыванию");
                break;
            case "decrease":
                products.sort(Comparator.comparing(Product::getProduct_price).reversed());
                model.addAttribute("isSorted", true);
                model.addAttribute("firstOption", "decrease");
                model.addAttribute("firstOptionText", "по убыванию");
                model.addAttribute("secondOption", "increase");
                model.addAttribute("secondOptionText", "по возрастанию");
                break;
            case null, default:
                model.addAttribute("isSorted", false);
                model.addAttribute("firstOption", "increase");
                model.addAttribute("firstOptionText", "по возрастанию");
                model.addAttribute("secondOption", "decrease");
                model.addAttribute("secondOptionText", "по убыванию");
                break;
        }
    }



    @GetMapping("/market")
    public String market(Model model, @RequestParam(required = false) String sort){     //sort - знач передаваемой кнопкой
        String pagePath = "/market";
        getAuth();
        List<Product> products = productService.getProducts();  //Получение в виде коллекции всех продуктов
        sortProd(model, products, sort);

        getProd(model, Arrays.asList(products.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов
        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", button_path);
        model.addAttribute("pagePath", pagePath);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        return "market";
    }


    @GetMapping("/sales")
    public String sales(Model model, @RequestParam(required = false) String sort){
        String pagePath = "/sales";
        getAuth();
        List<Product> products = productService.getSaleProduct();   //Получение в виде коллекции всех продуктов

        sortProd(model, products, sort);

        getProd(model, Arrays.asList(products.toArray()));          //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("path", button_path);
        model.addAttribute("pagePath", pagePath);

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
        model.addAttribute("path", button_path);
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