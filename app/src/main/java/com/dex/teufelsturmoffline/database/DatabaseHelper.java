package com.dex.teufelsturmoffline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;


import com.dex.teufelsturmoffline.model.Comment;
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


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Route> getRoutesByArea(String area){

        List routeList = new ArrayList<Route>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM "+ TABLE_ROUTES + " WHERE AREA == '" + area +"'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while(cursor.moveToNext()) {
            Route route = getRouteFromCursor (cursor);
            routeList.add(route);
        }
        db.close();
        return routeList;
    }

    public List<Pair<String, Integer>> getAreas(){
        List areaList = new ArrayList<Pair<String, Integer>>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT DISTINCT AREA, COUNT(AREA) FROM "+ TABLE_ROUTES + " GROUP BY AREA";
        Cursor cursor = db.rawQuery(sqlStatement, null);
        while(cursor.moveToNext()) {
            areaList.add(new Pair<>(
                    cursor.getString(0),
                    cursor.getInt(1))
            );
        }
        db.close();
        return areaList;
    }

    public List<Comment> getCommentByRoute(String id){

        List commentList = new ArrayList<Comment>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM "+ TABLE_COMMENTS + " WHERE ROUTE_ID == '" + id + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        while(cursor.moveToNext()) {
            Comment comment = getCommentFromCursor(cursor);
            commentList.add(comment);
        }

        db.close();
        return commentList;
    }

    public boolean addRoute(Route route){
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
        long result = db.insert(TABLE_ROUTES, null, contentValues );
        db.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateRoute(Route route){
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
        long result = db.update(TABLE_ROUTES, contentValues, "ID == " +route.getId(), null);
        db.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addComment(Comment comment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ROUTE_ID", comment.getRouteId());
        contentValues.put("NAME", comment.getName());
        contentValues.put("COMMENT", comment.getComment());
        contentValues.put("DATE", comment.getDate());
        contentValues.put("RATING", comment.getRating());
        long result = db.insert(TABLE_COMMENTS, null, contentValues );
        db.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Route getRouteById(String id){

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement = "SELECT * FROM "+ TABLE_ROUTES + " WHERE ID == " + id;
        Cursor cursor = db.rawQuery(sqlStatement, null);
        Route route = new Route();
        route.setDate("1975.01.01");
        route.setName("");
        if(cursor.moveToFirst()){
            route = getRouteFromCursor(cursor);
        }
        return route;
    }

    public Comment getCommentByNameAndDate(String name, String date, String route){

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStatement =
                "SELECT * FROM "+ TABLE_COMMENTS +
                " WHERE NAME == '" + name + "' " +
                "AND DATE == '" + date + "' " +
                "AND ROUTE_ID == '" + "'";
        Cursor cursor = db.rawQuery(sqlStatement, null);

        Comment comment = new Comment();
        comment.setName("");
        if(cursor.moveToFirst()){
            comment = getCommentFromCursor(cursor);
        }
        db.close();
        return comment;
    }

    private Comment getCommentFromCursor(Cursor cursor){
        return new Comment(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4)
        );
    }

    private Route getRouteFromCursor(Cursor cursor){
        return  new Route(
                cursor.getString(1),
                cursor.getString(0),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6),
                cursor.getInt(7)
        );
    }
}
