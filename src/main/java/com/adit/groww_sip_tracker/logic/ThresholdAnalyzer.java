package com.adit.groww_sip_tracker.logic;

import java.util.List;

public class ThresholdAnalyzer {
    public static boolean shouldBuy(double currentNav, List<Double> historicalNavs, double dipPercent) {
        double avg = historicalNavs.stream().mapToDouble(Double::doubleValue).average().orElse(currentNav);
        double threshold = avg * (1.0 - dipPercent / 100.0);
        System.out.printf("📊 Average NAV: %.2f | Current NAV: %.2f | Threshold: %.2f%n", avg, currentNav, threshold);
        return currentNav < threshold;
    }
}


