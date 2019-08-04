package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("position")
    private String position;

    @SerializedName("height_feet")
    private String height_feet;

    @SerializedName("height_inches")
    private String height_inches;

    @SerializedName("weight_pounds")
    private String weight_pounds;

    @SerializedName("team")
    private Team team;

    private String playerProfile;


    public String getFirst_name() {
        return first_name;
    }

    public String getId() {
        return id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPosition() {
        return position;
    }

    public String getHeight_feet() {
        return height_feet;
    }

    public String getHeight_inches() {
        return height_inches;
    }

    public String getWeight_pounds() {
        return weight_pounds;
    }

    public Team getTeam(){
        return team;
    }

    public String getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(String playerProfile) {
        playerProfile = this.playerProfile;
    }

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }


}
