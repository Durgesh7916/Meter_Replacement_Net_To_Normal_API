package com.msedcl.main.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class ApplicationAlreadyExistWithOldMeterNumberValidator {

    private ApplicationAlreadyExistWithOldMeterNumberValidator() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String oldMeterNumber) {

    	String sql =
    	        "SELECT A.APPLICATION_ID_N, "
    	      + "A.ASSIGNED_CONSUMER_NO_C, "
    	      + "A.CURRENT_WORKFLOW_STATUS_C, "
    	      + "B.OLD_METRE_SR_NUMBER, "
    	      + "B.OLD_METER_METER_CODE "
    	      + "FROM NC_METER_REPLACE_APPLICATION A "
    	      + "JOIN NC_OLD_METER_READING B "
    	      + "ON A.APPLICATION_ID_N = B.APPLICATION_ID_N "
    	      + "WHERE A.ASSIGNED_CONSUMER_NO_C = ? "
    	      + "AND B.OLD_METRE_SR_NUMBER = ? "
    	      + "AND A.APPLN_SERVICE_TYPE_ID_N = '32' "
    	      + "AND A.STATUS_CD_C = 'A' "
    	      + "AND A.CURRENT_WF_STATUS_ID_N IN (31,33)";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

        	ps.setString(1, consumerNumber);
        	ps.setString(2, oldMeterNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Long applicationId =
                            rs.getLong("APPLICATION_ID_N");

                    throw new IllegalArgumentException(
                            "Meter Replacement Application Already Exist With Status. "
                            + "Application Id = "
                            + applicationId
                            + ", Current Status = "
                            + rs.getString("CURRENT_WORKFLOW_STATUS_C")
                            + ", Old Meter SR No = "
                            + rs.getString("OLD_METRE_SR_NUMBER")
                            + ", Old Meter Make Code = "
                            + rs.getString("OLD_METER_METER_CODE"));
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
