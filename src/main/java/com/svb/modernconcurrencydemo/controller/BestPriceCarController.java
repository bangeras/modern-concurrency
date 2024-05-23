package com.svb.modernconcurrencydemo.controller;

import com.svb.modernconcurrencydemo.model.ApiCallStatistics;
import com.svb.modernconcurrencydemo.model.car.BestCarPriceResult;
import com.svb.modernconcurrencydemo.services.car.BestPriceCarDealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

/**
 * Case 1: Shutdown when all threads complete.
 * Ex: Query car make price across 3 dealers, and publish the best price along with all available quotes
 */
@RestController
@RequestMapping("/api/cars/marketplace")
public class BestPriceCarController {
    @Autowired
    private BestPriceCarDealerService bestPriceCarDealerService;

    public static final ScopedValue<ApiCallStatistics> apiCallStatisticsScopedValue = ScopedValue.newInstance();

    @GetMapping("/bestprice")
    public BestCarPriceResult getBestPricedCar(@RequestParam String make){
        ApiCallStatistics apiCallStatistics = new ApiCallStatistics();

        try {
            Instant before = Instant.now();

            BestCarPriceResult bestPriceResult = ScopedValue.callWhere(apiCallStatisticsScopedValue, apiCallStatistics, ()->bestPriceCarDealerService.getBestPricedCar(make));

            Instant after = Instant.now();
            Duration duration = Duration.between(before, after);
            apiCallStatistics.addTiming("Best Price MarketPlace", duration.toMillis());

            apiCallStatistics.print();

            return bestPriceResult;
        } catch (Exception e) {
            throw new RuntimeException("Exception while getting best price for car:", e);
        }
    }
}
