package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AnimalService {

    private Animal animal;

    @Autowired
    public AnimalService(@Qualifier("dog") Animal animal) {
        this.animal = animal;
    }

    public void makeAnimalSpeak() {
        System.out.println(animal.speak());
    }
}