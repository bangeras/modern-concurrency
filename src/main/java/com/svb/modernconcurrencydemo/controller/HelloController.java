package com.svb.modernconcurrencydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam("name") String name){
        return "Hello " + name;
    }

    @GetMapping("/thread-info")
    public String getThreadName() throws InterruptedException {
        Thread t =  Thread.ofVirtual().name("vt-svb").start(()->{
            System.out.println("Virtual Thread started..");
        });
        return t.getName();
    }
}
