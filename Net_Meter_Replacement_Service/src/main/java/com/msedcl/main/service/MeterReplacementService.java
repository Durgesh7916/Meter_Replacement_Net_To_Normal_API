package com.msedcl.main.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msedcl.main.Billing.validation.ConsumerBuValidationBilling;
import com.msedcl.main.Billing.validation.ConsumerNameLoaderBilling;
import com.msedcl.main.Billing.validation.ConsumerStatusValidatorBilling;
import com.msedcl.main.Billing.validation.DigitValidatorBilling;
import com.msedcl.main.Billing.validation.LtHtCodeValidatorBilling;
import com.msedcl.main.Billing.validation.TodExportReadingValidatorBilling;
import com.msedcl.main.Master.validation.DateValidator;
import com.msedcl.main.Master.validation.MeterReadingValidator;
import com.msedcl.main.Master.validation.MeterSubTypeValidatorMaster;
import com.msedcl.main.Billing.validation.MeterTypePhaseInstallationTypeValidatorBilling;
import com.msedcl.main.Master.validation.MeterTypePhaseInstallationTypeValidatorMaster;
import com.msedcl.main.Billing.validation.MeterValidationBilling;
import com.msedcl.main.Billing.validation.NonTodDigitValidatorBilling;
import com.msedcl.main.Billing.validation.NonTodReadingValidatorBilling;
import com.msedcl.main.Billing.validation.OfficeDetailsLoaderBilling;
import com.msedcl.main.Billing.validation.ReplacementDateValidatorBilling;
import com.msedcl.main.Billing.validation.SectionValidatorBilling;
import com.msedcl.main.Billing.validation.SmartMeterValidatorBilling;
import com.msedcl.main.Billing.validation.SolarRooftopValidatorBilling;
import com.msedcl.main.Billing.validation.SolarRooftopValidatorBilling;
import com.msedcl.main.Master.validation.OwnershipValidatorMaster;
import com.msedcl.main.Master.validation.ReplacementReasonValidatorMaster;
import com.msedcl.main.Master.validation.ServiceTypeValidator;
import com.msedcl.main.Billing.validation.TariffAndMeterSubtypeValidatorBilling;
import com.msedcl.main.Billing.validation.TodImportReadingValidatorBilling;
import com.msedcl.main.Master.validation.YesNoFieldValidator;
import com.msedcl.main.constant.MessageConstants;
import com.msedcl.main.dao.ApplicationDao;
import com.msedcl.main.dao.OldMeterReadingDao;
import com.msedcl.main.dto.MeterReplacementRequestDto;
import com.msedcl.main.dto.MeterReplacementResponseDto;
import com.msedcl.main.dto.OfficeDetailsDto;
import com.msedcl.main.repository.ApplicationAlreadyExistWithOldMeterNumberValidator;
import com.msedcl.main.repository.ConsumerExistsValidator;
import com.msedcl.main.repository.PendingApplicationValidator;

import jakarta.annotation.PostConstruct;

@Service
public class MeterReplacementService {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConsumerBuValidationBilling consumerBuValidationBilling;

    public String testConnection() {

        validateDatabaseConnection();

        return "Oracle DB Connected Successfully";
    }

    @PostConstruct
    public void testDbConnection() {

        try (Connection con = dataSource.getConnection()) {

            System.out.println("=================================");
            System.out.println("DB URL = " + con.getMetaData().getURL());
            System.out.println("DB USER = " + con.getMetaData().getUserName());
            System.out.println("DB CONNECTION SUCCESS");
            System.out.println("=================================");

        } catch (Exception e) {

            System.out.println("DB CONNECTION FAILED: " + e.getMessage());
        }
    }

    public MeterReplacementResponseDto save(
            MeterReplacementRequestDto request) {

        MeterReplacementResponseDto response =
                new MeterReplacementResponseDto();

        response.setId(1L);
        response.setConsumerNumber(
                request.getConsumerDetails().getConsumerNumber());
        response.setStatus("SUCCESS");
        response.setMessage(
                "Meter replacement data saved successfully");

        return response;
    }

    public MeterReplacementRequestDto getByConsumerNumber(
            String consumerNumber) {

        return new MeterReplacementRequestDto();
    }

    public MeterReplacementResponseDto update(
            Long id,
            MeterReplacementRequestDto request) {

        MeterReplacementResponseDto response =
                new MeterReplacementResponseDto();

        response.setId(id);
        response.setConsumerNumber(
                request.getConsumerDetails().getConsumerNumber());
        response.setStatus("SUCCESS");
        response.setMessage("Record updated successfully");

        return response;
    }

