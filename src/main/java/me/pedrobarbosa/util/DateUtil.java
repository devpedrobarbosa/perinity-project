package me.pedrobarbosa.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /*
     * Lê dia/mês/ano e retorna uma instância de LocalDate
     * */
    public static LocalDate parseDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    /*
     * Retorna em texto uma data formatada em dia/mês/ano
     * */
    public static String writeDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }
}