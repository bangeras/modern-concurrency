package com.svb.modernconcurrencydemo.services;

import com.svb.modernconcurrencydemo.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDealer1Service implements ICarService {

    @Autowired
    private CarService carService;

    @Override
    public List<Car> getCarsByMake(String make) {
        return carService.getCarsByDealer("Popular", make);
    }
}
