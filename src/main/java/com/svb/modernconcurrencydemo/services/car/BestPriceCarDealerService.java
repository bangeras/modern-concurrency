package com.svb.modernconcurrencydemo.services.car;

import com.svb.modernconcurrencydemo.controller.BestPriceCarController;
import com.svb.modernconcurrencydemo.model.car.BestCarPriceResult;
import com.svb.modernconcurrencydemo.model.car.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Collectors;

@Service
public class BestPriceCarDealerService {

    /**
     * For convenience, we have kept these services in the same JVM. In production, think of them as microservices
     * accessed in this JVM via RestClient invocations.
     */
    @Autowired
    private CarDealer1Service carDealer1Service;
    @Autowired
    private CarDealer2Service carDealer2Service;
    @Autowired
    private CarDealer3Service carDealer3Service;

    private List<Car> getCarsFromAllDealers(String make) throws InterruptedException{
        try (var structuredTaskScope = new StructuredTaskScope<List<Car>>()) { //try-with-scope block
            //Start running tasks in parallel
            List<StructuredTaskScope.Subtask<List<Car>>> subtaskList = new ArrayList();
            subtaskList.add(structuredTaskScope.fork(() -> carDealer1Service.getCarsByMake(make)));
            subtaskList.add(structuredTaskScope.fork(() -> carDealer2Service.getCarsByMake(make)));
            subtaskList.add(structuredTaskScope.fork(() -> carDealer3Service.getCarsByMake(make)));

            //Wait for all tasks to complete (success or failed)
            structuredTaskScope.join();

            //dump stacktrace for all failures
            subtaskList.stream()
                    .filter(t -> t.state() == StructuredTaskScope.Subtask.State.FAILED)
                    .map(StructuredTaskScope.Subtask::exception)
                    .forEach(e -> e.printStackTrace());

            //Collect all successful responses
            List<List<Car>> listOfCarsList = subtaskList.stream()
                    .filter(t -> t.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(StructuredTaskScope.Subtask::get)
                    .collect(Collectors.toList());

            List<Car> listOfCars = listOfCarsList.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            return listOfCars;
        }
    }

    public BestCarPriceResult getBestPricedCar(String make) {
        try {
            List<Car> cars = getCarsFromAllDealers(make);

           Car bestPricedCar = cars.stream()
                    .min(Comparator.comparing(Car::getPrice))
                    .orElseThrow();
            return new BestCarPriceResult(BestPriceCarController.apiCallStatisticsScopedValue.get(), bestPricedCar, cars);
        }
        catch (Exception e){
            throw new RuntimeException("Exception while getting best priced car", e);
        }

    }
}
