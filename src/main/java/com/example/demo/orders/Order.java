package com.example.demo.orders;


import com.example.demo.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private String userEmail;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public Product getProduct(){
        return product;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public String getUserEmail(){
        return userEmail;
    }
}
