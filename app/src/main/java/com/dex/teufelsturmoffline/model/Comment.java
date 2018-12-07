package com.dex.teufelsturmoffline.model;

public class Comment {
    String name, comment, date, routeId;
    int rating;

    public Comment(String name, String comment, String date, String routeId, int rating) {
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.routeId = routeId;
        this.rating = rating;
    }
    public Comment() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", routeId='" + routeId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
