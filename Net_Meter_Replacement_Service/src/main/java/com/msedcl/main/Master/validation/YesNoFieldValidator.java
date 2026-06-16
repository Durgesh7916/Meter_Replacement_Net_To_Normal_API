package com.msedcl.main.Master.validation;

import com.msedcl.main.dto.ConsumerDetailsDto;

public class YesNoFieldValidator {

    private YesNoFieldValidator() {
    }

    public static void validate(ConsumerDetailsDto consumerDetails) {

        validateYesNo(
                consumerDetails.getMeterWithinGuarantee(),
                "meterWithinGuarantee");

        validateYesNo(
                consumerDetails.getReusableMeter(),
                "reusableMeter");

        validateAssessmentRequired(
                consumerDetails.getAssessmentRequired());
    }

    private static void validateYesNo(
            String value,
            String fieldName) {

        if (value == null || value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    fieldName
                    + " is required. Allowed values are Y For Yes or N For No.");
        }

        if (!"Y".equalsIgnoreCase(value)
                && !"N".equalsIgnoreCase(value)) {

            throw new IllegalArgumentException(
                    "Invalid value for "
                    + fieldName
                    + ". Provided value = "
                    + value
                    + ". Allowed values are Y For Yes or N For No.");
        }
    }

    private static void validateAssessmentRequired(
            String value) {

        if (value == null || value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "assessmentRequired is required. Allowed value is N(No).");
        }

        if (!"N".equalsIgnoreCase(value)) {

            throw new IllegalArgumentException(
                    "Invalid value for assessmentRequired. "
                    + "Provided value = "
                    + value
                    + ". Allowed value is N(No) only.");
        }
    }
}