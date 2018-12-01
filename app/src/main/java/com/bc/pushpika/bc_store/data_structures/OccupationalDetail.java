package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OccupationalDetail {


    private String companyName = "";
    private String companyAddress = "";
    private String jobTitle = "";
    private String phone = "";
    private String startYear = "";

    public OccupationalDetail() { }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String AllOccupationalDataText(){

        return "<B> Company Name </B> - "+ this.companyName +"<br>"+
                "<B> Company Address </B> - "+ this.companyAddress +"<br>"+
                "<B> Job Title </B> - "+ this.jobTitle +"<br>"+
                "<B> Started Year </B> - "+ this.startYear +"<br>"+
                "<B> Phone </B> - "+ this.phone +"<br>";
    }
}
