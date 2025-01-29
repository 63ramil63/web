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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProductController implements IMain {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    @Override
    public void getAuth(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    @Override
    public void getProd(Model model, List<Object> products){
        boolean fail_load;
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
        switch (sort) {                                                     //сортировка предметов по requestparam
            case "increase":
                products.sort(Comparator.comparing(Product::getProduct_price));
                model.addAttribute("currentOption", "по возрастанию");
                model.addAttribute("currentSort", "increase");
                break;
            case "decrease":
                products.sort(Comparator.comparing(Product::getProduct_price).reversed());
                model.addAttribute("currentOption", "по убыванию");
                model.addAttribute("currentSort", "decrease");
                break;
            case null, default:
                products.sort(Comparator.comparing(Product::getId));
                model.addAttribute("currentOption", "по умолчанию");
                model.addAttribute("currentSort", "");
                break;
        }
    }

    public void filterProd(Model model, List<Product> products, String filter){
        List<Product> newProducts = new ArrayList<>();
        products.forEach(product -> {
            if(product.getCategory().equals(filter)){
                newProducts.add(product);
            }
        });
        getProd(model, Arrays.asList(newProducts.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов
        model.addAttribute("products", newProducts);
        model.addAttribute("currentFilter", filter);
    }


    @GetMapping("/market")
    public String market(Model model, @RequestParam(required = false) String sort, @RequestParam(required = false) String filter){     //sort - знач передаваемой кнопкой
        String pagePath = "/market";
        getAuth(model);
        List<Product> products = productService.getProducts();  //Получение в виде коллекции всех продуктов
        sortProd(model, products, sort);
        if (filter != null && !filter.isEmpty()) {
            filterProd(model, products, filter);
        }else {
            getProd(model, Arrays.asList(products.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов
            model.addAttribute("products", products);
            model.addAttribute("currentFilter", filter);
        }
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("pagePath", pagePath);
        return "market";
    }


    @GetMapping("/sales")
    public String sales(Model model, @RequestParam(required = false) String sort, @RequestParam(required = false) String filter){
        String pagePath = "/sales";
        getAuth(model);
        List<Product> products = productService.getSaleProduct();   //Получение в виде коллекции всех продуктов

        sortProd(model, products, sort);

        if (filter != null && filter.equals("nout")) {
            System.out.println(true);
            List<Product> newProducts = new ArrayList<>();
            products.forEach(product -> {
                if(product.getProduct_name().contains("Ф")){
                    newProducts.add(product);
                }
            });

            getProd(model, Arrays.asList(newProducts.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов
            model.addAttribute("products", newProducts);
            model.addAttribute("currentFilter", filter);
        }else {
            getProd(model, Arrays.asList(products.toArray()));      //products.toArray - Product в массив объектов Object, Arrays.asList - массив Объектов в список Объектов
            model.addAttribute("products", products);
            model.addAttribute("currentFilter", filter);
        }
        model.addAttribute("pagePath", pagePath);
        return "market";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        getAuth(model);
        model.addAttribute("product", new Product());
        return "admin";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product){
        productService.addProduct(product);
        return "redirect:/admin";
    }
}