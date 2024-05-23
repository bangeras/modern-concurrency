package com.svb.modernconcurrencydemo.controller;

import com.svb.modernconcurrencydemo.model.stock.StockPrice;
import com.svb.modernconcurrencydemo.model.stock.StockPriceResult;
import com.svb.modernconcurrencydemo.services.stock.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * Case 3: Shutdown when first child thread succeeds
 * Ex: Query stock price across 3 exchanges, and publish the first available price
 */
@RestController
@RequestMapping("/api/stocks")
public class StockPriceController {
    @Autowired
    private StockPriceService stockService;

    @GetMapping("/fastest")
    public StockPriceResult getStockPriceFromFastestSource(@RequestParam String symbol){
        try {
            return stockService.getStockPriceFromFastestSource(symbol);
        } catch (Exception e) {
            throw new RuntimeException("Exception while get stock price", e);
        }
    }
}
