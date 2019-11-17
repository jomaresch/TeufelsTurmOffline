package com.dex.teufelsturmoffline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;


import com.dex.teufelsturmoffline.model.Comment;
import com.dex.teufelsturmoffline.model.Peak;
import com.dex.teufelsturmoffline.model.Route;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "otto.db";
    public final static String TABLE_ROUTES = "ROUTES";
    public final static String TABLE_COMMENTS = "COMMENTS";
    public final static String TABLE_PEAKS = "PEAKS";
    public final static String TABLE_MY_COMMENTS = "MY_COMMENTS";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Route> getRoutesByArea(String area) {

        List routeList = new ArrayList<Route>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_ROUTES + " LEFT JOIN "+TABLE_PEAKS+" ON "+TABLE_PEAKS+".TT_NAME = "+TABLE_ROUTES+".MOUNTAIN WHERE AREA == '" + area + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while (cursor.moveToNext()) {
            Route route = getRouteFromCursor(cursor);
            routeList.add(route);
        }
        db.close();
        return routeList;
    }

    public List<Pair<String, Integer>> getAreas() {
        List areaList = new ArrayList<Pair<String, Integer>>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT DISTINCT AREA, COUNT(AREA) FROM " + TABLE_ROUTES + " GROUP BY AREA";
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while (cursor.moveToNext()) {
            areaList.add(new Pair<>(
                    cursor.getString(0),
                    cursor.getInt(1))
            );
        }
        db.close();
        return areaList;
    }

    public List<Comment> getCommentByRoute(String id) {

        List commentList = new ArrayList<Comment>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_COMMENTS + " WHERE ROUTE_ID == '" + id + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while (cursor.moveToNext()) {
            Comment comment = getCommentFromCursor(cursor);
            commentList.add(comment);
        }

        db.close();
        return commentList;
    }

    public List<Route> getFavRoutes() {

        List routesList = new ArrayList<Route>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_ROUTES + " WHERE FAV == 1";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while (cursor.moveToNext()) {
            Route route = getRouteFromCursor(cursor);
            routesList.add(route);
        }

        db.close();
        return routesList;
    }

    public List<Route> getPeakRoutes(String peak) {

        List routesList = new ArrayList<Route>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_ROUTES + " WHERE MOUNTAIN == '"+ peak+"'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while (cursor.moveToNext()) {
            Route route = getRouteFromCursor(cursor);
            routesList.add(route);
        }

        db.close();
        return routesList;
    }


    public boolean addRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", route.getId());
        contentValues.put("NAME", route.getName());
        contentValues.put("MOUNTAIN", route.getMountain());
        contentValues.put("SCALE", route.getScale());
        contentValues.put("AREA", route.getArea());
        contentValues.put("DATE", route.getDate());
        contentValues.put("RATING", route.getRating());
        contentValues.put("FAV", route.getFav());
        long result = db.insert(TABLE_ROUTES, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", route.getId());
        contentValues.put("NAME", route.getName());
        contentValues.put("MOUNTAIN", route.getMountain());
        contentValues.put("SCALE", route.getScale());
        contentValues.put("AREA", route.getArea());
        contentValues.put("DATE", route.getDate());
        contentValues.put("RATING", route.getRating());
        contentValues.put("FAV", route.getFav());
        long result = db.update(TABLE_ROUTES, contentValues, "ID == " + route.getId(), null);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void setFavorite(boolean favorite, String weg_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (favorite) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("FAV", 1);
            db.update(TABLE_ROUTES, contentValues, "ID = '" + weg_id + "'", null);
        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("fav", 0);
            db.update(TABLE_ROUTES, contentValues, "ID = '" + weg_id + "'", null);
        }
    }

    public boolean addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ROUTE_ID", comment.getRouteId());
        contentValues.put("NAME", comment.getName());
        contentValues.put("COMMENT", comment.getComment());
        contentValues.put("DATE", comment.getDate());
        contentValues.put("RATING", comment.getRating());
        long result = db.insert(TABLE_COMMENTS, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Route getRouteById(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_ROUTES + " WHERE ID == " + id;
        Cursor cursor = db.rawQuery(sqlStatement, null);
        Route route = new Route();
        route.setDate("1975.01.01");
        route.setName("");
        if (cursor.moveToFirst()) {
            route = getRouteFromCursor(cursor);
        }
        return route;
    }

    public Comment getCommentByNameAndDate(String name, String date, String route) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement =
                "SELECT * FROM " + TABLE_COMMENTS +
                        " WHERE NAME == '" + name + "' " +
                        "AND DATE == '" + date + "' " +
                        "AND ROUTE_ID == '" + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        Comment comment = new Comment();
        comment.setName("");
        if (cursor.moveToFirst()) {
            comment = getCommentFromCursor(cursor);
        }
        db.close();
        return comment;
    }

    private Comment getCommentFromCursor(Cursor cursor) {
        return new Comment(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4)
        );
    }

    private Route getRouteFromCursor(Cursor cursor) {
        Route r = new Route(
                cursor.getString(1),
                cursor.getString(0),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8)
        );
        if( cursor.getColumnCount() > 9){
            r.setPeak_id(cursor.getString(15));
        }
        return r;
    }

    public String countRoutes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT COUNT(ID) FROM " + TABLE_ROUTES;
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getInt(0));
        } else {
            return "0";
        }
    }

    public String countComments() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT COUNT(*) FROM " + TABLE_COMMENTS;
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getInt(0));
        } else {
            return "0";
        }
    }

    public String countMountain() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT COUNT(distinct MOUNTAIN) FROM " + TABLE_ROUTES;
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getInt(0));
        } else {
            return "0";
        }
    }

    public String getLastCommentDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT DATE FROM " + TABLE_COMMENTS + " ORDER BY DATE DESC";
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return "";
        }
    }

    public void setDone(boolean value, String weg_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        if (value)
            contentValues.put("DONE", 1);
        else
            contentValues.put("DONE", 0);

        db.update(TABLE_ROUTES, contentValues, "ID = '" + weg_id + "'", null);
        db.close();
    }

    public void setMyComment(String weg_id, String myComment) {
        String sqlStatement = "";
        if (!getMyComment(weg_id).first){
            sqlStatement = "INSERT INTO " + TABLE_MY_COMMENTS + " VALUES ('" + weg_id + "','" + myComment + "')";
        } else {
            sqlStatement = "UPDATE " + TABLE_MY_COMMENTS + " SET COMMENT = '" + myComment + "' WHERE ID = '" + weg_id + "'";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sqlStatement);
        db.close();
    }

    public Pair<Boolean,String> getMyComment(String weg_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Pair<Boolean,String> result = new Pair<>(false, "");

        String sqlStatement = "SELECT * FROM "+ TABLE_MY_COMMENTS + " WHERE ID = '" + weg_id + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);
        if (cursor.moveToNext()){
            result = new Pair<>(true, cursor.getString(1));
        }
        db.close();
        return result;
    }

    public List<Route> getDoneRoutes(){
        List<Route> routes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement =  "SELECT * FROM "+ TABLE_ROUTES + " WHERE DONE = 1";
        Cursor cursor = db.rawQuery(sqlStatement, null );
        while (cursor.moveToNext()){
            routes.add(getRouteFromCursor(cursor));
        }
        db.close();
        return routes;
    }

    public List<Peak> getAllPeaks(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM " + TABLE_PEAKS;
        Cursor c = db.rawQuery(sqlStatement, null);
        List<Peak> peakList = new ArrayList<>();
        while (c.moveToNext()){
            peakList.add(new Peak(c.getString(0), c.getString(2), c.getDouble(3), c.getDouble(4),  c.getString(5)));
        }
        c.close();
        db.close();
        return peakList;
    }


}
