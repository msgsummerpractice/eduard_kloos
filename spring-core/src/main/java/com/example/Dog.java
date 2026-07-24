package com.example;

import org.springframework.stereotype.Component;

@Component("dog")
public class Dog implements Animal {

    @Override
    public String speak() {
        return "Hauhau!";
    }

}
