package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class SolarRooftopValidatorBilling {

    private SolarRooftopValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber) {

        String sql =
                "SELECT SOLAR_ROOFTOP_FLAG " +
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

                String solarFlag =
                        rs.getString("SOLAR_ROOFTOP_FLAG");

                if (solarFlag == null
                        || !solarFlag.trim().equalsIgnoreCase("Y")) {

                    throw new IllegalArgumentException(
                            "Consumer is not a valid Solar Rooftop Consumer. "
                            + "Please provide a valid Solar Rooftop Consumer.");
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Solar Rooftop Consumer : "
                    + e.getMessage());
        }
    }
}