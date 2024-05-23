package com.svb.modernconcurrencydemo.services.car;

import com.svb.modernconcurrencydemo.model.car.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDealer3Service implements ICarService {

    @Autowired
    private CarService carService;

    @Override
    public List<Car> getCarsByMake(String make) {
        return carService.getCarsByDealer("Classic", make);
    }
}
