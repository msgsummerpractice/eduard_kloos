package com.example;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.example")
public class App 
{
    public static void main(String[] args) {
       AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(App.class);

        AnimalService service = context.getBean(AnimalService.class);
        service.makeAnimalSpeak();

        context.close();
    }
}
