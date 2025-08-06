package com.ihg.hotelbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/hotels-page")
    public String hotels() {
        return "hotels";
    }
    
    @GetMapping("/bookings-page")
    public String bookings() {
        return "bookings";
    }
    
    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
