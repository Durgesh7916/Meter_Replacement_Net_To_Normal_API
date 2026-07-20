
package com.msedcl.main.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConsumerDetailsDto {



    @NotBlank(message = "Consumer number is required")
    @Pattern(regexp = "\\d{12}", message = "Consumer number must be 12 digits")
    private String consumerNumber;

    @NotBlank(message = "Consumer name is required")
    private String consumerName;

    @NotNull(message = "Tariff code is required")
    private String tariffCode;

    @NotBlank(message = "Billing unit is required")
    private String billingUnit;

    @NotBlank(message = "Make code is required")
    private String makeCode;
    
    @NotBlank(message = "Meter Type is required")
    private String meterTypePhaseId;

    public String getMeterTypePhaseId() {
        return meterTypePhaseId;
    }

    public void setMeterTypePhaseId(String meterType) {
        this.meterTypePhaseId = meterType;
    }
    
    @NotBlank(message =
            "ServiceTypeId is required. "
            + "This API only allows ServiceTypeId 032 "
            + "(Solar Net To Normal Replacement Service)")
    private String serviceTypeId;

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}

	@NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Meter subtype is required")
    private String meterSubType;

    public String getReplacementDate() {
		return replacementDate;
	}

	public void setReplacementDate(String replacementDate) {
		this.replacementDate = replacementDate;
	}

	@NotBlank(message = "Ownership is required")
    private String ownership;

    @NotBlank(message = "Meter guarantee status is required")
    private String meterWithinGuarantee;

    @NotBlank(message = "Replacement reason is required")
    private String replacementReason;

    @NotNull(message = "Reusable meter flag is required")
    private String reusableMeter;
    
    
   

    public String getAssessmentRequired() {
		return assessmentRequired;
	}

	public void setAssessmentRequired(String assessmentRequired) {
		this.assessmentRequired = assessmentRequired;
	}

	public ConsumerDetailsDto() {
		// TODO Auto-generated constructor stub
	}
	

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	
	public String getBillingUnit() {
		return billingUnit;
	}

	public void setBillingUnit(String billingUnit) {
		this.billingUnit = billingUnit;
	}

	public String getMakeCode() {
		return makeCode;
	}

	public void setMakeCode(String makeCode) {
		this.makeCode = makeCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getMeterSubType() {
		return meterSubType;
	}

	public void setMeterSubType(String meterSubType) {
		this.meterSubType = meterSubType;
	}

	public String getOwnership() {
		return ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}

	public String getReusableMeter() {
		 if ("YES".equalsIgnoreCase(reusableMeter)) {
	            return "Y";
	        }
	        if ("NO".equalsIgnoreCase(reusableMeter)) {
	            return "N";
	        }
	        return reusableMeter;
	}

	public void setReusableMeter(String reusableMeter) {
		this.reusableMeter = reusableMeter;
	}



	public String getMeterWithinGuarantee() {
		
		 if ("YES".equalsIgnoreCase(meterWithinGuarantee)) {
	            return "Y";
	        }
	        if ("NO".equalsIgnoreCase(meterWithinGuarantee)) {
	            return "N";
	        }
	        
		return meterWithinGuarantee;
	}

	public void setMeterWithinGuarantee(String meterWithinGuarantee) {
		this.meterWithinGuarantee = meterWithinGuarantee;
	}

	public String getReplacementReason() {
		return replacementReason;
	}

	public void setReplacementReason(String replacementReason) {
		this.replacementReason = replacementReason;
	}



	

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String section) {
		this.sectionId = section;
	}

	public String getInstallationType() {
		return installationType;
	}

	public void setInstallationType(String installationType) {
		this.installationType = installationType;
	}

	public String getGuaranteeEndDate() {
		return guaranteeEndDate;
	}

	public void setGuaranteeEndDate(String guaranteeEndDate) {
		this.guaranteeEndDate = guaranteeEndDate;
	}

	

	@NotNull(message = "assessment required flag is required should be N (No)")
    private String assessmentRequired;

    @NotBlank(message = "Section is required")
    private String sectionId;

    @NotBlank(message = "Installation type is required")
    private String installationType;

    @NotBlank(message = "Guarantee end date is required")
  
    private String guaranteeEndDate;
    
    @NotBlank(message = "Replacement date is required")
       
    private String replacementDate;
    
    
    
    @NotBlank(message = "AMISP Agency Id is required")
    private String amispAgencyId;

    @NotBlank(message = "AMISP Agency Name is required")
    private String amispAgencyName;

    @NotBlank(message = "OLD_MTR_RDNG_USING_CMRI_FLAG_YN is required")
    @Pattern(
            regexp = "^[YNyn]$",
            message = "OLD_MTR_RDNG_USING_CMRI_FLAG_YN must be Y or N")
    private String oldMtrRdngUsingCmriFlagYn;

    @NotBlank(message = "AMISP_OLD_MTR_RDNG_USING_OCR_FLAG_YN is required")
    @Pattern(
            regexp = "^[YNyn]$",
            message = "AMISP_OLD_MTR_RDNG_USING_OCR_FLAG_YN must be Y or N")
    private String amispOldMtrRdngUsingOcrFlagYn;

    @NotBlank(message = "AMISP_SMART_METER_FLAG_YN is required")
    @Pattern(
            regexp = "^[Yy]$",
            message = "AMISP_SMART_METER_FLAG_YN must be Y")
    private String amispSmartMeterFlagYn;

    @NotBlank(message = "Application Submission Source is required")
    private String applicationSubmissionSource;


    @NotBlank(message = "LT/HT Code is required")
    private String ltHtCode;

    @NotBlank(message = "Updated By is required")
    private String updatedBy;

    @NotBlank(message = "SMART_NET_TO_SMART_NORMAL_FLAG_YN is mandatory")
    @Pattern(
        regexp = "[Yy]",
        message = "SMART_NET_TO_SMART_NORMAL_FLAG_YN must be Y"
    )
    private String smartNetToSmartNormalFlagYn;
    
	public String getSmartNetToSmartNormalFlagYn() {
		return smartNetToSmartNormalFlagYn;
	}

	public void setSmartNetToSmartNormalFlagYn(String smartNetToSmartNormalFlagYn) {
		this.smartNetToSmartNormalFlagYn = smartNetToSmartNormalFlagYn;
	}

	public String getAmispAgencyId() {
		return amispAgencyId;
	}

	public void setAmispAgencyId(String amispAgencyId) {
		this.amispAgencyId = amispAgencyId;
	}

	public String getAmispAgencyName() {
		return amispAgencyName;
	}

	public void setAmispAgencyName(String amispAgencyName) {
		this.amispAgencyName = amispAgencyName;
	}

	public String getOldMtrRdngUsingCmriFlagYn() {
		return oldMtrRdngUsingCmriFlagYn;
	}

	public void setOldMtrRdngUsingCmriFlagYn(String oldMtrRdngUsingCmriFlagYn) {
		this.oldMtrRdngUsingCmriFlagYn = oldMtrRdngUsingCmriFlagYn;
	}

	public String getAmispOldMtrRdngUsingOcrFlagYn() {
		return amispOldMtrRdngUsingOcrFlagYn;
	}

	public void setAmispOldMtrRdngUsingOcrFlagYn(String amispOldMtrRdngUsingOcrFlagYn) {
		this.amispOldMtrRdngUsingOcrFlagYn = amispOldMtrRdngUsingOcrFlagYn;
	}

	public String getAmispSmartMeterFlagYn() {
		return amispSmartMeterFlagYn;
	}

	public void setAmispSmartMeterFlagYn(String amispSmartMeterFlagYn) {
		this.amispSmartMeterFlagYn = amispSmartMeterFlagYn;
	}

	public String getApplicationSubmissionSource() {
		return applicationSubmissionSource;
	}

	public void setApplicationSubmissionSource(String applicationSubmissionSource) {
		this.applicationSubmissionSource = applicationSubmissionSource;
	}

	public String getLtHtCode() {
		return ltHtCode;
	}

	public void setLtHtCode(String ltHtCode) {
		this.ltHtCode = ltHtCode;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
    
}

