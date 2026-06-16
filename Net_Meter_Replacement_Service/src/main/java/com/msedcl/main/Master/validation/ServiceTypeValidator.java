package com.msedcl.main.Master.validation;

public class ServiceTypeValidator {

    private ServiceTypeValidator() {
    }

    public static void validate(String serviceTypeId) {

        if (serviceTypeId == null
                || serviceTypeId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "ServiceTypeId is required. "
                    + "This API only allows ServiceTypeId 032 "
                    + "(Solar Net To Normal Replacement Service)");
        }

        if (!"032".equals(serviceTypeId.trim())) {

            throw new IllegalArgumentException(
                    "Invalid ServiceTypeId : "
                    + serviceTypeId
                    + ". This API only allows ServiceTypeId 032 "
                    + "(Solar Net To Normal Replacement Service)");
        }
    }
}