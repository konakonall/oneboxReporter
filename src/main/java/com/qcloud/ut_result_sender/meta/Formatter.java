package com.qcloud.ut_result_sender.meta;

import java.text.DecimalFormat;

public class Formatter {

    public static String toRatePercent(long numerator, long denominator) {
        if (denominator == 0) {
            return "N/A";
        }
        double rate = numerator / (double)denominator;
        return toRatePercent(rate);
    }

    public static String toRatePercent(double rate) {
        if (rate == 0) {
            return "N/A";
        }
        if (rate == 1) {
            return "100%";
        }
        return new DecimalFormat("0.00").format(100 * rate) + "%";
    }
}
