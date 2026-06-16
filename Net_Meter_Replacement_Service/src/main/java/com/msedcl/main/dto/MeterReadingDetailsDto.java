package com.msedcl.main.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public class MeterReadingDetailsDto {

	@Valid
    @NotNull(message = "Header reading is required")
    private ReadingDto headerReading;

    @Valid
    @NotNull(message = "Slot Reading 01 is required")
    private ReadingDto slotReading01;

    @Valid
    @NotNull(message = "Slot Reading 02 is required")
    private ReadingDto slotReading02;

    @Valid
    @NotNull(message = "Slot Reading 03 is required")
    private ReadingDto slotReading03;

    @Valid
    @NotNull(message = "Slot Reading 04 is required")
    private ReadingDto slotReading04;
    
    
    public MeterReadingDetailsDto() {
		// TODO Auto-generated constructor stub
	}


	public ReadingDto getHeaderReading() {
		return headerReading;
	}


	public void setHeaderReading(ReadingDto headerReading) {
		this.headerReading = headerReading;
	}


	public ReadingDto getSlotReading01() {
		return slotReading01;
	}


	public void setSlotReading01(ReadingDto slotReading01) {
		this.slotReading01 = slotReading01;
	}


	public ReadingDto getSlotReading02() {
		return slotReading02;
	}


	public void setSlotReading02(ReadingDto slotReading02) {
		this.slotReading02 = slotReading02;
	}


	public ReadingDto getSlotReading03() {
		return slotReading03;
	}


	public void setSlotReading03(ReadingDto slotReading03) {
		this.slotReading03 = slotReading03;
	}


	public ReadingDto getSlotReading04() {
		return slotReading04;
	}


	public void setSlotReading04(ReadingDto slotReading04) {
		this.slotReading04 = slotReading04;
	}
}
