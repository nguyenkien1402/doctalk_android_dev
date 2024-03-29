package com.mobile.docktalk.models;

import java.util.List;

public class RequestConsult {

    private int id;
    private String briefOverview;
    private String inquiry;
    private int urgent;
    private int patientId;
    private String specification;
    public int professionalId;
    private List<RequestConsultDocument> requestConsultDocument;

    public RequestConsult(int id, String briefOverview, String inquiry, int urgent, int patientId, String specification,int professionalId, List<RequestConsultDocument> requestConsultDocument) {
        this.id = id;
        this.briefOverview = briefOverview;
        this.inquiry = inquiry;
        this.urgent = urgent;
        this.patientId = patientId;
        this.specification = specification;
        this.professionalId = professionalId;
        this.requestConsultDocument = requestConsultDocument;
    }

    public RequestConsult() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }
}
