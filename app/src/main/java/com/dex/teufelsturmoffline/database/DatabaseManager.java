package com.dex.teufelsturmoffline.database;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseManager {

    Context context;
    final String internalDBPath;
    final String internalFilePath;
    final String mapName = "ss.map";

    public DatabaseManager(Context context) {
        this.context = context;
        internalDBPath = "/data/data/com.dex.teufelsturmoffline/databases/";
        internalFilePath = "/data/data/com.dex.teufelsturmoffline/files/";
    }

    public void copyDatabase(){
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DB_NAME);
            String fileOutName = internalDBPath + DatabaseHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(fileOutName);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Toast.makeText(context, "Successfully copied DB ", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(context, "Error while copy DB", Toast.LENGTH_SHORT).show();
        }
    }

    public void createDirectories(){
        new File(internalDBPath).mkdirs();
        new File(internalFilePath).mkdirs();
    }

    public boolean doDbExists(){
        return new File(internalDBPath + DatabaseHelper.DB_NAME).exists();
    }


    public boolean doMapExists(){
        return new File(internalFilePath + mapName).exists();
    }

    public void copyMap(){
        try {
            InputStream inputStream = context.getAssets().open(mapName);
            String fileOutName = internalFilePath + mapName;
            OutputStream outputStream = new FileOutputStream(fileOutName);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "Successfully copied map ", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(context, "Error while copy map", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void exportDatabase(){

        String fileLocation = internalDBPath + DatabaseHelper.DB_NAME;
        try {
            InputStream in = new FileInputStream(fileLocation);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            OutputStream out = new FileOutputStream(path + "/"+DatabaseHelper.DB_NAME);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            in.close();
            out.close();
            Toast.makeText(context, "Datenbank erfolgreich nach 'Downloads' exportiert", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Datenbank konnte nicht exportiert werden", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Datenbank konnte nicht exportiert werden", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
