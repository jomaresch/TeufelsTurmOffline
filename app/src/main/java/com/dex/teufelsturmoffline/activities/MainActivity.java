package com.dex.teufelsturmoffline.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.adapter.SpinnerAreaAdapter;
import com.dex.teufelsturmoffline.adapter.ViewPagerAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.AreaSpinnerData;
import com.dex.teufelsturmoffline.views.DoneViewFragment;
import com.dex.teufelsturmoffline.views.FavoritesViewFragment;
import com.dex.teufelsturmoffline.views.SearchViewFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    SearchViewFragment searchViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
        if(!database.exists()){
            copyDatabase(this);
        }

        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        searchViewFragment = new SearchViewFragment();
        adapter.addFragment(searchViewFragment, getString(R.string.tab_search));
        adapter.addFragment(new FavoritesViewFragment(), getString(R.string.tab_favorites));
        adapter.addFragment(new DoneViewFragment(), getString(R.string.tab_done));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_leg:
                Intent intent2 = new Intent(this, InfoActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void copyDatabase(Context context){
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DB_NAME);

            String fileOutName = "/data/data/com.dex.teufelsturmoffline/databases/"+ DatabaseHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(fileOutName);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "Successfully copy DB ", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(context, "Error while copy DB", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {}

    @Override
    public void onPageSelected(int i) {
        if (i == 0){
            searchViewFragment.resetRouteList();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {}
}
