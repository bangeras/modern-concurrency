package com.svb.modernconcurrencydemo.services.order;

import com.svb.modernconcurrencydemo.model.order.Client;
import com.svb.modernconcurrencydemo.model.order.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Product getProduct(String cusip) throws InterruptedException {
        boolean isProductException = new Random().nextInt()%2 == 0;
        logger.info("Getting product info..isProductException={}", isProductException);
        if (isProductException) {
            throw new RuntimeException("Error getting product details");
        }
        Thread.sleep(Duration.ofSeconds(2));
        return new Product("GOOGL", "cusip1", "STOCK", "USD");
    }

    public Client getClient(String clientId) throws InterruptedException {
        boolean isClientException = new Random().nextInt()%2 == 0;
        logger.info("Getting product info..isClientException={}", isClientException);
        if (isClientException) {
            throw new RuntimeException("Error getting client details");
        }

        Thread.sleep(Duration.ofSeconds(3));
        return new Client(clientId, "1234567890", true);
    }

    public String createOrder(String cusip, String clientId){
        try {
            try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
                //Start running tasks in parallel
                StructuredTaskScope.Subtask clientSubTask = scope.fork(() -> getClient(clientId));
                StructuredTaskScope.Subtask productSubTask = scope.fork(() -> getProduct(cusip));

                //Wait till first child task fails. Send cancellation to other child tasks
                scope.join();
                scope.throwIfFailed();

                //Handle successful child task results
                Client client = (Client)clientSubTask.get();
                Product product = (Product) productSubTask.get();

                //Create order
                Thread.sleep(Duration.ofSeconds(1));
                String orderID = "ORD000001";

                logger.info("Order {} Created", orderID);
                return orderID;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(STR."Exception while creating an order \{ e.getMessage() }");
        }

    }
}
