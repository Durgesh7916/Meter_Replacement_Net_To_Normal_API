package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class SmartMeterValidatorBilling {

    private SmartMeterValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber) {

        String sql =
                "SELECT METER_SUBTYPE " +
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

                String meterSubtype =
                        rs.getString("METER_SUBTYPE");

                if (meterSubtype == null
                        || meterSubtype.trim().isEmpty()) {

                    throw new IllegalArgumentException(
                            "Consumer have no Smart Meter Flag.");
                }

                meterSubtype = meterSubtype.trim().toUpperCase();

                if (!"SMARTCEL".equals(meterSubtype)
                        && !"SMARTRF".equals(meterSubtype)&& !"SMARTCELL".equals(meterSubtype)) {

                    throw new IllegalArgumentException(
                            "Please Provide Valid Smart Meter Consumer. "
                            + "Consumer have no Smart Meter Flag. "
                            + "Existing Meter Subtype = "
                            + meterSubtype);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Smart Meter Consumer : "
                            + e.getMessage());
        }
    }
}


