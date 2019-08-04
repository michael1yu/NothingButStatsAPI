package com.michael.yu.nbawiki.models;

import com.google.gson.annotations.SerializedName;

public class PlayerPageMeta {
    @SerializedName("total_pages")
    private String total_pages;

    @SerializedName("next_page")
    private String next_page;

    public String getTotal_pages() {
        return total_pages;
    }

    public String getNext_page() {
        return next_page;
    }
}
