package com.msedcl.main.Master.validation;

import java.math.BigDecimal;

import com.msedcl.main.dto.MeterReadingDetailsDto;
import com.msedcl.main.dto.ReadingDto;

public class MeterReadingValidator {

    private static final BigDecimal TOLERANCE =
            BigDecimal.valueOf(0.05);

    public static void validate(
            MeterReadingDetailsDto reading,
            String type) {

        if (reading == null) {
            throw new IllegalArgumentException(
                    type + " reading details are required");
        }

        validateReading(
                reading.getHeaderReading(),
                type + " Header");

        validateReading(
                reading.getSlotReading01(),
                type + " Slot 01");

        validateReading(
                reading.getSlotReading02(),
                type + " Slot 02");

        validateReading(
                reading.getSlotReading03(),
                type + " Slot 03");

        validateReading(
                reading.getSlotReading04(),
                type + " Slot 04");

        validateTotals(reading, type);
    }

    private static void validateReading(
            ReadingDto reading,
            String location) {

        if (reading == null) {
            throw new IllegalArgumentException(
                    location + " reading is required");
        }

        validate3Decimal(
                reading.getKw(),
                location + " KW");

        validate3Decimal(
                reading.getKwh(),
                location + " KWH");

        validate3Decimal(
                reading.getKva(),
                location + " KVA");

        validate3Decimal(
                reading.getKvah(),
                location + " KVAH");

        validate3Decimal(
                reading.getRkvahLag(),
                location + " RKVAH Lag");

        validate3Decimal(
                reading.getRkvahLead(),
                location + " RKVAH Lead");
    }

    private static void validate3Decimal(
            Double value,
            String fieldName) {

        if (value == null) {
            throw new IllegalArgumentException(
                    fieldName + " is required");
        }

        BigDecimal bd = BigDecimal.valueOf(value);

        if (bd.scale() > 3) {
            throw new IllegalArgumentException(
                    fieldName
                    + " can contain maximum 3 decimal places");
        }
    }

    private static void validateTotals(
            MeterReadingDetailsDto reading,
            String type) {

        ReadingDto h = reading.getHeaderReading();

        ReadingDto s1 = reading.getSlotReading01();
        ReadingDto s2 = reading.getSlotReading02();
        ReadingDto s3 = reading.getSlotReading03();
        ReadingDto s4 = reading.getSlotReading04();

        // KWH
        checkTotal(
                h.getKwh(),
                s1.getKwh()
                + s2.getKwh()
                + s3.getKwh()
                + s4.getKwh(),
                type + " KWH");

        // KVAH
        checkTotal(
                h.getKvah(),
                s1.getKvah()
                + s2.getKvah()
                + s3.getKvah()
                + s4.getKvah(),
                type + " KVAH");

        // RKVAH LAG
        checkTotal(
                h.getRkvahLag(),
                s1.getRkvahLag()
                + s2.getRkvahLag()
                + s3.getRkvahLag()
                + s4.getRkvahLag(),
                type + " RKVAH Lag");

        // RKVAH LEAD
        checkTotal(
                h.getRkvahLead(),
                s1.getRkvahLead()
                + s2.getRkvahLead()
                + s3.getRkvahLead()
                + s4.getRkvahLead(),
                type + " RKVAH Lead");

		/*
		 * // KW = MAX SLOT VALUE checkTotal( h.getKw(), max( s1.getKw(), s2.getKw(),
		 * s3.getKw(), s4.getKw()), type + " KW");
		 * 
		 * // KVA = MAX SLOT VALUE checkTotal( h.getKva(), max( s1.getKva(),
		 * s2.getKva(), s3.getKva(), s4.getKva()), type + " KVA");
		 */
        checkMaximum(
                h.getKw(),
                max(
                        s1.getKw(),
                        s2.getKw(),
                        s3.getKw(),
                        s4.getKw()),
                type + " KW");

        checkMaximum(
                h.getKva(),
                max(
                        s1.getKva(),
                        s2.getKva(),
                        s3.getKva(),
                        s4.getKva()),
                type + " KVA");
    }

    
    private static void checkMaximum(
            Double header,
            Double maxValue,
            String fieldName) {

        long headerValue = (long) Math.floor(header);
        long maxSlotValue = (long) Math.floor(maxValue);

        if (headerValue != maxSlotValue) {

            throw new IllegalArgumentException(
                    fieldName
                    + " mismatch. Header Value="
                    + BigDecimal.valueOf(header).toPlainString()
                    + ", Maximum Slot Value="
                    + BigDecimal.valueOf(maxValue).toPlainString());
        }
    }
    
    private static void checkTotal(
            Double header,
            Double calculated,
            String fieldName) {

        long headerValue = (long) Math.floor(header);
        long calculatedValue = (long) Math.floor(calculated);

        if (headerValue != calculatedValue) {

            throw new IllegalArgumentException(
                    fieldName
                    + " Total mismatch. Header Total="
                    + BigDecimal.valueOf(header).toPlainString()
                    + ", Calculated Slot Total="
                    + BigDecimal.valueOf(calculated).toPlainString());
        }
    }

    private static Double max(
            Double a,
            Double b,
            Double c,
            Double d) {

        return Math.max(
                Math.max(a, b),
                Math.max(c, d));
    }
}