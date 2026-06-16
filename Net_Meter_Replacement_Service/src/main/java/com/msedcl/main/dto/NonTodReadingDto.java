package com.msedcl.main.dto;

import jakarta.validation.constraints.NotNull;

public class NonTodReadingDto {

    @NotNull
    private Double importFinalReadingKwh;

    @NotNull
    private Double importFinalSolarHourReadingKwh;

    @NotNull
    private Double exportFinalReadingKwh;

    @NotNull
    private Double exportFinalSolarReadingKwh;

    public Double getImportFinalReadingKwh() {
        return importFinalReadingKwh;
    }

    public void setImportFinalReadingKwh(Double importFinalReadingKwh) {
        this.importFinalReadingKwh = importFinalReadingKwh;
    }

    public Double getImportFinalSolarHourReadingKwh() {
        return importFinalSolarHourReadingKwh;
    }

    public void setImportFinalSolarHourReadingKwh(
            Double importFinalSolarHourReadingKwh) {
        this.importFinalSolarHourReadingKwh =
                importFinalSolarHourReadingKwh;
    }

    public Double getExportFinalReadingKwh() {
        return exportFinalReadingKwh;
    }

    public void setExportFinalReadingKwh(
            Double exportFinalReadingKwh) {
        this.exportFinalReadingKwh = exportFinalReadingKwh;
    }

    public Double getExportFinalSolarReadingKwh() {
        return exportFinalSolarReadingKwh;
    }

    public void setExportFinalSolarReadingKwh(
            Double exportFinalSolarReadingKwh) {
        this.exportFinalSolarReadingKwh =
                exportFinalSolarReadingKwh;
    }
}