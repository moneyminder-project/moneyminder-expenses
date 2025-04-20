package com.moneyminder.moneyminderexpenses.utils;

public class MoneyUtils {

    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
