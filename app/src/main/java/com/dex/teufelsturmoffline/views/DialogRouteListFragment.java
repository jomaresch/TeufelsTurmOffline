package com.dex.teufelsturmoffline.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.activities.CommentActivity;
import com.dex.teufelsturmoffline.adapter.RouteRecycleAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.model.Route;

import java.util.List;

public class DialogRouteListFragment extends DialogFragment implements View.OnClickListener {

    String peak = "";

    RecyclerView recyclerView;
    DatabaseHelper db;
    RouteRecycleAdapter routeRecycleAdapter;
    Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        peak = args.getString("NAME");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_route_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_routes_map_list);

        db = new DatabaseHelper(getContext());
        List<Route> routeList = db.getPeakRoutes(peak);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        routeRecycleAdapter = new RouteRecycleAdapter(getActivity(), routeList, new RouteRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route item) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("ID", item.getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(routeRecycleAdapter);

        backButton = view.findViewById(R.id.button_back_route_dialog);
        backButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View view) {
        this.getDialog().dismiss();
    }
}
