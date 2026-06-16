package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.sql.DataSource;

public class ReplacementDateValidatorBilling {

    private ReplacementDateValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String installationType,
            LocalDate replacementDate) {

        if ("11".equals(installationType)) {

            validateNonTod(
                    dataSource,
                    consumerNumber,
                    replacementDate);

        } else if ("71".equals(installationType)) {

            validateTod(
                    dataSource,
                    consumerNumber,
                    replacementDate);
        }
    }

    private static void validateNonTod(
            DataSource dataSource,
            String consumerNumber,
            LocalDate replacementDate) {

        String sql =
                "SELECT BILL_DT_TO " +
                "FROM MBC_WEB_DATA " +
                "WHERE CONSUMER_NO = ? " +
                "AND BILL_MTH = ( " +
                "    SELECT MAX(BILL_MTH) " +
                "    FROM MBC_WEB_DATA " +
                "    WHERE CONSUMER_NO= ? " +
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

                Date billDate = rs.getDate("BILL_DT_TO");

                if (billDate == null) {

                    throw new IllegalArgumentException(
                            "Last billed reading date not found.");
                }

                LocalDate lastBillDate =
                        billDate.toLocalDate();

                if (!replacementDate.isAfter(lastBillDate)) {

                    throw new IllegalArgumentException(
                            "Replacement Date "
                                    + replacementDate
                                    + " should be greater than Last Billed Reading Date "
                                    + lastBillDate);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Replacement Date : "
                            + e.getMessage());
        }
    }

    private static void validateTod(
            DataSource dataSource,
            String consumerNumber,
            LocalDate replacementDate) {

        String sql =
                "SELECT READING_DATE " +
                "FROM MBC_LTIP_HT_BILL_DATA " +
                "WHERE CONSUMER_NUMBER = ? " +
                "AND ( BILL_YEAR*100+BILL_MONTH) = ( " +
                "      SELECT MAX(BILL_YEAR*100+BILL_MONTH) " +
                "      FROM MBC_LTIP_HT_BILL_DATA " +
                "      WHERE CONSUMER_NUMBER = ? " +
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

                Date readingDate =
                        rs.getDate("READING_DATE");

                if (readingDate == null) {

                    throw new IllegalArgumentException(
                            "Last billed reading date not found.");
                }

                LocalDate lastReadingDate =
                        readingDate.toLocalDate();

                if (!replacementDate.isAfter(lastReadingDate)) {

                    throw new IllegalArgumentException(
                            "Replacement Date "
                                    + replacementDate
                                    + " should be greater than Last Billed Reading Date "
                                    + lastReadingDate);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Replacement Date : "
                            + e.getMessage());
        }
    }
}