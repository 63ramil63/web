package com.example.demo.config;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config{

    @Autowired
    private UserService userService;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/", "/login", "/registerform", "/sales","/market").permitAll()       //Страницы, доступные всем
                        .requestMatchers("/styles/**", "/images/**").permitAll()    //Ресурсы доступные всем
                        .requestMatchers("/home", "/order", "/delete").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().hasAuthority("ADMIN"))    //Абсолютно все страницы и ресурсы доступны пользователям с ролью ADMIN
                .formLogin(form -> form
                        .usernameParameter("email")             //Указываем название ввода данных с атрибутом th:field="email"
                        .passwordParameter("password")          //Указываем название ввода данных с атрибутом th:field="password"
                        .loginPage("/login")                    //Страница для логина
                        .defaultSuccessUrl("/market", true)     //Страница при входе
                        .permitAll())                           //Доступ разрешен всем
                .logout(logout -> logout                        //Настройка страницы выхода
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());
        return http.build();
    }


    @Bean
    public PasswordEncoder encoder(){       //Шифрование паролей
        return new BCryptPasswordEncoder();
    }
}
