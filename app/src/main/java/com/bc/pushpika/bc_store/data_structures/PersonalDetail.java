package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PersonalDetail {

    private String address = "";
    private String dOB = "";

    private String idNumber = "";
    private String emailAddress = "";
    private String home = "";
    private String married = "";
    private String mobile = "";
    private String name = "";

    private String numberOfChildren = "";

    public PersonalDetail() {

    }

    public String getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(String numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }


    public String AllPersonalDataText(){

        String result =  "<B> Name </B> - "+ this.name +"<br>"+
                "<B> Address </B> - "+ this.address +"<br>"+
                "<B> ID number </B> - "+ this.idNumber +"<br>"+
                "<B> DOB </B> - "+ this.dOB +"<br>"+
                "<B> EmailAddress </B> - "+ this.emailAddress +"<br>"+
                "<B> Mobile </B> - "+ this.mobile +"<br>"+
                "<B> Home </B> - "+ this.home +"<br>"+
                "<B> Married </B> - "+ this.married +"<br>";
        if(this.married.equals("yes")){
            result += "<B> Number of children </B> - "+ this.numberOfChildren +"<br>";
        }
        return result;
    }

}
