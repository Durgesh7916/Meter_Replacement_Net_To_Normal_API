package com.msedcl.main.Master.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class MeterSubTypeValidatorMaster {

    private MeterSubTypeValidatorMaster() {
    }

    public static void validate(
            DataSource dataSource,
            String meterSubType) {

        String sql =
                "SELECT METER_SUB_TYPE_CD_C " +
                "FROM NC_METER_SUB_TYPE " +
                "WHERE UPPER(METER_SUB_TYPE_CD_C) = UPPER(?) " +
                "AND STATUS_CD_C = 'A'";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, meterSubType);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Meter Sub Type '"
                                    + meterSubType
                                    + "' not found in master.");
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Meter Sub Type : "
                            + e.getMessage());
        }
    }
}