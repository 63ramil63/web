package com.example.demo.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product_name;
    private String image_url;
    private int product_price;
    private String description;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setProduct_name(String product_name){
        this.product_name = product_name;
    }

    public String getProduct_name(){
        return this.product_name;
    }

    public void setImage_url(String url){
        this.image_url = url;
    }

    public String getImage_url(){
        return image_url;
    }

    public void setProduct_price(int price){
        this.product_price = price;
    }

    public int getProduct_price(){
        return product_price;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