    public void delete(Long id) {

        // Delete logic
    }

    private void validateDatabaseConnection() {

        try (Connection con = dataSource.getConnection()) {

            if (!con.isValid(15)) {

                throw new RuntimeException(
                        MessageConstants.DB_CONNECTION_FAILED);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    MessageConstants.DB_CONNECTION_FAILED, e);
        }
    }

    public String createSolarToNormalApplication(
            MeterReplacementRequestDto request) {

        validateDatabaseConnection();

        try (Connection conn = dataSource.getConnection()) {

            // TODO:
            // Call package/procedure here
        	
        	// Validate import readings
        	
        	ServiceTypeValidator.validate(
        	        request.getConsumerDetails().getServiceTypeId());
        	
        	SmartMeterValidatorBilling.validate(
        	        dataSource,
        	        request.getConsumerDetails().getConsumerNumber());
        	
        	ConsumerStatusValidatorBilling.validate(
        	        dataSource,
        	        request.getConsumerDetails().getConsumerNumber());
        	
        	SolarRooftopValidatorBilling.validate(
        	        dataSource,
        	        request.getConsumerDetails().getConsumerNumber());
       
           // MeterReadingValidator.validate(request.getImportReading(), "Import");

            DateValidator.validateReplacementDate(
                    request.getConsumerDetails()
                           .getReplacementDate());

            DateValidator.validateGuaranteeEndDate(
                    request.getConsumerDetails()
                           .getGuaranteeEndDate());
            
            // Validate export readings
           // MeterReadingValidator.validate(request.getExportReading(), "Export");
            
            consumerBuValidationBilling.validateConsumerAndBillingUnit(
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getConsumerDetails().getBillingUnit());
            
            YesNoFieldValidator.validate(
                    request.getConsumerDetails());
            
            OfficeDetailsDto officeDetails =
                    new OfficeDetailsDto();
            
            OfficeDetailsLoaderBilling.populateOfficeDetails(
                    dataSource,
                    request.getConsumerDetails().getBillingUnit(),
                    officeDetails);
            
            request.setOfficeDetails(officeDetails);
            
            MeterValidationBilling.validateMeterDetails(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getConsumerDetails().getMakeCode(),
                    request.getConsumerDetails().getSerialNumber());
            
            MeterSubTypeValidatorMaster.validate(
                    dataSource,
                    request.getConsumerDetails().getMeterSubType());
            
            TariffAndMeterSubtypeValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    String.valueOf(
                            request.getConsumerDetails().getTariffCode()),
                    request.getConsumerDetails().getMeterSubType());
           
            MeterTypePhaseInstallationTypeValidatorMaster.validate(
                    dataSource,
                    request.getConsumerDetails().getMeterTypePhaseId(),
                    request.getConsumerDetails().getInstallationType());

            MeterTypePhaseInstallationTypeValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getConsumerDetails().getMeterTypePhaseId(),
                    request.getConsumerDetails().getInstallationType());
        
            
			/*
			 * MeterTypePhaseInstallationTypeValidator.validate( dataSource,
			 * request.getConsumerDetails().getConsumerNumber(),
			 * request.getConsumerDetails().getMeterType(),
			 * request.getConsumerDetails().getInstallationType());
			 * 
			 * 
			 * MeterTypePhaseInstallationTypeValidator.validate( dataSource,
			 * request.getConsumerDetails().getConsumerNumber(),
			 * request.getConsumerDetails().getInstallationType(),
			 * request.getConsumerDetails().getMeterType());
			 */
            
            String consumerName =
                    ConsumerNameLoaderBilling.getConsumerName(
                            dataSource,
                            request.getConsumerDetails().getConsumerNumber());

            request.getConsumerDetails()
                   .setConsumerName(consumerName);
            
            OwnershipValidatorMaster.validate(
                    dataSource,
                    request.getConsumerDetails().getOwnership());
            
            ReplacementReasonValidatorMaster.validate(
                    dataSource,
                    request.getConsumerDetails().getReplacementReason());
            
            
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate replacementDate =
                    LocalDate.parse(
                            request.getConsumerDetails().getReplacementDate(),
                            formatter);

            ReplacementDateValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getConsumerDetails().getInstallationType(),
                    replacementDate);
            
            
           
            
            DateValidator.validateReplacementDate(
                    request.getConsumerDetails()
                           .getReplacementDate());

            DateValidator.validateGuaranteeEndDate(
                    request.getConsumerDetails()
                           .getGuaranteeEndDate());
            
			/*
			 * ReplacementDateValidator.validate( dataSource,
			 * request.getConsumerDetails().getConsumerNumber(),
			 * request.getConsumerDetails().getInstallationType(),
			 * request.getConsumerDetails().getReplacementDate());
			 */
            String sectionId =
                    SectionValidatorBilling.validateAndGetSection(
                            dataSource,
                            request.getConsumerDetails()
                                   .getConsumerNumber());

            request.getConsumerDetails()
                   .setSectionId(sectionId);
			
 /**********************/	
        	
            String installationType =
                    request.getConsumerDetails()
                           .getInstallationType();

            if ("71".equals(installationType)) {

                if (request.getImportReading() == null) {
                    throw new IllegalArgumentException(
                            "Import reading details are required for TOD consumer.");
                }

                if (request.getExportReading() == null) {
                    throw new IllegalArgumentException(
                            "Export reading details are required for TOD consumer.");
                }

                
            }

            if ("11".equals(installationType)) {

                if (request.getNonTodReading() == null) {

                    throw new IllegalArgumentException(
                            "Non-TOD reading details are required.");
                }
            }
    /****************************************/    
            
            if ("11".equals(
                    request.getConsumerDetails()
                           .getInstallationType())) {

                NonTodReadingValidatorBilling.validate(
                        dataSource,
                        request.getConsumerDetails().getConsumerNumber(),
                        request.getNonTodReading().getImportFinalReadingKwh(),
                        request.getNonTodReading().getExportFinalReadingKwh());
                
                NonTodDigitValidatorBilling.validate(
                        dataSource,
                        request.getConsumerDetails()
                               .getConsumerNumber(),
                        request.getNonTodReading());
            }
        	
            if("71".equals(
                    request.getConsumerDetails()
                           .getInstallationType())) {
            	
            	   MeterReadingValidator.validate(
                           request.getImportReading(),
                           "Import");

                   MeterReadingValidator.validate(
                           request.getExportReading(),
                           "Export");
                   
                   DigitValidatorBilling.validate(
                           dataSource,
                           request.getConsumerDetails().getConsumerNumber(),
                           request.getImportReading(),
                           request.getExportReading());
            TodImportReadingValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getImportReading());
            TodExportReadingValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getExportReading());
            
            }
            
         
            
            
            
            LtHtCodeValidatorBilling.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber(),
                    request.getConsumerDetails().getLtHtCode());
            
            ConsumerExistsValidator.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber());

            PendingApplicationValidator.validate(
                    dataSource,
                    request.getConsumerDetails().getConsumerNumber());
            
            ApplicationAlreadyExistWithOldMeterNumberValidator
            .validate(
                    dataSource,
                    request.getConsumerDetails()
                           .getConsumerNumber(),
                    request.getConsumerDetails()
                           .getSerialNumber());

			/*
			 * String oldMeterNo = OldMeterValidator.validate( dataSource,
			 * request.getConsumerDetails().getConsumerNumber());
			 */
			/*
			 * try (Connection conn2 = dataSource.getConnection()) {
			 * 
			 * conn2.setAutoCommit(false);
			 * 
			 * try {
			 * 
			 * Long applicationId = ApplicationDao.createApplication( conn, request);
			 * 
			 * OldMeterReadingDao.insert( conn, applicationId,
			 * request.getConsumerDetails().getSerialNumber(), request);
			 * 
			 * conn2.commit();
			 * 
			 * return "Application Created Successfully. Application Id = " + applicationId;
			 * 
			 * } catch (Exception e) {
			 * 
			 * conn.rollback();
			 * 
			 * return "ERROR: " + e.getMessage(); } }
			 */
            
            //return "Validation successful";

            //return "SUCCESS";
            
            conn.setAutoCommit(false);

            try {

                Long applicationId =
                        ApplicationDao.createApplication(
                                conn,
                                request);

                OldMeterReadingDao.insert(
                        conn,
                        applicationId,
                        request.getConsumerDetails().getSerialNumber(),
                        request);

                // Commit both inserts
                conn.commit();

                return "Application Created Successfully. Application Id = "
                        + applicationId;

            } catch (Exception e) {

                // Rollback both inserts
                conn.rollback();

                e.printStackTrace();

                return "ERROR: " + e.getMessage();

            } finally {

                // Restore auto-commit (optional but recommended)
                conn.setAutoCommit(true);

            }

        } catch (Exception e) {

            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}