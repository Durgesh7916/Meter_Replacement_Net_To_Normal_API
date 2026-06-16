package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class SectionValidatorBilling {

    private SectionValidatorBilling() {
    }

    public static String validateAndGetSection(
            DataSource dataSource,
            String consumerNumber) {

        String consumerSql =
                "SELECT LPAD(SECTION_CODE,3,'0') SECTION_CODE, BU " +
                "FROM MBC_CONSUMER_MASTER " +
                "WHERE CONSUMER_NUMBER = ?";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(consumerSql)
        ) {

            ps.setString(1, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Consumer Number "
                                    + consumerNumber
                                    + " not found.");
                }

                String consumerSection =
                        rs.getString("SECTION_CODE");

                String bu =
                        rs.getString("BU");

                return getSectionId(
                        con,
                        consumerSection,
                        bu);
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Section : "
                            + e.getMessage());
        }
    }

    private static String getSectionId(
            Connection con,
            String consumerSection,
            String bu)
            throws Exception {

        String sql =
                "SELECT LPAD(SECTION_CD_C,3,'0') SECTION_CD_C, " +
                "SECTION_ID_N " +
                "FROM MBC_SECTION_LOCATION " +
                "WHERE BU_CODE = ? " +
                "AND STATUS_CD_C = 'A'";

        try (
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, bu);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String masterSection =
                            rs.getString("SECTION_CD_C");

                    if (consumerSection.equals(masterSection)) {

                        return rs.getString("SECTION_ID_N");
                    }
                }
            }
        }

        throw new IllegalArgumentException(
                "Section mapping not found. "
                        + "Consumer Section="
                        + consumerSection
                        + ", BU="
                        + bu);
    }
}