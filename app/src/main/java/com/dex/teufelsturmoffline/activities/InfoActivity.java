package com.dex.teufelsturmoffline.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dex.teufelsturmoffline.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Info");
        setContentView(R.layout.activity_info);
    }
}
