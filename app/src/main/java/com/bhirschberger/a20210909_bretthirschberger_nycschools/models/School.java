package com.bhirschberger.a20210909_bretthirschberger_nycschools.models;

import java.io.Serializable;

// class is serializable so it can be passed between in an intent
final public class School implements Serializable {
    private final String dbn;
    private final String name;
    private final String address;
    private final String overviewParagraph;
    private final String latitude;
    private final String longitude;
    private final String phoneNumber;
    private final String email;
    private String testTakers;
    private String averageReadingScore;
    private String averageMathScore;
    private String averageWritingScore;
    private boolean expanded;

    public School(String dbn, String name, String address, String overviewParagraph, String latitude, String longitude, String phoneNumber, String email) {
        this.dbn = dbn;
        this.name = name;
        this.address = address;
        this.overviewParagraph = overviewParagraph;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.expanded = false;
    }

    public void setTestScores(String testTakers, String averageReadingScore, String averageMathScore, String averageWritingScore) {
        this.testTakers = testTakers;
        this.averageReadingScore = averageReadingScore;
        this.averageMathScore = averageMathScore;
        this.averageWritingScore = averageWritingScore;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getOverviewParagraph() {
        return overviewParagraph;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getAverageReadingScore() {
        return averageReadingScore;
    }

    public String getAverageMathScore() {
        return averageMathScore;
    }

    public String getAverageWritingScore() {
        return averageWritingScore;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "School{" +
                "dbn='" + dbn + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", overviewParagraph='" + overviewParagraph + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", testTakers='" + testTakers + '\'' +
                ", averageReadingScore='" + averageReadingScore + '\'' +
                ", averageMathScore='" + averageMathScore + '\'' +
                ", averageWritingScore='" + averageWritingScore + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
