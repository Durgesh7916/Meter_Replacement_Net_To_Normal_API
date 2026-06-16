package com.msedcl.main.dto;




import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ReadingDto {

    @Valid
	@NotNull(message = "KW reading is required")
    private Double kw;

    @Valid
    @NotNull(message = "KWH reading is required")
    private Double kwh;

    @Valid
    @NotNull(message = "KVA reading is required")
    private Double kva;

    @Valid
    @NotNull(message = "KVAH reading is required")
    private Double kvah;

    @Valid
    @NotNull(message = "RKVAH Lag reading is required")
    private Double rkvahLag;

    @Valid
    @NotNull(message = "RKVAH Lead reading is required")
    private Double rkvahLead;
    
    public ReadingDto() {
		// TODO Auto-generated constructor stub
	}

	public Double getKw() {
		return kw;
	}

	public void setKw(Double kw) {
		this.kw = kw;
	}

	public Double getKwh() {
		return kwh;
	}

	public void setKwh(Double kwh) {
		this.kwh = kwh;
	}

	public Double getKva() {
		return kva;
	}

	public void setKva(Double kva) {
		this.kva = kva;
	}

	public Double getKvah() {
		return kvah;
	}

	public void setKvah(Double kvah) {
		this.kvah = kvah;
	}

	public Double getRkvahLag() {
		return rkvahLag;
	}

	public void setRkvahLag(Double rkvahLag) {
		this.rkvahLag = rkvahLag;
	}

	public Double getRkvahLead() {
		return rkvahLead;
	}

	public void setRkvahLead(Double rkvahLead) {
		this.rkvahLead = rkvahLead;
	}
}


