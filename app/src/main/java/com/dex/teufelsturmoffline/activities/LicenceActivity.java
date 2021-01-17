package com.dex.teufelsturmoffline.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.database.SettingsSaver;

import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.AccessController.getContext;

public class LicenceActivity extends AppCompatActivity implements View.OnClickListener {

    private String android_id = "";
    private Button buttonCopyId, buttonApply;
    private EditText licenceInput;
    private TextView deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
        android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);

        String shouldKey = md5(android_id);
        if (shouldKey.equals(SettingsSaver.getKey(this))){
            openMainActivity();
        }

        buttonCopyId = findViewById(R.id.button_copy_device_id);
        buttonCopyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ID", android_id);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        licenceInput = findViewById(R.id.edit_licence_key);
        deviceId = findViewById(R.id.text_device_key_label);
        buttonApply = findViewById(R.id.button_apply_licence_key);

        deviceId.setText(android_id);
        buttonApply.setOnClickListener(this);

    }

    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            String k = hexString.toString();
            return k.substring(0,3) + k.substring(k.length()-3);

        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this,"Algorithm not found", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        buttonApply.setEnabled(false);
        String shouldKey = md5(android_id);
        if(licenceInput.getText().toString().equals(shouldKey)){
            SettingsSaver.setKey(this,shouldKey );
            openMainActivity();
        }
        else {
            Toast.makeText(this,"Wrong Key", Toast.LENGTH_SHORT).show();
        }
        buttonApply.setEnabled(true);
    }
}
