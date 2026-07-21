package com.msedcl.main.Master.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class ReplacementReasonValidatorMaster {

    private ReplacementReasonValidatorMaster() {
    }

    public static void validate(
            DataSource dataSource,
            String replacementReason) {

        String sql =
                "SELECT REPLACMENT_REASON_CD_C " +
                "FROM NC_METER_REPLACEMENT_REASON " +
                "WHERE UPPER(REPLACMENT_REASON_CD_C) = UPPER(?) " +
                "AND STATUS_CD_C = 'A' AND UPPER(REPLACMENT_REASON_CD_C)='18' ";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, replacementReason);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Replacement Reason '"
                                    + replacementReason
                                    + "' not found in master. OR Only Allowed Changed to Smart Meter(18)");
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Replacement Reason : "
                            + e.getMessage());
        }
    }
}