package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.msedcl.main.dto.OfficeDetailsDto;

public class OfficeDetailsLoaderBilling {

    public static void populateOfficeDetails(
            DataSource dataSource,
            String billingUnit,
            OfficeDetailsDto officeDetails) {

        String sql =
                "SELECT REGION_NAME, " +
                "ZONE_NAME, " +
                "CIRCLE_NAME, " +
                "DIVISION_NAME, " +
                "SUB_DIVISION_NAME " +
                "FROM NC_BILLING_UNIT_MASTER " +
                "WHERE BU_CODE = ?";
        
        System.out.println(sql);

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, billingUnit);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Billing Unit "
                                    + billingUnit
                                    + " not found in NC_BILLING_UNIT_MASTER");
                }

                officeDetails.setRegion(
                        rs.getString("REGION_NAME"));

                officeDetails.setZone(
                        rs.getString("ZONE_NAME"));

                officeDetails.setCircle(
                        rs.getString("CIRCLE_NAME"));

                officeDetails.setDivision(
                        rs.getString("DIVISION_NAME"));

                officeDetails.setSubdivision(
                        rs.getString("SUB_DIVISION_NAME"));
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error loading office details : "
                            + e.getMessage());
        }
    }
}