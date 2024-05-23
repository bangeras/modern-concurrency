package com.svb.modernconcurrencydemo.services.car;

import com.svb.modernconcurrencydemo.controller.BestPriceCarController;
import com.svb.modernconcurrencydemo.model.ApiCallStatistics;
import com.svb.modernconcurrencydemo.model.car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(Car.class);


    @Value("${services.cars.url}")
    private String carsURI;

    private RestClient restClient = RestClient.create();

    protected List<Car> getAllCars(){
        logger.info("Getting cars data from {}", carsURI);

        List<Car> allCars = restClient.get()
                .uri(carsURI)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Car>>() {});
        return allCars;
    }

    public List<Car> getCarsByDealer(String dealer, String make){
        logger.info("Fetching {} cars data for dealer={}", make, dealer);
        Instant before = Instant.now();

        List<Car> allCars = getAllCars();
        List<Car> filteredCars = allCars.stream()
                .filter(car -> car.dealer().contains(dealer) && car.make().contains(make))
                .collect(Collectors.toList());
        logger.info("{} {} cars sold by {}", filteredCars.size(), make, dealer);

        Instant after = Instant.now();
        Duration duration = Duration.between(before, after);
        ApiCallStatistics apiCallStatistics = BestPriceCarController.apiCallStatisticsScopedValue.get();
        apiCallStatistics.addTiming(dealer, duration.toMillis());

        return filteredCars;
    }

}
