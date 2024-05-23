package com.svb.modernconcurrencydemo.controller;

import com.svb.modernconcurrencydemo.model.car.Car;
import com.svb.modernconcurrencydemo.services.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarsController {
    @Autowired
    private CarService carService;

    @GetMapping("")
    public List<Car> getCarsByDealer(@RequestParam String dealer, @RequestParam String make){
        return carService.getCarsByDealer(dealer, make);
    }

    
}
