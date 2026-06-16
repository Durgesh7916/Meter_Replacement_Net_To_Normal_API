package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class MeterValidationBilling {

    public static void validateMeterDetails(
            DataSource dataSource,
            String consumerNumber,
            String makeCode,
            String serialNumber) {

        String sql =
                "SELECT METER_CODE, METER_SR_NO_C " +
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

                String existingMakeCode =
                        rs.getString("METER_CODE");

                String existingSerialNumber =
                        rs.getString("METER_SR_NO_C");

                boolean makeMatch =
                        makeCode != null
                        && makeCode.trim().equalsIgnoreCase(
                                existingMakeCode == null
                                        ? ""
                                        : existingMakeCode.trim());

                boolean serialMatch =
                        serialNumber != null
                        && serialNumber.trim().equalsIgnoreCase(
                                existingSerialNumber == null
                                        ? ""
                                        : existingSerialNumber.trim());

                if (!(makeMatch && serialMatch)) {

                    throw new IllegalArgumentException(
                            "Meter details mismatch. "
                            + "Provided Make Code="
                            + makeCode
                            + ", Serial Number="
                            + serialNumber
                            + ". Existing Make Code="
                            + existingMakeCode
                            + ", Serial Number="
                            + existingSerialNumber);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating meter details : "
                            + e.getMessage());
        }
    }
}