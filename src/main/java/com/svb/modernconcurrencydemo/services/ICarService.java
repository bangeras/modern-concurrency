package com.svb.modernconcurrencydemo.services;

import com.svb.modernconcurrencydemo.model.Car;

import java.util.List;

public interface ICarService {
    public List<Car> getCarsByMake(String make);
}
