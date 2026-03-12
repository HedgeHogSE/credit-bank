package ru.neoflex.calculator.util;

import java.math.BigDecimal;

public class Util {

    public static boolean isGreaterThan (BigDecimal num1, BigDecimal num2) {
        int val = num1.compareTo(num2);
        return val > 0;
    }
}
