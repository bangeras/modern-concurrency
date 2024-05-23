package com.svb.modernconcurrencydemo.services.stock;

import com.svb.modernconcurrencydemo.model.stock.StockPrice;
import com.svb.modernconcurrencydemo.model.stock.StockPriceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

@Service
public class StockPriceService {
    private static final Logger logger = LoggerFactory.getLogger(StockPriceService.class);

    @Value("${services.stocks.url}")
    private String stocksURI;

    private RestClient restClient = RestClient.create();

    public StockPriceResult getStockPrice(String symbol, String source){
        logger.info("Fetching {} stock price from {}", source, stocksURI);
        List<StockPrice> stocks = restClient.get()
                .uri(stocksURI)
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockPrice>>() {});

        StockPrice stockPrice = stocks.stream()
                .filter(s -> s.symbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElseThrow();

        StockPriceResult stockPriceResult = new StockPriceResult(source, stockPrice);
        return stockPriceResult;
    }

    public StockPriceResult getStockPriceFromReuters(String symbol){
        return getStockPrice(symbol, "Reuters");
    }

    public StockPriceResult getStockPriceFromBloomberg(String symbol){
        return getStockPrice(symbol, "Bloomberg");
    }

    public StockPriceResult getStockPriceFromMarkit(String symbol){
        return getStockPrice(symbol, "Markit");
    }

    public StockPriceResult getStockPriceFromFastestSource(String symbol) throws InterruptedException, ExecutionException {
        try (var structuredTaskScope = new StructuredTaskScope.ShutdownOnSuccess<StockPriceResult>()){
            //Start running tasks in parallel
            List<StructuredTaskScope.Subtask<StockPriceResult>> subtaskList = new ArrayList<>();
            subtaskList.add(structuredTaskScope.fork(() -> getStockPriceFromReuters(symbol)));
            subtaskList.add(structuredTaskScope.fork(() -> getStockPriceFromBloomberg(symbol)));
            subtaskList.add(structuredTaskScope.fork(()-> getStockPriceFromMarkit(symbol)));

            // Join the scope and wait for the first successful result
            structuredTaskScope.join();

            StockPriceResult stockPriceResult = structuredTaskScope.result();
            return stockPriceResult;
        }
    }
}
