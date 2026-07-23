package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        HelloService service = context.getBean(HelloService.class);
        System.out.println(service.getMessage());
        ((AnnotationConfigApplicationContext) context).close();
    }
}
