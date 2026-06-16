package com.msedcl.main.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import com.msedcl.main.dto.ApplicationStatusResponse;

@Service
public class ApplicationStatusService {

    @Autowired
    private DataSource dataSource;

    public ApplicationStatusResponse getApplicationStatus(
            Long applicationId) {

    	String sql =
    	        "SELECT APPLICATION_ID_N, "
    	      + "ASSIGNED_CONSUMER_NO_C, "
    	      + "APPLN_SERVICE_TYPE_ID_N, "
    	      + "CURRENT_WORKFLOW_STATUS_C "
    	      + "FROM NC_METER_REPLACE_APPLICATION "
    	      + "WHERE APPLICATION_ID_N = ? "
    	      + "AND APPLN_SERVICE_TYPE_ID_N IN ('032','32')";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setLong(1, applicationId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    ApplicationStatusResponse response =
                            new ApplicationStatusResponse();

                    response.setApplicationId(
                            rs.getLong("APPLICATION_ID_N"));

                    response.setConsumerNumber(
                            rs.getString(
                                    "ASSIGNED_CONSUMER_NO_C"));

                    response.setApplicationServiceTypeId(rs.getString("APPLN_SERVICE_TYPE_ID_N"));

                    response.setWorkflowStatus(
                            rs.getString(
                                    "CURRENT_WORKFLOW_STATUS_C"));

                    return response;
                }

                throw new IllegalArgumentException(
                        "Application Id not found : "
                        + applicationId);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error fetching application status"+e.getMessage(),
                    e);
        }
    }
}