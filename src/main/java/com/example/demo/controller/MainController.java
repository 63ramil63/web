package com.example.demo.controller;
import com.example.demo.user.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String welcome(){
        return "redirect:/market";
    }

    @GetMapping("/registerform")
    public String register(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/market";                              //Если пользователь авторизован, то переводит на глав страницу
        }
        model.addAttribute("user", new User());         //добавление объекта User к Форме
        return "createAccount";
    }

    @PostMapping("/register")
    public String submit(@ModelAttribute User user, Model model){   //ModelAttribute указывает то, что из формы мы получаем объект User
        if(user.getPassword().equals(user.getConfirmPassword())){   //Проверка совпадения двух полей паролей при регистрации
            if(!userService.registerUser(user)){
                model.addAttribute("error", "Пользователь с таким email уже существует");
                return "createAccount";
            }
            return "redirect:/login";
        }
        model.addAttribute("error", "Пароли не совпадают");     //Добавление надписи ошибки
        return "createAccount";
    }

    @GetMapping("/login")
    public String login(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/market";      //Если пользователь авторизован, то перенаправляет на глав страницу
        }
        model.addAttribute("user", new User());     //добавление объекта User к Форме
        return "login";
    }
}
