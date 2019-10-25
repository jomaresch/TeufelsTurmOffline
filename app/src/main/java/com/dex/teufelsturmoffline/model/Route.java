package com.dex.teufelsturmoffline.model;

public class Route implements Comparable<Route>{
    private String name, id, mountain, scale, area, date;
    private int rating, fav, done;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMountain() {
        return mountain;
    }

    public void setMountain(String mountain) {
        this.mountain = mountain;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public Route(String name, String id, String mountain, String scale, String area, String date, int rating, int fav, int done) {
        this.name = name;
        this.id = id;
        this.mountain = mountain;
        this.scale = scale;
        this.area = area;
        this.date = date;
        this.rating = rating;
        this.fav = fav;
        this.done = done;
    }

    public Route() {
    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", mountain='" + mountain + '\'' +
                ", scale='" + scale + '\'' +
                ", area='" + area + '\'' +
                ", date='" + date + '\'' +

                ", rating=" + rating +
                ", fav=" + fav +
                ", done=" + done +
                '}';
    }



    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    @Override
    public int compareTo(Route o) {
        return this.getMountain().compareTo(o.getMountain());
    }
}
