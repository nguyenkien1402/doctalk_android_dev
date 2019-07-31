package com.mobile.docktalk.models;

public class Doctor {

    private int id;
    private String firstName;
    private String lastName;
    private String preferName;
    private String clinicAddress;
    private String clinicSuburb;
    private String clinicState;
    private String clinicPostCode;
    private String joinedDate;
    private String userId;

    public Doctor() {
    }

    public Doctor(int id, String firstName, String lastName, String preferName, String clinicAddress, String clinicSuburb, String clinicState, String clinicPostCode, String joinedDate, String userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.preferName = preferName;
        this.clinicAddress = clinicAddress;
        this.clinicSuburb = clinicSuburb;
        this.clinicState = clinicState;
        this.clinicPostCode = clinicPostCode;
        this.joinedDate = joinedDate;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferName() {
        return preferName;
    }

    public void setPreferName(String preferName) {
        this.preferName = preferName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicSuburb() {
        return clinicSuburb;
    }

    public void setClinicSuburb(String clinicSuburb) {
        this.clinicSuburb = clinicSuburb;
    }

    public String getClinicState() {
        return clinicState;
    }

    public void setClinicState(String clinicState) {
        this.clinicState = clinicState;
    }

    public String getClinicPostCode() {
        return clinicPostCode;
    }

    public void setClinicPostCode(String clinicPostCode) {
        this.clinicPostCode = clinicPostCode;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}