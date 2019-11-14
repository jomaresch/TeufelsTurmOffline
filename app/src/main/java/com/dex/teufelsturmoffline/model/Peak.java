package com.dex.teufelsturmoffline.model;

public class Peak {
    String name;
    Double longitude, latitude;
    String id, tt_name;

    public Peak(String id, String name,  Double latitude, Double longitude,String tt_name) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.tt_name = tt_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTt_name() {
        return tt_name;
    }

    public void setTt_name(String tt_name) {
        this.tt_name = tt_name;
    }

    @Override
    public String toString() {
        return "Peak{" +
                "name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", id='" + id + '\'' +
                ", tt_name='" + tt_name + '\'' +
                '}';
    }
}