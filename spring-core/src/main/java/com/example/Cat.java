package com.example;

import org.springframework.stereotype.Component;

@Component("cat")
public class Cat implements Animal {
    
    @Override
    public String speak() {
        return "Miau!";
    }
}
