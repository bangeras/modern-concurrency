package com.svb.modernconcurrencydemo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApiCallStatistics {
    private static final Logger logger = LoggerFactory.getLogger(ApiCallStatistics.class);

    private final Map<String, Long> timingMap = Collections.synchronizedMap(new HashMap<String, Long>());

    public Map<String, Long> getTimingMap() {
        return timingMap;
    }

    public void addTiming(String dealer, Long time){
        timingMap.put(dealer, time);
    }

    public void print(){
        timingMap.forEach((dealer, time)->{
            logger.info("Dealer store {} took {} ms", dealer, time);
        });
    }
}
