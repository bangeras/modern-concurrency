package com.svb.modernconcurrencydemo.controller;

import com.svb.modernconcurrencydemo.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String createOrder(@RequestParam String cusip, @RequestParam String clientId){
        return orderService.createOrder(cusip, clientId);
    }
}
