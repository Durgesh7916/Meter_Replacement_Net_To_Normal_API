package com.msedcl.main.Billing.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.msedcl.main.dto.MeterReadingDetailsDto;

public class TodExportReadingValidatorBilling {

    private TodExportReadingValidatorBilling() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            MeterReadingDetailsDto exportReading) {

        String consumerSql =
                "SELECT LT_HT_CD " +
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

                String ltHtCd =
                        rs.getString("LT_HT_CD");

                if ("LTIP".equalsIgnoreCase(ltHtCd)) {

                    validateLTIPExport(
                            con,
                            consumerNumber,
                            exportReading);

                } else if ("HT".equalsIgnoreCase(ltHtCd)) {

                    validateHTExport(
                            con,
                            consumerNumber,
                            exportReading);
                }
            }

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error validating Export Reading : "
                            + e.getMessage());
        }
    }

    private static void validateLTIPExport(
            Connection con,
            String consumerNumber,
            MeterReadingDetailsDto exportReading)
            throws Exception {

        String sql =
                "SELECT " +
                "EXP_CUR_KWH_READING_TOTAL, " +
                "EXP_CUR_KWH_READING_S1, " +
                "EXP_CUR_KWH_READING_S2, " +
                "EXP_CUR_KWH_READING_S3, " +
                "EXP_CUR_KWH_READING_S4 " +
                "FROM MBC_LTIP_HT_BILL_DATA " +
                "WHERE CONSUMER_NUMBER = ? " +
                "AND (BILL_YEAR * 100 + BILL_MONTH) = ( " +
                "      SELECT MAX(BILL_YEAR * 100 + BILL_MONTH) " +
                "      FROM MBC_LTIP_HT_BILL_DATA " +
                "      WHERE CONSUMER_NUMBER = ? " +
                ")";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, consumerNumber);
            ps.setString(2, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "LTIP billing data not found.");
                }

                validateReading(
                        exportReading.getHeaderReading().getKwh(),
                        rs.getDouble("EXP_CUR_KWH_READING_TOTAL"),
                        "Export Header KWH");

                validateReading(
                        exportReading.getSlotReading01().getKwh(),
                        rs.getDouble("EXP_CUR_KWH_READING_S1"),
                        "Export KWH Slot-1");

                validateReading(
                        exportReading.getSlotReading02().getKwh(),
                        rs.getDouble("EXP_CUR_KWH_READING_S2"),
                        "Export KWH Slot-2");

                validateReading(
                        exportReading.getSlotReading03().getKwh(),
                        rs.getDouble("EXP_CUR_KWH_READING_S3"),
                        "Export KWH Slot-3");

                validateReading(
                        exportReading.getSlotReading04().getKwh(),
                        rs.getDouble("EXP_CUR_KWH_READING_S4"),
                        "Export KWH Slot-4");
            }
        }
    }

    private static void validateHTExport(
            Connection con,
            String consumerNumber,
            MeterReadingDetailsDto exportReading)
            throws Exception {

        String sql =
                "SELECT * " +
                "FROM DW_HT_METER_CONSUMPTION " +
                "WHERE CONSUMER_NUMBER = ? " +
                "AND METER_TYPE_CODE = '91' " +
                "AND (BILL_YEAR * 100 + BILL_MONTH) = ( " +
                "      SELECT MAX(BILL_YEAR * 100 + BILL_MONTH) " +
                "      FROM DW_HT_METER_CONSUMPTION " +
                "      WHERE CONSUMER_NUMBER = ? " +
                "      AND METER_TYPE_CODE = '91' " +
                ")";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, consumerNumber);
            ps.setString(2, consumerNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "HT Export Meter "
                                    + "(METER_TYPE_CODE=91) "
                                    + "not found.");
                }

                // Header

                validateReading(
                        exportReading.getHeaderReading().getKwh(),
                        rs.getDouble("KWC_UNIT_KWH"),
                        "Export Header KWH");

                validateReading(
                        exportReading.getHeaderReading().getKvah(),
                        rs.getDouble("KVAC_UNIT_KVAH"),
                        "Export Header KVAH");

                validateReading(
                        exportReading.getHeaderReading().getRkvahLag(),
                        rs.getDouble("RKVAC_UNIT_RKVAH"),
                        "Export Header RKVAH Lag");

                validateReading(
                        exportReading.getHeaderReading().getRkvahLead(),
                        rs.getDouble("RKVAC_UNIT_RKVAH_LEAD"),
                        "Export Header RKVAH Lead");

                // Slot 1

                validateSlot(
                        exportReading.getSlotReading01(),
                        rs,
                        1);

                // Slot 2

                validateSlot(
                        exportReading.getSlotReading02(),
                        rs,
                        2);

                // Slot 3

                validateSlot(
                        exportReading.getSlotReading03(),
                        rs,
                        3);

                // Slot 4

                validateSlot(
                        exportReading.getSlotReading04(),
                        rs,
                        4);
            }
        }
    }

    private static void validateSlot(
            com.msedcl.main.dto.ReadingDto slot,
            ResultSet rs,
            int slotNo)
            throws Exception {

        validateReading(
                slot.getKwh(),
                rs.getDouble("KWH_READING" + slotNo),
                "Export KWH Slot-" + slotNo);

        validateReading(
                slot.getKvah(),
                rs.getDouble("KVAH_READING" + slotNo),
                "Export KVAH Slot-" + slotNo);

        validateReading(
                slot.getRkvahLag(),
                rs.getDouble("RKVAH_READING" + slotNo),
                "Export RKVAH Lag Slot-" + slotNo);

        validateReading(
                slot.getRkvahLead(),
                rs.getDouble(
                        "RKVAH_LEAD_READINGS_S" + slotNo),
                "Export RKVAH Lead Slot-" + slotNo);
    }

    private static void validateReading(
            Double currentReading,
            Double previousReading,
            String fieldName) {

        if (currentReading < previousReading) {

            throw new IllegalArgumentException(
                    fieldName
                            + " Current Reading="
                            + currentReading
                            + " is less than Previous Reading="
                            + previousReading);
        }
    }
}