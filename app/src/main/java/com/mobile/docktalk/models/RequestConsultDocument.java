package com.mobile.docktalk.models;

public class RequestConsultDocument {

    private String documentType;
    private String documentName;
    private String documentLink;

    public RequestConsultDocument(String documentType, String documentName, String documentLink) {
        this.documentType = documentType;
        this.documentName = documentName;
        this.documentLink = documentLink;
    }

    public RequestConsultDocument() {
    }


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }
}
