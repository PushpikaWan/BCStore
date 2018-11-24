package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OccupationalDetail {


    private String companyName = "";
    private String companyAddress = "";
    private String jobTitle = "";
    private String phone = "";
    private String startDate = "";

    public OccupationalDetail() {
    }

    public OccupationalDetail(String companyName, String companyAddress, String jobTitle, String phone, String startDate) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.jobTitle = jobTitle;
        this.phone = phone;
        this.startDate = startDate;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String AllOccupationalDataText(){

        return "<B> Company Name </B> - "+ this.companyName +"<br>"+
                "<B> Company Address </B> - "+ this.companyAddress +"<br>"+
                "<B> Job Title </B> - "+ this.jobTitle +"<br>"+
                "<B> Start Date </B> - "+ this.startDate +"<br>"+
                "<B> Phone </B> - "+ this.phone +"<br>";
    }
}
