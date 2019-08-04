package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("id")
    private String id;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("city")
    private String city;

    @SerializedName("conference")
    private String conference;

    @SerializedName("division")
    private String division;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("name")
    private String name;

    public String getId(){
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getCity() {
        return city;
    }

    public String getConference() {
        return conference;
    }

    public String getDivision() {
        return division;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
