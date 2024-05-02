package org.regikeskus.events.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class IdValidationUtils {

    private IdValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final LocalDate EARLIEST_VALID_DATE = LocalDate.of(1917, 6, 18);

    public static boolean isValidEstonianPersonalId(String id) {
        if (id == null || !id.matches("\\d{11}")) {
            return false;
        }
        return checkDate(id) && checkChecksum(id);
    }

    private static boolean checkDate(String id) {
        int yearPrefix;
        switch (id.charAt(0)) {
            case '3', '4' -> yearPrefix = 1900;
            case '5', '6' -> yearPrefix = 2000;
            default -> {
                return false;
            }
        }

        String year = String.valueOf(yearPrefix + Integer.parseInt(id.substring(1, 3)));
        String month = id.substring(3, 5);
        String day = id.substring(5, 7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate birthDate = LocalDate.parse(year + "-" + month + "-" + day, formatter);
            return (!birthDate.isAfter(LocalDate.now())) && (birthDate.isAfter(EARLIEST_VALID_DATE) || birthDate.equals(EARLIEST_VALID_DATE));
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private static boolean checkChecksum(String id) {
        int[] weightsFirst = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
        int[] weightsSecond = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};
        int checksum = Character.getNumericValue(id.charAt(10));

        int sum = calculateChecksum(id, weightsFirst);
        if (sum % 11 != 10) {
            return sum % 11 == checksum;
        }

        sum = calculateChecksum(id, weightsSecond);
        if (sum % 11 != 10) {
            return sum % 11 == checksum;
        }

        return sum % 10 == checksum;
    }

    private static int calculateChecksum(String id, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(id.charAt(i)) * weights[i];
        }
        return sum;
    }
}
