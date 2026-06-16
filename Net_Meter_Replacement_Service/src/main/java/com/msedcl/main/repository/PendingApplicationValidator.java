package com.msedcl.main.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class PendingApplicationValidator {

    private PendingApplicationValidator() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber) {

        String sql =
                "SELECT APPLICATION_ID_N ,CURRENT_WORKFLOW_STATUS_C " +
                "FROM NC_METER_REPLACE_APPLICATION " +
                "WHERE ASSIGNED_CONSUMER_NO_C = ? " +
                "AND APPLN_SERVICE_TYPE_ID_N = 32 " +
                "AND STATUS_CD_C = 'A' " +
                "AND CURRENT_WF_STATUS_ID_N IN (31)";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Long applicationId =
                            rs.getLong("APPLICATION_ID_N");

                    throw new IllegalArgumentException(
                            "Meter Replacement Application Status. "
                            + "Application Id = "
                            + applicationId
                            + ",Current_Status = "
                            + rs.getString("CURRENT_WORKFLOW_STATUS_C"));
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating pending application : "
                    + e.getMessage());
        }
    }
}