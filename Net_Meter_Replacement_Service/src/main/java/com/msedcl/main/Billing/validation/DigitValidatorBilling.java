package com.msedcl.main.Billing.validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.msedcl.main.dto.MeterReadingDetailsDto;
import com.msedcl.main.dto.ReadingDto;

public class DigitValidatorBilling {

    private DigitValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            MeterReadingDetailsDto importReading,
            MeterReadingDetailsDto exportReading) {

        try (Connection con = dataSource.getConnection()) {

            String ltHtCd = getLtHtCd(
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

            validateHeader(
                    importReading.getHeaderReading(),
                    noOfDigits,
                    "Import");

            validateHeader(
                    exportReading.getHeaderReading(),
                    noOfDigits,
                    "Export");

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating meter digits : "
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

    private static void validateHeader(
            ReadingDto reading,
            int noOfDigits,
            String type) {

        checkDigits(
                reading.getKwh(),
                noOfDigits,
                type + " Header KWH");

        checkDigits(
                reading.getKvah(),
                noOfDigits,
                type + " Header KVAH");

        checkDigits(
                reading.getRkvahLag(),
                noOfDigits,
                type + " Header RKVAH Lag");

        checkDigits(
                reading.getRkvahLead(),
                noOfDigits,
                type + " Header RKVAH Lead");
        checkDigits(
                reading.getKw(),
                noOfDigits,
                type + " Header KW");

        checkDigits(
                reading.getKva(),
                noOfDigits,
                type + " Header KVA");
    }

    private static void checkDigits(
            Double value,
            int noOfDigits,
            String fieldName) {

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