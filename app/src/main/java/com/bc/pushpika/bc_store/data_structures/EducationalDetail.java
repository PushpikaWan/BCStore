package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EducationalDetail {

    private String endDate = "";
    private String higherStudies = "";
    private String startDate = "";
    private String stream = "";


    // Default constructor required for calls to
    // DataSnapshot.getValue(EducationalDetail.class)
    public EducationalDetail() {

    }

    public EducationalDetail(String endDate, String higherStudies, String startDate, String stream) {
        this.endDate = endDate;
        this.higherStudies = higherStudies;
        this.startDate = startDate;
        this.stream = stream;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHigherStudies() {
        return higherStudies;
    }

    public void setHigherStudies(String higherStudies) {
        this.higherStudies = higherStudies;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String AllEducationalDataText(){

        return "<B> A/L Stream </B> - "+ this.stream +"<br>"+
                "<B> Start Date </B> - "+ this.startDate +"<br>"+
                "<B> End Date </B> - "+ this.endDate +"<br>"+
                "<B> Higher studies </B> - "+ this.higherStudies +"<br>";
    }

}
