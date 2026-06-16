package com.msedcl.main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.msedcl.main.dto.MeterReplacementRequestDto;
import com.msedcl.main.dto.ReadingDto;

public class OldMeterReadingDao {

    private OldMeterReadingDao() {
    }

    public static void insert(
            Connection conn,
            Long applicationId,
            String oldMeterNo,
            MeterReplacementRequestDto request)
            throws Exception {

        String installationType =
                request.getConsumerDetails()
                       .getInstallationType();

        if ("11".equals(installationType)) {

            insertNonTod(
                    conn,
                    applicationId,
                    oldMeterNo,
                    request);

        } else if ("71".equals(installationType)) {

            insertTod(
                    conn,
                    applicationId,
                    oldMeterNo,
                    request);
        }
        
    }
        private static void insertNonTod(
                Connection conn,
                Long applicationId,
                String oldMeterNo,
                MeterReplacementRequestDto request)
                throws Exception {

            String sql =
                    "INSERT INTO NC_OLD_METER_READING ("
                    + "METER_READING_ID_N,"
                    + "APPLICATION_ID_N,"
                    + "METER_READING_DT,"
                    + "CONSUMER_NUMBER_C,"
                    + "OLD_METRE_SR_NUMBER,"
                    + "HEADER_KWH_N,"
                    + "HEADER_SOLAR_HOUR_KWH_N,"
                    + "HEADER_EXPORT_KWH_N,"
                    + "HEADER_EXPORT_SOLAR_HOUR_KWH_N,"
                    + "METER_INSTALLATION_TYPE_CD,"
                    + "CREATED_BY_C,"
                    + "METER_TYPE_ID_N,"
            		+ "METER_OWNERSHIP_CD,"
            		+ "METER_GUARANTEE_CD,"
            		+ "METER_GUARANTEE_DT,"
            		+ "METER_REPLACEMENT_CD,"
            		+ "REUSABEL_METER_CD,"
            		+ "METER_SUB_TYPE_CD_C,"
            		+ "AMISP_OLD_MTR_RDNG_USING_OCR_FLAG,"
                    + "CREATED_DT,"+ "OLD_METER_METER_CODE,"
                    + "UPDATED_BY_C,"
                    + "UPDATED_DT,"
                    + "METER_ASSESSMENT_CD"
                    + ") VALUES ("
                    + "SEQ_NC_METER_READING_ID.NEXTVAL,"
                    + "?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,SYSDATE,?,?,SYSDATE,?)";

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                int i = 1;

                ps.setLong(i++, applicationId);

                ps.setDate(
                        i++,
                        Date.valueOf(
                                LocalDate.parse(
                                        request.getConsumerDetails()
                                               .getReplacementDate(),
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy"))));

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getConsumerNumber());

                ps.setString(
                        i++,
                        oldMeterNo);

                ps.setDouble(
                        i++,
                        request.getNonTodReading()
                               .getImportFinalReadingKwh());

                ps.setDouble(
                        i++,
                        request.getNonTodReading()
                               .getImportFinalSolarHourReadingKwh());

                ps.setDouble(
                        i++,
                        request.getNonTodReading()
                               .getExportFinalReadingKwh());

                ps.setDouble(
                        i++,
                        request.getNonTodReading()
                               .getExportFinalSolarReadingKwh());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getInstallationType());

                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getUpdatedBy());
                
                /***addition field*********/
                
                ps.setLong(
                        i++,
                        Long.parseLong(
                                request.getConsumerDetails()
                                       .getMeterTypePhaseId()));

              

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getOwnership());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMeterWithinGuarantee());

                ps.setDate(
                        i++,
                        Date.valueOf(
                                LocalDate.parse(
                                        request.getConsumerDetails()
                                               .getGuaranteeEndDate(),
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy"))));

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getReplacementReason());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getReusableMeter());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMeterSubType());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getAmispOldMtrRdngUsingOcrFlagYn());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMakeCode());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getUpdatedBy());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getAssessmentRequired());
                /***********end*********/
                
               
                ps.executeUpdate();
            }
        }
        
        
        
        
        private static void insertTod(
                Connection conn,
                Long applicationId,
                String oldMeterNo,
                MeterReplacementRequestDto request)
                throws Exception {

            ReadingDto impHdr =
                    request.getImportReading()
                           .getHeaderReading();

            ReadingDto i1 =
                    request.getImportReading()
                           .getSlotReading01();

            ReadingDto i2 =
                    request.getImportReading()
                           .getSlotReading02();

            ReadingDto i3 =
                    request.getImportReading()
                           .getSlotReading03();

            ReadingDto i4 =
                    request.getImportReading()
                           .getSlotReading04();

            ReadingDto expHdr =
                    request.getExportReading()
                           .getHeaderReading();

            ReadingDto e1 =
                    request.getExportReading()
                           .getSlotReading01();

            ReadingDto e2 =
                    request.getExportReading()
                           .getSlotReading02();

            ReadingDto e3 =
                    request.getExportReading()
                           .getSlotReading03();

            ReadingDto e4 =
                    request.getExportReading()
                           .getSlotReading04();

            String sql =
                    "INSERT INTO NC_OLD_METER_READING ("
                    + "METER_READING_ID_N,"
                    + "APPLICATION_ID_N,"
                    + "METER_READING_DT,"
                    + "CONSUMER_NUMBER_C,"
                    + "OLD_METRE_SR_NUMBER,"

                    + "HEADER_KW_N,"
                    + "HEADER_KWH_N,"
                    + "HEADER_KVA_N,"
                    + "HEADER_KVAH_N,"
                    + "HEADER_RKVAH_N,"
                    + "HEADER_RKVAH_LEAD_N,"

                    + "SLOT1_KW_N,"
                    + "SLOT1_KWH_N,"
                    + "SLOT1_KVA_N,"
                    + "SLOT1_KVAH_N,"
                    + "SLOT1_RKVAH_N,"
                    + "SLOT1_RKVAH_LEAD_N,"

                    + "SLOT2_KW_N,"
                    + "SLOT2_KWH_N,"
                    + "SLOT2_KVA_N,"
                    + "SLOT2_KVAH_N,"
                    + "SLOT2_RKVAH_N,"
                    + "SLOT2_RKVAH_LEAD_N,"

                    + "SLOT3_KW_N,"
                    + "SLOT3_KWH_N,"
                    + "SLOT3_KVA_N,"
                    + "SLOT3_KVAH_N,"
                    + "SLOT3_RKVAH_N,"
                    + "SLOT3_RKVAH_LEAD_N,"

                    + "SLOT4_KW_N,"
                    + "SLOT4_KWH_N,"
                    + "SLOT4_KVA_N,"
                    + "SLOT4_KVAH_N,"
                    + "SLOT4_RKVAH_N,"
                    + "SLOT4_RKVAH_LEAD_N,"

                    + "HEADER_EXPORT_KW_N,"
                    + "HEADER_EXPORT_KWH_N,"
                    + "HEADER_EXPORT_KVA_N,"
                    + "HEADER_EXPORT_KVAH_N,"
                    + "HEADER_EXPORT_RKVAH_N,"
                    + "HEADER_EXPORT_RKVAH_LEAD_N,"

                    + "SLOT1_EXPORT_KW_N,"
                    + "SLOT1_EXPORT_KWH_N,"
                    + "SLOT1_EXPORT_KVA_N,"
                    + "SLOT1_EXPORT_KVAH_N,"
                    + "SLOT1_EXPORT_RKVAH_N,"
                    + "SLOT1_EXPORT_RKVAH_LEAD_N,"

                    + "SLOT2_EXPORT_KW_N,"
                    + "SLOT2_EXPORT_KWH_N,"
                    + "SLOT2_EXPORT_KVA_N,"
                    + "SLOT2_EXPORT_KVAH_N,"
                    + "SLOT2_EXPORT_RKVAH_N,"
                    + "SLOT2_EXPORT_RKVAH_LEAD_N,"

                    + "SLOT3_EXPORT_KW_N,"
                    + "SLOT3_EXPORT_KWH_N,"
                    + "SLOT3_EXPORT_KVA_N,"
                    + "SLOT3_EXPORT_KVAH_N,"
                    + "SLOT3_EXPORT_RKVAH_N,"
                    + "SLOT3_EXPORT_RKVAH_LEAD_N,"

                    + "SLOT4_EXPORT_KW_N,"
                    + "SLOT4_EXPORT_KWH_N,"
                    + "SLOT4_EXPORT_KVA_N,"
                    + "SLOT4_EXPORT_KVAH_N,"
                    + "SLOT4_EXPORT_RKVAH_N,"
                    + "SLOT4_EXPORT_RKVAH_LEAD_N,"

                    + "METER_INSTALLATION_TYPE_CD,"
                    + "CREATED_BY_C,"+ "METER_TYPE_ID_N,"
            		+ "METER_OWNERSHIP_CD,"
            		+ "METER_GUARANTEE_CD,"
            		+ "METER_GUARANTEE_DT,"
            		+ "METER_REPLACEMENT_CD,"
            		+ "REUSABEL_METER_CD,"
            		+ "METER_SUB_TYPE_CD_C,"
            		+ "AMISP_OLD_MTR_RDNG_USING_OCR_FLAG,"
                    + "CREATED_DT,"+ "OLD_METER_METER_CODE,"
                    + "UPDATED_BY_C,"
                    + "UPDATED_DT,"
                    + "METER_ASSESSMENT_CD"
                    

                    + ") VALUES ("

                    + "SEQ_NC_METER_READING_ID.NEXTVAL,"
                    + "?,?,?,?,"

                    + "?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"

                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?,?,?"

                    + ",?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,SYSDATE,?)";

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                int i = 1;

                ps.setLong(i++, applicationId);

                ps.setDate(
                        i++,
                        Date.valueOf(
                                LocalDate.parse(
                                        request.getConsumerDetails()
                                               .getReplacementDate(),
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy"))));

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getConsumerNumber());

                ps.setString(
                        i++,
                        oldMeterNo);

                // Import Header
                setReading(ps, i, impHdr);
                i += 6;

                // Import Slots
                setReading(ps, i, i1);
                i += 6;

                setReading(ps, i, i2);
                i += 6;

                setReading(ps, i, i3);
                i += 6;

                setReading(ps, i, i4);
                i += 6;

                // Export Header
                setReading(ps, i, expHdr);
                i += 6;

                // Export Slots
                setReading(ps, i, e1);
                i += 6;

                setReading(ps, i, e2);
                i += 6;

                setReading(ps, i, e3);
                i += 6;

                setReading(ps, i, e4);
                i += 6;

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getInstallationType());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getUpdatedBy());
                
  /***addition field*********/
                
                ps.setLong(
                        i++,
                        Long.parseLong(
                                request.getConsumerDetails()
                                       .getMeterTypePhaseId()));


                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getOwnership());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMeterWithinGuarantee());

                ps.setDate(
                        i++,
                        Date.valueOf(
                                LocalDate.parse(
                                        request.getConsumerDetails()
                                               .getGuaranteeEndDate(),
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy"))));

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getReplacementReason());

                ps.setString(
                        i++,
                        request.getConsumerDetails().getReusableMeter());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMeterSubType());

                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getAmispOldMtrRdngUsingOcrFlagYn());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getMakeCode());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getUpdatedBy());
                
                ps.setString(
                        i++,
                        request.getConsumerDetails()
                               .getAssessmentRequired());
                /***********end*********/
                
                System.out.println("Application Insert Started");
                ps.executeUpdate();
            }
        }
        private static void setReading(
                PreparedStatement ps,
                int i,
                ReadingDto r) throws Exception {

            ps.setDouble(i++, r.getKw());
            ps.setDouble(i++, r.getKwh());
            ps.setDouble(i++, r.getKva());
            ps.setDouble(i++, r.getKvah());
            ps.setDouble(i++, r.getRkvahLag());
            ps.setDouble(i++, r.getRkvahLead());
        }
    
    }