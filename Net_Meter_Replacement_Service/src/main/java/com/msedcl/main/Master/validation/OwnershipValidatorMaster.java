package com.msedcl.main.Master.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class OwnershipValidatorMaster {

    private OwnershipValidatorMaster() {
    }

    public static void validate(
            DataSource dataSource,
            String ownership) {

        if (ownership == null || ownership.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Ownership is required. Valid values are MSEDCL or Consumer.");
        }

        String sql =
                "SELECT OWNERSHIP_ID " +
                "FROM NC_OWNERSHIP " +
                "WHERE UPPER(TRIM(OWNERSHIP_ID)) = UPPER(TRIM(?))";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, ownership);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Ownership '" + ownership
                            + "' is not available in master. "
                            + "Valid Ownership IDs are: "
                            + "1 = MSEDCL, 2 = Consumer.");
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Ownership : "
                            + e.getMessage());
        }
    }
}