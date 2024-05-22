package com.svb.modernconcurrencydemo.model;

import java.util.List;

public record BestPriceResult(ApiCallStatistics apiCallStatistics, Car bestPriceCar, List<Car> carDeals) {
}
