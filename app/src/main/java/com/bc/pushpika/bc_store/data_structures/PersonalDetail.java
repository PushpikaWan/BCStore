package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PersonalDetail {

    private String address = "";
    private String dOB = "";
    private String emailAddress = "";
    private String home = "";
    private String married = "";
    private String mobile = "";
    private String name = "";

    public PersonalDetail() {

    }

    public PersonalDetail(String address, String dOB, String emailAddress, String home, String married, String mobile, String name) {
        this.address = address;
        this.dOB = dOB;
        this.emailAddress = emailAddress;
        this.home = home;
        this.married = married;
        this.mobile = mobile;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllPersonalDataText(){

        return " <B> Occupational Data </B>  \n \n"+
                "<B> Name </B> - "+ this.name +"\n"+
                "<B> Address </B> - "+ this.address +"\n"+
                "<B> DOB </B> - "+ this.dOB +"\n"+
                "<B> EmailAddress </B> - "+ this.emailAddress +"\n"+
                "<B> Mobile </B> - "+ this.mobile +"\n"+
                "<B> Home </B> - "+ this.home +"\n"+
                "<B> Married </B> - "+ this.married +"\n";
    }

}
