package com.dex.teufelsturmoffline.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.network.HttpTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    private HttpTask httpTask;
    private final int NUMBER_ROUTES = 100;
    private ImageView infoIcon;
    private TextView infoTextIcon;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Einstellungen");
        setContentView(R.layout.activity_settings);
        db = new DatabaseHelper(this);
        String lastComments = db.getLastCommentDate();
        String countComments = db.countComments();
        String countRoutes = db.countRoutes();
        String countMountain = db.countMountain();

        infoIcon = findViewById(R.id.icon_settings_info);
        infoTextIcon = findViewById(R.id.text_settings_info);

        TextView infoText = findViewById(R.id.text_setting_db_info);
        infoText.setText("Anzahl der Gipfel: " + countMountain +
        "\nAnzahl der Routen: "+ countRoutes +
        "\nAnzahl der Kommentare: "+ countComments +
        "\nLetztes Kommentar vom: " + lastComments);

        Button button = findViewById(R.id.button_settings_run);
        Button buttonDel = findViewById(R.id.button_del_db);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpTask = new HttpTask(SettingsActivity.this);
                httpTask.execute(NUMBER_ROUTES);
            }
        });

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File database = getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
                database.delete();
            }
        });

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        infoTextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        Button button1 = findViewById(R.id.button_settings_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpTask.cancel(false);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(httpTask == null){
            return;
        }
        if(httpTask.getStatus() == AsyncTask.Status.RUNNING)
            httpTask.cancel(false);
    }

    private void dialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("INFO");
        alert.setMessage(
                "Sobald du auf STARTEN klickst, verbindet sich die App mit Teufelsturm.de und läd alle neuen Kommentare herunter. " +
                        "Ich empfehle die Datenbank 1-2 mal im Monat zu aktualisieren.\n\n" +
                        "Damit die Seite nicht überlastet wird, wartet die App 3 Sekunden lang zwischen den Anfragen." +
                        "Dies verlängert zwar das Herunterladen, ist aber besser für die Seite :)"
        );
        alert.setPositiveButton("OK",null);
        alert.show();
    }
}
