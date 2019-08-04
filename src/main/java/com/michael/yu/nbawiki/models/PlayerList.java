package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayerList {
    @SerializedName("data")
    private ArrayList<Player> players;

    @SerializedName("meta")
    private PlayerPageMeta playerPageMeta;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public PlayerPageMeta getPlayerPageMeta() {
        return playerPageMeta;
    }
}
