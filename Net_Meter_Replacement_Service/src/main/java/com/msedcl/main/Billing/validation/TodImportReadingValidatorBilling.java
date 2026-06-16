package com.msedcl.main.Billing.validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.msedcl.main.dto.MeterReadingDetailsDto;

public class TodImportReadingValidatorBilling {

    private TodImportReadingValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            MeterReadingDetailsDto importReading) {

        String sql =
                "SELECT * " +
                "FROM MBC_LTIP_HT_BILL_DATA " +
                "WHERE CONSUMER_NUMBER = ? " +
                "AND (BILL_YEAR*100+BILL_MONTH) = ( " +
                "    SELECT MAX(BILL_YEAR*100+BILL_MONTH) " +
                "    FROM MBC_LTIP_HT_BILL_DATA " +
                "    WHERE CONSUMER_NUMBER = ? " +
                ")";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, consumerNumber);
            ps.setString(2, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Billing data not found for Consumer Number "
                                    + consumerNumber);
                }

                // =========================
                // KWH SLOT VALIDATION
                // =========================

                validateReading(
                        importReading.getSlotReading01().getKwh(),
                        rs.getDouble("KWH_READING1"),
                        "Import KWH Slot-1");

                validateReading(
                        importReading.getSlotReading02().getKwh(),
                        rs.getDouble("KWH_READING2"),
                        "Import KWH Slot-2");

                validateReading(
                        importReading.getSlotReading03().getKwh(),
                        rs.getDouble("KWH_READING3"),
                        "Import KWH Slot-3");

                validateReading(
                        importReading.getSlotReading04().getKwh(),
                        rs.getDouble("KWH_READING4"),
                        "Import KWH Slot-4");

                // =========================
                // KVAH SLOT VALIDATION
                // =========================

                validateReading(
                        importReading.getSlotReading01().getKvah(),
                        rs.getDouble("KVAH_READING1"),
                        "Import KVAH Slot-1");

                validateReading(
                        importReading.getSlotReading02().getKvah(),
                        rs.getDouble("KVAH_READING2"),
                        "Import KVAH Slot-2");

                validateReading(
                        importReading.getSlotReading03().getKvah(),
                        rs.getDouble("KVAH_READING3"),
                        "Import KVAH Slot-3");

                validateReading(
                        importReading.getSlotReading04().getKvah(),
                        rs.getDouble("KVAH_READING4"),
                        "Import KVAH Slot-4");

                // =========================
                // RKVAH LAG
                // =========================

                validateReading(
                        importReading.getSlotReading01().getRkvahLag(),
                        rs.getDouble("RKVAH_READING1"),
                        "Import RKVAH Lag Slot-1");

                validateReading(
                        importReading.getSlotReading02().getRkvahLag(),
                        rs.getDouble("RKVAH_READING2"),
                        "Import RKVAH Lag Slot-2");

                validateReading(
                        importReading.getSlotReading03().getRkvahLag(),
                        rs.getDouble("RKVAH_READING3"),
                        "Import RKVAH Lag Slot-3");

                validateReading(
                        importReading.getSlotReading04().getRkvahLag(),
                        rs.getDouble("RKVAH_READING4"),
                        "Import RKVAH Lag Slot-4");

                // =========================
                // RKVAH LEAD
                // =========================

                validateReading(
                        importReading.getSlotReading01().getRkvahLead(),
                        rs.getDouble("RKVAH_LEAD_READINGS_S1"),
                        "Import RKVAH Lead Slot-1");

                validateReading(
                        importReading.getSlotReading02().getRkvahLead(),
                        rs.getDouble("RKVAH_LEAD_READINGS_S2"),
                        "Import RKVAH Lead Slot-2");

                validateReading(
                        importReading.getSlotReading03().getRkvahLead(),
                        rs.getDouble("RKVAH_LEAD_READINGS_S3"),
                        "Import RKVAH Lead Slot-3");

                validateReading(
                        importReading.getSlotReading04().getRkvahLead(),
                        rs.getDouble("RKVAH_LEAD_READINGS_S4"),
                        "Import RKVAH Lead Slot-4");

                // =========================
                // HEADER VALIDATION
                // =========================

                validateReading(
                        importReading.getHeaderReading().getKwh(),
                        rs.getDouble("KWC_UNIT_KWH"),
                        "Import Header KWH");

                validateReading(
                        importReading.getHeaderReading().getKvah(),
                        rs.getDouble("KVAC_UNIT_KVAH"),
                        "Import Header KVAH");

                validateReading(
                        importReading.getHeaderReading().getRkvahLag(),
                        rs.getDouble("RKVAC_UNIT_RKVAH"),
                        "Import Header RKVAH Lag");

                validateReading(
                        importReading.getHeaderReading().getRkvahLead(),
                        rs.getDouble("RKVAC_UNIT_RKVAH_LEAD"),
                        "Import Header RKVAH Lead");

                // =========================
                // KW HEADER = MAX SLOT KW
                // =========================

                validateHeaderMaximum(
                        importReading.getHeaderReading().getKw(),
                        importReading.getSlotReading01().getKw(),
                        importReading.getSlotReading02().getKw(),
                        importReading.getSlotReading03().getKw(),
                        importReading.getSlotReading04().getKw(),
                        "KW");

                // =========================
                // KVA HEADER = MAX SLOT KVA
                // =========================

                validateHeaderMaximum(
                        importReading.getHeaderReading().getKva(),
                        importReading.getSlotReading01().getKva(),
                        importReading.getSlotReading02().getKva(),
                        importReading.getSlotReading03().getKva(),
                        importReading.getSlotReading04().getKva(),
                        "KVA");
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating TOD readings : "
                            + e.getMessage());
        }
    }

    private static void validateReading(
            Double currentReading,
            Double previousReading,
            String readingName) {

        if (currentReading < previousReading) {

            throw new IllegalArgumentException(
                    readingName
                            + " Current Reading "
                            + currentReading
                            + " is less than Previous Reading "
                            + previousReading
                            + ". Please provide valid current reading.");
        }
    }

    private static void validateHeaderMaximum(
            Double headerValue,
            Double slot1,
            Double slot2,
            Double slot3,
            Double slot4,
            String fieldName) {

        double maxValue =
                Math.max(
                        Math.max(slot1, slot2),
                        Math.max(slot3, slot4));

        if (Double.compare(headerValue, maxValue) != 0) {

            throw new IllegalArgumentException(
                    fieldName
                            + " Header Value "
                            + BigDecimal.valueOf(headerValue).toPlainString()
                            + " should be equal to maximum slot value "
                            + BigDecimal.valueOf(maxValue).toPlainString());
        }
    }
}