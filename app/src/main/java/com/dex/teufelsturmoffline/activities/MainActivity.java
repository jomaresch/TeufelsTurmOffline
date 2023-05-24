package com.dex.teufelsturmoffline.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimingLogger;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.adapter.ViewPagerAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.database.DatabaseManager;
import com.dex.teufelsturmoffline.views.DoneViewFragment;
import com.dex.teufelsturmoffline.views.FavoritesViewFragment;
import com.dex.teufelsturmoffline.views.MapViewFragment;
import com.dex.teufelsturmoffline.views.SearchViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    DatabaseManager databaseManager;
    BottomNavigationView bottomNavigation;
    ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);

        databaseManager.createDirectories();
        if (!databaseManager.doDbExists())
            databaseManager.copyDatabase();
        if (!databaseManager.doMapExists())
            databaseManager.copyMap();
        DatabaseHelper dbh = new DatabaseHelper(this);
        dbh.checkDBUpgrade();

        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation_view);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        fragmentList.add(new MapViewFragment());
        fragmentList.add(new SearchViewFragment());
        fragmentList.add(new FavoritesViewFragment());
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

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_page_map: {
                openFragment(fragmentList.get(0));
                return true;
            }
            case R.id.menu_page_routes: {
                openFragment(fragmentList.get(1));
                return true;
            }
            case R.id.menu_page_book: {
                openFragment(fragmentList.get(2));
                return true;
            }
        }
        return false;
    }
}
