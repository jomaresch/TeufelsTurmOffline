package com.dex.teufelsturmoffline.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.adapter.CommentRecycleAdapter;
import com.dex.teufelsturmoffline.adapter.RouteRecycleAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.Comment;
import com.dex.teufelsturmoffline.model.Route;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    DatabaseHelper db;
    CommentRecycleAdapter commentRecycleAdapter;
    TextView area, last_update, scale;
    RecyclerView recyclerView;
    Route route;
    List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        this.route = db.getRouteById(id);
        this.commentList = db.getCommentByRoute(id);
        this.recyclerView = findViewById(R.id.recycler_list_comment);

        setTitle(route.getName());

        area = findViewById(R.id.text_comment_area_mountain);
        area.setText("Fels: "+ route.getMountain()+ "\nGebiet: "+ route.getArea());
        scale = findViewById(R.id.text_comment_scale);
        scale.setText("Skala: " + route.getScale());
        last_update = findViewById(R.id.text_comment_last_update);
        last_update.setText("Letztes Update:\n"+ route.getDate());


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
                this.getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);

        commentRecycleAdapter = new CommentRecycleAdapter(
                this,
                commentList,
                new CommentRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comment item) {
                Toast.makeText(getApplicationContext(), "OK" + item.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(commentRecycleAdapter);
    }
}
