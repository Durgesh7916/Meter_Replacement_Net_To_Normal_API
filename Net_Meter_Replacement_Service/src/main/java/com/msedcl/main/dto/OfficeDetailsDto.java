package com.msedcl.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OfficeDetailsDto {

    @NotBlank(message = "Region is required")
    private String region;

    @NotBlank(message = "Zone is required")
    private String zone;

    @NotBlank(message = "Circle is required")
    private String circle;

    @NotBlank(message = "Division is required")
    private String division;

    @NotBlank(message = "Subdivision is required")
    private String subdivision;
    
    public OfficeDetailsDto() {
		// TODO Auto-generated constructor stub
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}
    
    
}

