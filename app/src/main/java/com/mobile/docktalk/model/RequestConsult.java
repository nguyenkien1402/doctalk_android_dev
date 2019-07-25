package com.mobile.docktalk.model;

import java.util.List;

public class RequestConsult {

    private String briefOverview;
    private String inquiry;
    private int urgent;
    private int patientId;
    private String specification;
    private List<RequestConsultDocument> requestConsultDocument;

    public RequestConsult(String briefOverview, String inquiry, int urgent, int patientId, String specification, List<RequestConsultDocument> requestConsultDocument) {
        this.briefOverview = briefOverview;
        this.inquiry = inquiry;
        this.urgent = urgent;
        this.patientId = patientId;
        this.specification = specification;
        this.requestConsultDocument = requestConsultDocument;
    }

    public RequestConsult() {
    }

    public String getBriefOverview() {
        return briefOverview;
    }

    public void setBriefOverview(String briefOverview) {
        this.briefOverview = briefOverview;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<RequestConsultDocument> getRequestConsultDocument() {
        return requestConsultDocument;
    }

    public void setRequestConsultDocument(List<RequestConsultDocument> requestConsultDocument) {
        this.requestConsultDocument = requestConsultDocument;
    }
}
