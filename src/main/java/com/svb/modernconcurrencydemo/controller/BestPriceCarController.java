package com.svb.modernconcurrencydemo.controller;

import com.svb.modernconcurrencydemo.model.ApiCallStatistics;
import com.svb.modernconcurrencydemo.model.BestPriceResult;
import com.svb.modernconcurrencydemo.services.BestPriceCarDealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/cars/marketplace")
public class BestPriceCarController {
    @Autowired
    private BestPriceCarDealerService bestPriceCarDealerService;

    public static final ScopedValue<ApiCallStatistics> apiCallStatisticsScopedValue = ScopedValue.newInstance();

    @GetMapping("/bestprice")
    public BestPriceResult getBestPricedCar(@RequestParam String make){
        ApiCallStatistics apiCallStatistics = new ApiCallStatistics();

        try {
            Instant before = Instant.now();

            BestPriceResult bestPriceResult = ScopedValue.callWhere(apiCallStatisticsScopedValue, apiCallStatistics, ()->bestPriceCarDealerService.getBestPricedCar(make));

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
