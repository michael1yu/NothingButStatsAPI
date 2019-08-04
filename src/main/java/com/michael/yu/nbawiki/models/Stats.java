package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("games_played")
    String games_played;
    @SerializedName("player_id")
    String player_id;
    @SerializedName("season")
    String season;
    @SerializedName("min")
    String min;
    @SerializedName("fgm")
    String fgm;
    @SerializedName("fga")
    String fga;
    @SerializedName("fg3m")
    String fg3m;
    @SerializedName("fg3a")
    String fg3a;
    @SerializedName("ftm")
    String ftm;
    @SerializedName("fta")
    String fta;
    @SerializedName("oreb")
    String oreb;
    @SerializedName("dreb")
    String dreb;
    @SerializedName("reb")
    String reb;
    @SerializedName("ast")
    String ast;
    @SerializedName("stl")
    String stl;
    @SerializedName("blk")
    String blk;
    @SerializedName("turnover")
    String turnover;
    @SerializedName("pf")
    String pf;
    @SerializedName("pts")
    String pts;
    @SerializedName("fg_pct")
    String fg_pct;
    @SerializedName("fg3_pct")
    String fg3_pct;
    @SerializedName("ft_pct")
    String ft_pct;

    public String getGames_played() {
        return games_played;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public String getSeason() {
        return season;
    }

    public String getMin() {
        return min;
    }

    public String getFgm() {
        return fgm;
    }

    public String getFga() {
        return fga;
    }

    public String getFg3m() {
        return fg3m;
    }

    public String getFg3a() {
        return fg3a;
    }

    public String getFtm() {
        return ftm;
    }

    public String getFta() {
        return fta;
    }

    public String getOreb() {
        return oreb;
    }

    public String getDreb() {
        return dreb;
    }

    public String getReb() {
        return reb;
    }

    public String getAst() {
        return ast;
    }

    public String getStl() {
        return stl;
    }

    public String getBlk() {
        return blk;
    }

    public String getTurnover() {
        return turnover;
    }

    public String getPf() {
        return pf;
    }

    public String getPts() {
        return pts;
    }

    public String getFg_pct() {
        return fg_pct;
    }

    public String getFg3_pct() {
        return fg3_pct;
    }

    public String getFt_pct() {
        return ft_pct;
    }
}
