package com.msedcl.main.dto;


public class ApplicationStatusResponse {

    private Long applicationId;
    private String consumerNumber;
    private String applicationServiceTypeId;
    private String workflowStatus;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

   

    public String getApplicationServiceTypeId() {
		return applicationServiceTypeId;
	}

	public void setApplicationServiceTypeId(String applicationServiceTypeId) {
		this.applicationServiceTypeId = applicationServiceTypeId;
	}

	public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }
}