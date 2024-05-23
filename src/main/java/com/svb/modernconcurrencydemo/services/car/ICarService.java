package com.svb.modernconcurrencydemo.services.car;

import com.svb.modernconcurrencydemo.model.car.Car;

import java.util.List;

public interface ICarService {
    public List<Car> getCarsByMake(String make);
}
