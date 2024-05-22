package com.svb.modernconcurrencydemo.model;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public record Car(String model, String make, String price, Integer year, String dealer) {

    public Double getPrice() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        try {
            Number number = currencyFormat.parse(price);
            return number.doubleValue();
        } catch (ParseException e) {
            // Handle the exception (return null, throw a custom exception, or log the error)
            e.printStackTrace();
            return null;
        }
    }
}

