package com.example.demo.services;

import com.example.demo.controller.ProductController;
import com.example.demo.product.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product){
        productRepository.save(product);
    }

    @Cacheable(value = "allProd", key = "'all'")
    public List<Product> getProducts(){
        System.out.println("Получение базы");
        List<Product> products = productRepository.findAll();
        System.out.println("Полученные продукты " + products);
        return products;
    }

    @Cacheable(value = "categories", key = "'Categories'")
    public List<String> getCategories(){
        return productRepository.findCategories();
    }


    @Cacheable(value = "saleProd", key = "'sale'")
    public List<Product> getSaleProduct(){
        System.out.println("Получение акций");
        List<Product> products = productRepository.findByIsSaleTrue();
        System.out.println("Полученные продукты " + products);
        return products;
    }
}
