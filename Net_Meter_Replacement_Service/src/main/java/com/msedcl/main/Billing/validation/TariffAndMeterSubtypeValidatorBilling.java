package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class TariffAndMeterSubtypeValidatorBilling {

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String tariffCode,
            String meterSubtype) {

        String sql =
                "SELECT BILLING_TARIFF_CODE, METER_SUBTYPE " +
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

                String existingTariffCode =
                        rs.getString("BILLING_TARIFF_CODE");

                String existingMeterSubtype =
                        rs.getString("METER_SUBTYPE");

                // Tariff Code Validation
                if (!tariffCode.trim().equalsIgnoreCase(
                        existingTariffCode == null
                                ? ""
                                : existingTariffCode.trim())) {

                    throw new IllegalArgumentException(
                            "Tariff Code mismatch. "
                            + "Provided Tariff Code="
                            + tariffCode
                            + ", Existing Tariff Code="
                            + existingTariffCode);
                }

                // Meter Subtype Validation
				/*
				 * if (!meterSubtype.trim().equalsIgnoreCase( existingMeterSubtype == null ? ""
				 * : existingMeterSubtype.trim())) {
				 * 
				 * throw new IllegalArgumentException( "Meter Subtype mismatch. " +
				 * "Provided Meter Subtype=" + meterSubtype + ", Existing Meter Subtype=" +
				 * existingMeterSubtype); }
				 */
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Tariff Code/Meter Subtype : "
                            + e.getMessage());
        }
    }
}
