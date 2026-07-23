package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

            HelloService helloService = context.getBean("helloService", HelloService.class);

            System.out.println(helloService.getMessage());

            if (context instanceof ClassPathXmlApplicationContext) {
                ((ClassPathXmlApplicationContext) context).close();
            }
        } catch (Exception e) {
            System.err.println("Error loading Spring context: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
