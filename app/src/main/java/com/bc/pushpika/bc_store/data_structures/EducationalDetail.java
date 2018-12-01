package com.bc.pushpika.bc_store.data_structures;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EducationalDetail {

    private String indexNumber = "";

    private String endYear = "";
    private String higherStudies = "";
    private String startYear = "";
    private String stream = "";

    // Default constructor required for calls to

    // DataSnapshot.getValue(EducationalDetail.class)
    public EducationalDetail() {}

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getHigherStudies() {
        return higherStudies;
    }

    public void setHigherStudies(String higherStudies) {
        this.higherStudies = higherStudies;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String AllEducationalDataText(){

        return "<B> A/L Stream </B> - "+ this.stream +"<br>"+
                "<B> Index number </B> - "+ this.indexNumber +"<br>"+
                "<B> Started Year </B> - "+ this.startYear +"<br>"+
                "<B> End Year </B> - "+ this.endYear +"<br>"+
                "<B> Higher studies </B> - "+ this.higherStudies +"<br>";
    }

}
