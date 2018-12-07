package com.dex.teufelsturmoffline.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.activities.CommentActivity;
import com.dex.teufelsturmoffline.adapter.RouteRecycleAdapter;
import com.dex.teufelsturmoffline.model.Route;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewFragment extends Fragment {

    RecyclerView recyclerView;
    View view;

    public FavoritesViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_routes);

        List<Route> routeList = new ArrayList<>();

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        RouteRecycleAdapter routeRecycleAdapter = new RouteRecycleAdapter(getActivity(), routeList, new RouteRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route item) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("ID", item.getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(routeRecycleAdapter);
        return view;
    }
}
