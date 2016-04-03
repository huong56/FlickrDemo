package com.huongtt.flickrdemo.model;

/**
 * Created by admin on 03/04/2016.
 */
public class Photo {

    /*API service flickr.galleries.getPhotos*/
    private String id;
    private String owner;
    private String secret;
    private int server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfriend;
    private boolean isfamily;
    private boolean is_primary;
    private boolean has_comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean ispublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public boolean isfriend() {
        return isfriend;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }

    public boolean isfamily() {
        return isfamily;
    }

    public void setIsfamily(boolean isfamily) {
        this.isfamily = isfamily;
    }

    public boolean is_primary() {
        return is_primary;
    }

    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    public boolean isHas_comment() {
        return has_comment;
    }

    public void setHas_comment(boolean has_comment) {
        this.has_comment = has_comment;
    }
}
