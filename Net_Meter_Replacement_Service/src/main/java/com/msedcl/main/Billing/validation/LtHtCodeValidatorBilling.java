package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class LtHtCodeValidatorBilling {

    private LtHtCodeValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String providedLtHtCode) {

        String sql =
                "SELECT LT_HT_CD " +
                "FROM MBC_CONSUMER_MASTER " +
                "WHERE CONSUMER_NUMBER = ?";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Consumer Number "
                                    + consumerNumber
                                    + " not found.");
                }

                String existingLtHtCode =
                        rs.getString("LT_HT_CD");

                if (existingLtHtCode != null
                        && !existingLtHtCode.trim()
                                .equalsIgnoreCase(
                                        providedLtHtCode.trim())) {

                    throw new IllegalArgumentException(
                            "LT/HT Code mismatch. "
                                    + "Provided LT_HT_CD="
                                    + providedLtHtCode
                                    + ", Existing LT_HT_CD="
                                    + existingLtHtCode);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating LT/HT Code : "
                            + e.getMessage());
        }
    }
}