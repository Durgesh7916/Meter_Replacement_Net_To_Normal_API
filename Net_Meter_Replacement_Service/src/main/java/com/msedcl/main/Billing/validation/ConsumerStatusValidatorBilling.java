package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class ConsumerStatusValidatorBilling {

    private ConsumerStatusValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber) {

        String sql =
                "SELECT CONSUMER_STATUS " +
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

                String status =
                        rs.getString("CONSUMER_STATUS");

                if (status == null) {

                    throw new IllegalArgumentException(
                            "Consumer Status not available for Consumer Number "
                            + consumerNumber);
                }

                status = status.trim();

                if (!status.equalsIgnoreCase("0")
                        && !status.equalsIgnoreCase("1")
                        && !status.equalsIgnoreCase("C")
                        && !status.equalsIgnoreCase("T")) {

                    throw new IllegalArgumentException(
                            "Consumer is PD. Not Eligible For Replacement. "
                            + "Consumer Status = " + status);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Consumer Status : "
                    + e.getMessage());
        }
    }
}