package com.example.demo.interfaces;

import org.springframework.ui.Model;

import java.util.List;

public interface IMain {
    void getAuth(Model model);
    void getProd(Model model, List<Object> obj);
}
