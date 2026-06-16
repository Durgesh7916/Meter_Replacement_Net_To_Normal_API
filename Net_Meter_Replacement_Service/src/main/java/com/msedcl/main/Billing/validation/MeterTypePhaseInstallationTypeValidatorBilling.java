package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class MeterTypePhaseInstallationTypeValidatorBilling {

    private MeterTypePhaseInstallationTypeValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String meterType,
            String installationType) {

        String sql =
                "SELECT METER_PHASE, METER_TYPE_CODE " +
                "FROM MBC_CONSUMER_MASTER " +
                "WHERE CONSUMER_NUMBER = ?";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Consumer Number "
                                    + consumerNumber
                                    + " not found.");
                }

                String existingMeterPhase =
                        rs.getString("METER_PHASE");

                String existingMeterTypeCode =
                        rs.getString("METER_TYPE_CODE");

                /*
                 * Meter Type Validation
                 *
                 * JSON Value -> Billing Value
                 *
                 * 15 -> 1
                 * 16 -> 3
                 * 7  -> TOD
                 */
                validateMeterPhase(
                        meterType,
                        existingMeterPhase);

                /*
                 * Installation Type Validation
                 * Existing logic unchanged
                 */
                if (existingMeterTypeCode != null
                        && !existingMeterTypeCode.trim()
                                .equalsIgnoreCase(
                                        installationType.trim())) {

                    throw new IllegalArgumentException(
                            "Installation Type mismatch. "
                                    + "Provided Installation Type="
                                    + installationType
                                    + ", Existing Meter Type Code="
                                    + existingMeterTypeCode);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating consumer meter details : "
                            + e.getMessage());
        }
    }

    private static void validateMeterPhase(
            String meterType,
            String existingMeterPhase) {

        String expectedBillingPhase;

        if ("15".equalsIgnoreCase(meterType)) {

            expectedBillingPhase = "1";

        } else if ("16".equalsIgnoreCase(meterType)) {

            expectedBillingPhase = "3";

        } else if ("7".equalsIgnoreCase(meterType)) {

            expectedBillingPhase = "TOD";

        } else {

            throw new IllegalArgumentException(
                    "Invalid Meter Type '"
                            + meterType
                            + "'. Valid values are "
                            + "15 (1PH), "
                            + "16 (3PH), "
                            + "7 (TOD).");
        }

        if (existingMeterPhase == null
                || !expectedBillingPhase.equalsIgnoreCase(
                        existingMeterPhase.trim())) {

            throw new IllegalArgumentException(
                    "Meter Type mismatch. "
                            + "Provided Meter Type="
                            + meterType
                            + ", Existing Meter Phase="
                            + existingMeterPhase + " '. Valid values are "
                            + "15 (1PH), "
                            + "16 (3PH), "
                            + "7 (TOD).");
        }
    }
}