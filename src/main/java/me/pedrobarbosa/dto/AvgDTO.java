package me.pedrobarbosa.dto;

import me.pedrobarbosa.util.DateUtil;

import java.time.LocalDate;

public record AvgDTO(Long id, String name, String startDate, String endDate, Integer totalTasks, Long averageHours) {

    public LocalDate getStartDate() {
        return DateUtil.parseDate(startDate);
    }

    public LocalDate getEndDate() {
        return DateUtil.parseDate(endDate);
    }
}