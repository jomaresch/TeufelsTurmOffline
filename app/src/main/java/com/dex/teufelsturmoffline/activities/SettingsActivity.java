package com.dex.teufelsturmoffline.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.network.HttpTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    private HttpTask httpTask;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button button = findViewById(R.id.button_settings_run);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(editText.getText().toString());
                httpTask = new HttpTask(SettingsActivity.this);
                httpTask.execute(number);
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
        if(httpTask.getStatus() == AsyncTask.Status.RUNNING)
            httpTask.cancel(false);
    }
}
