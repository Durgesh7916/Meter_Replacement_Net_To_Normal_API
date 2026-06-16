package com.msedcl.main.Master.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    private DateValidator() {
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void validateReplacementDate(
            String replacementDate) {

        validateDate(
                replacementDate,
                "Replacement Date");
    }

    public static void validateGuaranteeEndDate(
            String guaranteeEndDate) {

        validateDate(
                guaranteeEndDate,
                "Guarantee End Date");
    }

    private static void validateDate(
            String date,
            String fieldName) {

        if (date == null || date.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName + " is required.");
        }

        try {

            LocalDate.parse(
                    date,
                    FORMATTER);

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    fieldName
                    + " must be in DD-MM-YYYY format. "
                    + "Example : 12-04-2026");
        }
    }
}