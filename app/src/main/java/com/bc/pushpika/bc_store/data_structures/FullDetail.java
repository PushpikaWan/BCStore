package com.bc.pushpika.bc_store.data_structures;


public class FullDetail {

    private PersonalDetail personalDetail;
    private OccupationalDetail occupationalDetail;
    private EducationalDetail educationalDetail;
    private String isUserVerified;
    private String userID;


    public FullDetail(PersonalDetail personalDetail, OccupationalDetail occupationalDetail, EducationalDetail educationalDetail, String isUserVerified, String userID) {
        this.personalDetail = (personalDetail == null? new PersonalDetail():personalDetail);
        this.occupationalDetail = (occupationalDetail == null? new OccupationalDetail():occupationalDetail);
        this.educationalDetail = (educationalDetail == null? new EducationalDetail():educationalDetail);
        //handle null
        this.isUserVerified = isUserVerified;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public FullDetail() {
       personalDetail = new PersonalDetail();
       occupationalDetail = new OccupationalDetail();
       educationalDetail = new EducationalDetail();
       isUserVerified = "false";
       userID = "0";
    }

    public PersonalDetail getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetail personalDetail) {
        this.personalDetail = personalDetail;
    }

    public OccupationalDetail getOccupationalDetail() {
        return occupationalDetail;
    }

    public void setOccupationalDetail(OccupationalDetail occupationalDetail) {
        this.occupationalDetail = occupationalDetail;
    }

    public EducationalDetail getEducationalDetail() {
        return educationalDetail;
    }

    public void setEducationalDetail(EducationalDetail educationalDetail) {
        this.educationalDetail = educationalDetail;
    }

    public String getIsUserVerified() {
        return isUserVerified;
    }

    public void setIsUserVerified(String isUserVerified) {
        this.isUserVerified = isUserVerified;
    }
}