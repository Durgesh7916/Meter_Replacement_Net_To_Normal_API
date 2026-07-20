package com.msedcl.main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.msedcl.main.dto.MeterReplacementRequestDto;

public class ApplicationDao {

    private ApplicationDao() {
    }

    public static Long createApplication(
            Connection conn,
            MeterReplacementRequestDto request)
            throws Exception {

        String sql =
                "INSERT INTO NC_METER_REPLACE_APPLICATION ("
                + "APPLICATION_ID_N,"
                + "APPLN_SERVICE_TYPE_ID_N,"
                + "ASSIGNED_CONSUMER_NO_C,"
                + "CURRENT_WF_STATUS_ID_N,"
                             + "CREATED_BY_C,"
                + "CREATED_DT,"
                + "CONSUMER_NAME,"
                + "ASSIGNED_BU_C,"
                + "CONSUMER_TYPE,"
                + "CONS_CATG_ID_N,"
                + "SUBCATEGORY_ID_N,"
                + "APPLICATION_DT,"
                + "APPLICATION_SUBMISSION_SOURCE,"
                + "CURRENT_WORKFLOW_STATUS_C,"
                + "STATUS_CD_C,"
                + "UPDATED_BY_C,"
                + "UPDATED_DT,"
                + "SECTION_ID_N,"
                + "TARIFF_CODE_C,"
                + "SOLAR_ROOFTOP_FLAG,"
                + "PC,"
                + "AMISP_SMART_METER_FLAG_YN,"
                + "AMISP_AGENCY_ID,"
                + "AMISP_AGENCY_NAME,"
                + "AMISP_OLD_MTR_RDNG_USING_OCR_FLAG_YN,"
                + "OLD_MTR_RDNG_USING_CMRI_FLAG_YN,"
                + "METER_PHASE_ID_N,"
                + "SMART_NET_TO_SMART_NORMAL_FLAG_YN"
                
                + ") VALUES ("
                + "SEQ_NC_APPLICATION.NEXTVAL,"
                + "32,"
                + "?,"
                + "31,"
                + "?,"
                + "SYSDATE,"
                + "?,"
                +"?,"
                +"?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                +"?,"
                +"SYSDATE,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                + "?,"
                +"?)";
        
        
        

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    request.getConsumerDetails()
                           .getConsumerNumber());

            ps.setString(
                    2,
                    request.getConsumerDetails()
                           .getUpdatedBy());
            
            ps.setString(
                    3,
                    request.getConsumerDetails()
                           .getConsumerName());
            
            ps.setString(
                    4,
                    request.getConsumerDetails()
                           .getBillingUnit());
            
            ps.setString(
                    5,
                    request.getConsumerDetails()
                           .getLtHtCode());
            System.out.println("Application Insert Started");
    
            
            
            String ltHtCode =
                    request.getConsumerDetails().getLtHtCode();

            Integer consumerType = null;

            if ("LT".equalsIgnoreCase(ltHtCode)
                    || "LTIP".equalsIgnoreCase(ltHtCode)) {
                consumerType = 1;
            } else if ("HT".equalsIgnoreCase(ltHtCode)) {
                consumerType = 2;
            }
            
            ps.setInt(6, consumerType);
            
            String sql1 =
                    "SELECT A.CONS_PARENT_CATG_ID_N  CONS_CATG_ID_N "
                  + "FROM MBC_CONS_CATEGORY_CD A, "
                  + "     MBC_CONS_CATEGORY_TARIFF_MAP B, "
                  + "     MBC_TARIFF_CD C "
                  + "WHERE A.CONS_CATG_ID_N = B.CONS_CATG_ID_N "
                  + "  AND B.TARIFF_CODE = C.TARIFF_CD_C "
                  + "  AND A.STATUS_CD_C = 'A' "
                  + "  AND LPAD(B.TARIFF_CODE, 3, '0') = LPAD(?, 3, '0') and rownum=1";

            Long consCatgId = null;
            Long consSubCatgId = null;

            try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {

                ps1.setString(
                        1,
                        request.getConsumerDetails()
                               .getTariffCode());

                try (ResultSet rs = ps1.executeQuery()) {

                    if (rs.next()) {

                        consCatgId =
                                rs.getLong("CONS_CATG_ID_N");

                        
                    } else {

                        throw new IllegalArgumentException(
                                "No Category Mapping Found For Tariff Code : "
                                + request.getConsumerDetails()
                                         .getTariffCode());
                    }
                }
            }

            System.out.println("CONS_CATG_ID_N = " + consCatgId);
            
            ps.setLong(7, consCatgId);
            
            
            ps.setString(
                    8,
                    request.getConsumerDetails()
                           .getReplacementDate());
            
            
            ps.setString(
                    9,
                    request.getConsumerDetails()
                           .getApplicationSubmissionSource());
            
            ps.setString(
                    10,
                   "Replacement Submitted");
            
            ps.setString(
                    11,
                    "A");
            
            ps.setString(
                    12,
                    request.getConsumerDetails()
                           .getUpdatedBy());
            
            ps.setString(
                    13,
                    request.getConsumerDetails()
                           .getSectionId());
            
            ps.setString(
                    14,
                    request.getConsumerDetails()
                           .getTariffCode());
            
            
            String solarFlag = null;
            String pc = null;

            String fetchSql =
                    "SELECT SOLAR_ROOFTOP_FLAG, PC "
                  + "FROM MBC_CONSUMER_MASTER "
                  + "WHERE CONSUMER_Number = ?";

            try (PreparedStatement ps1 = conn.prepareStatement(fetchSql)) {

                ps1.setString(
                        1,
                        request.getConsumerDetails().getConsumerNumber());

                try (ResultSet rs = ps1.executeQuery()) {

                    if (rs.next()) {

                        solarFlag = rs.getString("SOLAR_ROOFTOP_FLAG");
                        pc = rs.getString("PC");

                    } else {

                        throw new IllegalArgumentException(
                                "Consumer not found : "
                                + request.getConsumerDetails().getConsumerNumber());
                    }
                }
            }
            ps.setString(15, solarFlag);
            ps.setString(16, pc);
            
            
            ps.setString(17, request.getConsumerDetails().getAmispSmartMeterFlagYn());
            ps.setString(18, request.getConsumerDetails().getAmispAgencyId());
            
            ps.setString(19, request.getConsumerDetails().getAmispAgencyName());
            
            ps.setString(20, request.getConsumerDetails().getAmispOldMtrRdngUsingOcrFlagYn());
            
            ps.setString(21, request.getConsumerDetails().getOldMtrRdngUsingCmriFlagYn());

            ps.setString(22, request.getConsumerDetails().getMeterTypePhaseId());



            ps.setString(23, request.getConsumerDetails().getSmartNetToSmartNormalFlagYn().toUpperCase());
            
            
            ps.executeUpdate();
            
        }

        try (
                PreparedStatement ps =
                        conn.prepareStatement(
                                "SELECT SEQ_NC_APPLICATION.CURRVAL FROM DUAL");
                ResultSet rs =
                        ps.executeQuery()
        ) {

            rs.next();

            return rs.getLong(1);
        }
    }
}
