package com.adit.groww_sip_tracker.logic;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MarketCalendar {
    public static boolean isMarketOpenToday() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY;
    }
}
