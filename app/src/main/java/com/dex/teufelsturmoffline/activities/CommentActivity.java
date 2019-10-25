package com.dex.teufelsturmoffline.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.adapter.CommentRecycleAdapter;
import com.dex.teufelsturmoffline.adapter.RouteRecycleAdapter;
import com.dex.teufelsturmoffline.adapter.ViewPagerAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.Comment;
import com.dex.teufelsturmoffline.model.Route;
import com.dex.teufelsturmoffline.views.MYCommentFragment;
import com.dex.teufelsturmoffline.views.TTCommentFragment;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView area, last_update, scale;
    Route route;

    ViewPager mViewPager;
    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_fav,menu);
        this.menu = menu;

        setFavorite(route.getFav() == 1, false);
        setDoneIcon(route.getDone() == 1);


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        this.route = db.getRouteById(id);

        setTitle(route.getName());

        area = findViewById(R.id.text_comment_area_mountain);
        area.setText("Fels: "+ route.getMountain()+ "\nGebiet: "+ route.getArea());
        scale = findViewById(R.id.text_comment_scale);
        scale.setText("Skala: " + route.getScale());
        last_update = findViewById(R.id.text_comment_last_update);
        last_update.setText("Letztes Update:\n"+ route.getDate());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager_comment);

        setupViewPager(mViewPager, id);

        TabLayout tabLayout = findViewById(R.id.tab_layout_comment);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager mViewPager, String id) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        TTCommentFragment ttCommentFragment = new TTCommentFragment();
        MYCommentFragment myCommentFragment = new MYCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID",id);
        ttCommentFragment.setArguments(bundle);
        myCommentFragment.setArguments(bundle);

        adapter.addFragment(ttCommentFragment, "TT INFO");
        adapter.addFragment(myCommentFragment, "MY INFO");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.fav_button){
            setFavorite(!item.isChecked(),true);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setFavorite(boolean favorite, boolean withToastAndUpdate){
        if (favorite){
            menu.findItem(R.id.fav_button).setChecked(true);
            menu.findItem(R.id.fav_button).setIcon(R.drawable.ic_favorite_blk);

            if (withToastAndUpdate) {
                db.setFavorite(favorite,route.getId());
                Toast.makeText(this,"Der Weg wurde als Favorit hinzugefügt", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            menu.findItem(R.id.fav_button).setChecked(false);
            menu.findItem(R.id.fav_button).setIcon(R.drawable.ic_favorite_border_blk);
            if (withToastAndUpdate) {
                db.setFavorite(favorite,route.getId());
                Toast.makeText(this,"Der Weg wurde als Favorit entfernt", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setDoneIcon(boolean done){
        if(done) {
            this.menu.add(0, 1, Menu.NONE, "TEST").setIcon(R.drawable.check_solid).setShowAsAction(1);
            this.db.setDone(true, this.route.getId());
        }
        else {
            this.menu.removeItem(1);
            this.db.setDone(false, this.route.getId());
        }
    }

    public boolean getDone(){
        return route.getDone() == 1;
    }
}
