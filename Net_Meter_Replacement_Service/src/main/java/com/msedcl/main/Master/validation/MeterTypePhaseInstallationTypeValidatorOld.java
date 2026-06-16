package com.msedcl.main.Master.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class MeterTypePhaseInstallationTypeValidatorOld {

    private MeterTypePhaseInstallationTypeValidatorOld() {
    }

    public static void validate(
            DataSource dataSource,
            String consumerNumber,
            String meterType,
            String installationType) {

        validateMeterTypeMaster(
                dataSource,
                meterType);

        validateInstallationTypeMaster(
                dataSource,
                installationType);

        validateConsumerValues(
                dataSource,
                consumerNumber,
                meterType,
                installationType);
    }

    private static void validateMeterTypeMaster(
            DataSource dataSource,
            String meterType) {

        String sql =
                "SELECT METER_PHASE_ID " +
                "FROM NC_METER_PHASE_TYPE " +
                "WHERE METER_PHASE_ID = ?";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, meterType);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Meter Type '" + meterType
                                    + "' not found in master. "
                                    + "Valid values are : "
                                    + "15 = 1PH, "
                                    + "16 = 3PH, "
                                    + "7 = TOD METER");
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error validating Meter Type : "
                            + e.getMessage());
        }
    }

    private static void validateInstallationTypeMaster(
            DataSource dataSource,
            String installationType) {

        String sql =
                "SELECT INSTALLATION_TYPE_CD_C " +
                "FROM NC_INSTALLATION_TYPE " +
                "WHERE INSTALLATION_TYPE_CD_C = ?";

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, installationType);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    throw new IllegalArgumentException(
                            "Installation Type '" + installationType
                                    + "' not found in master. "
                                    + "Valid values are : "
                                    + "11 = NON TOD, "
                                    + "71 = TOD");
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error validating Installation Type : "
                            + e.getMessage());
        }
    }

    private static void validateConsumerValues(
            DataSource dataSource,
            String consumerNumber,
            String meterType,
            String installationType) {

        String sql =
                "SELECT METER_PHASE, METER_TYPE_CODE " +
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

                String existingMeterPhase =
                        rs.getString("METER_PHASE");

                String existingMeterTypeCode =
                        rs.getString("METER_TYPE_CODE");

                if (existingMeterPhase != null
                        && !existingMeterPhase.trim()
                                .equalsIgnoreCase(
                                        meterType.trim())) {

                    throw new IllegalArgumentException(
                            "Meter Type mismatch. "
                                    + "Provided Meter Type="
                                    + meterType
                                    + ", Existing Meter Phase="
                                    + existingMeterPhase);
                }

                if (existingMeterTypeCode != null
                        && !existingMeterTypeCode.trim()
                                .equalsIgnoreCase(
                                        installationType.trim())) {

                    throw new IllegalArgumentException(
                            "Installation Type mismatch. "
                                    + "Provided Installation Type="
                                    + installationType
                                    + ", Existing Meter Type Code="
                                    + existingMeterTypeCode);
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error validating Meter Type / Installation Type : "
                            + e.getMessage());
        }
    }
}