package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TeamList {
    @SerializedName("data")
    private ArrayList<Team> teams;
    public ArrayList<Team> getTeams() {
        return teams;
    }
}
