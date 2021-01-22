package com.canberkbbc.savebattery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoModel {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("contentinfo")
    @Expose
    private String contentinfo;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("title")
    @Expose
    private String title;

    public InfoModel() {
    }

    public InfoModel(String content, String contentinfo, String imgurl, String title) {
        super();
        this.content = content;
        this.contentinfo = contentinfo;
        this.imgurl = imgurl;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentinfo() {
        return contentinfo;
    }

    public void setContentinfo(String contentinfo) {
        this.contentinfo = contentinfo;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}