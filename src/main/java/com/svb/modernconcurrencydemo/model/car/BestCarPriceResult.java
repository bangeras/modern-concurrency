package com.svb.modernconcurrencydemo.model.car;

import com.svb.modernconcurrencydemo.model.ApiCallStatistics;

import java.util.List;

public record BestCarPriceResult(ApiCallStatistics apiCallStatistics, Car bestPriceCar, List<Car> carDeals) {
}
