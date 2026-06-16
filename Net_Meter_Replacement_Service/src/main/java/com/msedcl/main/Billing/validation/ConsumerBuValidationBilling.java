package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBuValidationBilling {

    @Autowired
    private DataSource dataSource;

    public void validateConsumerAndBillingUnit(
            String consumerNumber,
            String billingUnit) {

        String sql =
                "SELECT BU " +
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
                                    + " not found in MBC_CONSUMER_MASTER");
                }

                String existingBU =
                        rs.getString("BU");

                if (!billingUnit.trim()
                        .equalsIgnoreCase(existingBU.trim())) {

                    throw new IllegalArgumentException(
                            "Billing Unit mismatch. "
                                    + "Provided BU="
                                    + billingUnit
                                    + ", Existing BU="
                                    + existingBU);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Consumer/Billing Unit : "
                            + e.getMessage());
        }
    }
}