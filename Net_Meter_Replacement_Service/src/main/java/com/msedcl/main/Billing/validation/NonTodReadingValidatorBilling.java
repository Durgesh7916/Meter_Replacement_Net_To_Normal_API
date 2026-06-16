package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class NonTodReadingValidatorBilling {

    private NonTodReadingValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            Double importFinalReadingKwh,
            Double exportFinalReadingKwh) {

        String sql =
                "SELECT IMP_CUR_KWH_READING_TOTAL, " +
                "EXP_CUR_KWH_READING_TOTAL " +
                "FROM MBC_WEB_DATA " +
                "WHERE CONSUMER_NO = ? " +
                "AND BILL_MTH = ( " +
                "      SELECT MAX(BILL_MTH) " +
                "      FROM MBC_WEB_DATA " +
                "      WHERE CONSUMER_NO = ? " +
                " )";

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

                Double previousImportReading =
                        rs.getDouble("IMP_CUR_KWH_READING_TOTAL");

                Double previousExportReading =
                        rs.getDouble("EXP_CUR_KWH_READING_TOTAL");

                if (importFinalReadingKwh < previousImportReading) {

                    throw new IllegalArgumentException(
                            "Current Import Reading "
                                    + importFinalReadingKwh
                                    + " is less than Previous Import Reading "
                                    + previousImportReading
                                    + ". Please provide valid current Import Reading.");
                }

                if (exportFinalReadingKwh < previousExportReading) {

                    throw new IllegalArgumentException(
                            "Current Export Reading "
                                    + exportFinalReadingKwh
                                    + " is less than Previous Export Reading "
                                    + previousExportReading
                                    + ". Please provide valid current Export Reading.");
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Non-TOD readings : "
                            + e.getMessage());
        }
    }
}