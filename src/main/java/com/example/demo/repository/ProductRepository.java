package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.product.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsSaleTrue();       //Поиск продуктов по булевой переменной isSale со значением True
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findCategories();
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isSale = true")
    List<String> findSaleCategories();
}
