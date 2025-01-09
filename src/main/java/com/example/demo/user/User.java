package com.example.demo.user;

import com.example.demo.roles.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {
    @Id     //Указываем что это ключевое поле
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Указываем что значение будет генерироваться бд
    private Long Id;

    private String email;

    private String password;

    @Transient  //Указываем, что это поле не должно сохраняться в бд
    private String confirmPassword;

    @Enumerated(EnumType.STRING)    //Указываем, что поле role является перечислением Enum
    private Role role;

    //Необходимые методы Get/set для Spring
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public void setConfirmPassword(String confirmPassword){this.confirmPassword = confirmPassword;}
    public String getConfirmPassword(){return confirmPassword;}

    public void getRole(Role role){
        this.role = role;
    }

    public Long getId(){
        return Id;
    }

    public  User(){
        setRole(Role.USER);
    }

    public void setRole(Role role){
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }
}
