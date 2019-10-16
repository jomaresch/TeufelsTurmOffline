package com.dex.teufelsturmoffline.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;


import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.activities.CommentActivity;
import com.dex.teufelsturmoffline.adapter.RouteRecycleAdapter;
import com.dex.teufelsturmoffline.adapter.SpinnerAreaAdapter;
import com.dex.teufelsturmoffline.database.DatabaseHelper;
import com.dex.teufelsturmoffline.database.SettingsSaver;
import com.dex.teufelsturmoffline.model.AreaSpinnerData;
import com.dex.teufelsturmoffline.model.Route;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchViewFragment extends Fragment {
    Spinner spinner_area;
    RecyclerView recyclerView;
    RouteRecycleAdapter routeRecycleAdapter;
    View view;
    DatabaseHelper db;
    SearchView searchView;


    public SearchViewFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new DatabaseHelper(getContext());
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchButton = menu.findItem( R.id.search);
        searchView = (SearchView) searchButton.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals(""))
                    resetRouteList();
                else
                    filterRouteList(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals(""))
                    resetRouteList();
                else
                    filterRouteList(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_routes);
        spinner_area = view.findViewById(R.id.spinner_area);


        List<Pair<String, Integer>> areaPairs = db.getAreas();
        List<AreaSpinnerData> spinnerData = new ArrayList<>();

        for (Pair<String, Integer> pair : areaPairs){
            spinnerData.add(new AreaSpinnerData(pair.first,pair.second));
        }

        SpinnerAreaAdapter spinnerAreaAdapter = new SpinnerAreaAdapter(view.getContext(), R.layout.row_spinner_area, spinnerData);
        spinner_area.setAdapter(spinnerAreaAdapter);
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRouteList(parent.getItemAtPosition(position).toString());
                searchView.onActionViewCollapsed();
                SettingsSaver.setArea(getActivity(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_area.setSelection(SettingsSaver.getArea(getActivity()));

        List<Route> routeList = new ArrayList<>();
        if(spinnerData.size() > 0 ){
            routeList = db.getRoutesByArea(spinnerData.get(0).getArea());
        }
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



        return view;
    }

    public void updateRouteList(String area){
        List<Route> list = db.getRoutesByArea(area);
        Collections.sort(list);
        routeRecycleAdapter.updateData(list);
    }
    public void filterRouteList(String filter){
        routeRecycleAdapter.filterData(filter);
    }
    public void resetRouteList(){
        routeRecycleAdapter.resetData();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.requestFocus();
//        if(db == null){
//            db = new DatabaseHelper(getContext());
//        }
//        routeRecycleAdapter.updateData(db.getRoutesByArea(spinner_area.getSelectedItem().toString()));
    }
}
