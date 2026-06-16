package com.msedcl.main.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class ConsumerExistsValidator {

    private ConsumerExistsValidator() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber) {

        String sql =
                "SELECT CONSUMER_NUMBER " +
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
                            "Consumer does not exist. "
                            + "Consumer Number = "
                            + consumerNumber);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating consumer : "
                    + e.getMessage());
        }
    }
}