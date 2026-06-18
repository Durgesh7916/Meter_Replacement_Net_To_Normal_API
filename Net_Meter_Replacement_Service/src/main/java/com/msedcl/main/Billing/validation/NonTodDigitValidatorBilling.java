package com.msedcl.main.Billing.validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.msedcl.main.dto.NonTodReadingDto;

public class NonTodDigitValidatorBilling {

    private NonTodDigitValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            NonTodReadingDto nonTodReading) {

        try (Connection con = dataSource.getConnection()) {

            String ltHtCd =
                    getLtHtCd(
                            con,
                            consumerNumber);

            int noOfDigits;

            if ("LT".equalsIgnoreCase(ltHtCd)) {

                noOfDigits =
                        getDigitsFromWebData(
                                con,
                                consumerNumber);

            } else {

                noOfDigits =
                        getDigitsFromLtipData(
                                con,
                                consumerNumber);
            }

            checkDigits(
                    nonTodReading.getImportFinalReadingKwh(),
                    noOfDigits,
                    "Import Final Reading KWH");

            checkDigits(
                    nonTodReading.getImportFinalSolarHourReadingKwh(),
                    noOfDigits,
                    "Import Final Solar Hour Reading KWH");

            checkDigits(
                    nonTodReading.getExportFinalReadingKwh(),
                    noOfDigits,
                    "Export Final Reading KWH");

            checkDigits(
                    nonTodReading.getExportFinalSolarReadingKwh(),
                    noOfDigits,
                    "Export Final Solar Reading KWH");

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Non TOD meter digits : "
                            + e.getMessage());
        }
    }

    private static String getLtHtCd(
            Connection con,
            String consumerNumber)
            throws Exception {

        String sql =
                "SELECT LT_HT_CD " +
                "FROM MBC_CONSUMER_MASTER " +
                "WHERE CONSUMER_NUMBER=?";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, consumerNumber);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Consumer Number "
                                    + consumerNumber
                                    + " not found.");
                }

                return rs.getString("LT_HT_CD");
            }
        }
    }

    private static int getDigitsFromWebData(
            Connection con,
            String consumerNumber)
            throws Exception {

        String sql =
                "SELECT NO_OF_DIGITS " +
                "FROM MBC_WEB_DATA " +
                "WHERE CONSUMER_NO=? " +
                "AND BILL_MTH=( " +
                "SELECT MAX(BILL_MTH) " +
                "FROM MBC_WEB_DATA " +
                "WHERE CONSUMER_NO=? )";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, consumerNumber);
            ps.setString(2, consumerNumber);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Billing data not found.");
                }

                return rs.getInt("NO_OF_DIGITS");
            }
        }
    }

    private static int getDigitsFromLtipData(
            Connection con,
            String consumerNumber)
            throws Exception {

        String sql =
                "SELECT NO_OF_DIGITS " +
                "FROM MBC_LTIP_HT_BILL_DATA " +
                "WHERE CONSUMER_NUMBER=? " +
                "AND (BILL_YEAR*100+BILL_MONTH)=( " +
                "SELECT MAX(BILL_YEAR*100+BILL_MONTH) " +
                "FROM MBC_LTIP_HT_BILL_DATA " +
                "WHERE CONSUMER_NUMBER=? )";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, consumerNumber);
            ps.setString(2, consumerNumber);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Billing data not found.");
                }

                return rs.getInt("NO_OF_DIGITS");
            }
        }
    }

    private static void checkDigits(
            Double value,
            int noOfDigits,
            String fieldName) {

        if (value == null) {
            return;
        }

        long integerPart =
                (long) Math.floor(value);

        int length =
                String.valueOf(integerPart)
                        .length();

        if (length > noOfDigits) {

            throw new IllegalArgumentException(
                    fieldName
                            + " exceeds meter digit limit. "
                            + "Allowed Digits="
                            + noOfDigits
                            + ", Provided Reading="
                            + BigDecimal.valueOf(value).toPlainString());
        }
    }
}