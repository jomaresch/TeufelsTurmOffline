package com.dex.teufelsturmoffline.model;

public class AreaSpinnerData {
    private String area;
    private int routeCount;

    public AreaSpinnerData(String area, int routeCount) {
        this.area = area;
        this.routeCount = routeCount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    @Override
    public String toString() {
        return  area;
    }
}
