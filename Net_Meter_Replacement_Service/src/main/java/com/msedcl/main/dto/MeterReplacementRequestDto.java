package com.msedcl.main.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeterReplacementRequestDto {

    public ConsumerDetailsDto getConsumerDetails() {
		return consumerDetails;
	}

	public void setConsumerDetails(ConsumerDetailsDto consumerDetails) {
		this.consumerDetails = consumerDetails;
	}

	public MeterReadingDetailsDto getImportReading() {
		return importReading;
	}

	public void setImportReading(MeterReadingDetailsDto importReading) {
		this.importReading = importReading;
	}

	public MeterReadingDetailsDto getExportReading() {
		return exportReading;
	}

	public void setExportReading(MeterReadingDetailsDto exportReading) {
		this.exportReading = exportReading;
	}

	public OfficeDetailsDto getOfficeDetails() {
		return officeDetails;
	}

	public void setOfficeDetails(OfficeDetailsDto officeDetails) {
		this.officeDetails = officeDetails;
	}

	
	public MeterReplacementRequestDto() {
		// TODO Auto-generated constructor stub
	}
	@Valid
    @NotNull(message = "Consumer details are required")
    private ConsumerDetailsDto consumerDetails;

    @Valid
        private MeterReadingDetailsDto importReading;

    @Valid
       private MeterReadingDetailsDto exportReading;

    @Valid
   // @NotNull(message = "Office details are required")
    private OfficeDetailsDto officeDetails;
    
    @Valid
    private NonTodReadingDto nonTodReading;

	public NonTodReadingDto getNonTodReading() {
		return nonTodReading;
	}

	public void setNonTodReading(NonTodReadingDto nonTodReading) {
		this.nonTodReading = nonTodReading;
	}
}