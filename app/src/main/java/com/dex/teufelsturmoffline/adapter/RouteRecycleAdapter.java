package com.dex.teufelsturmoffline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.model.ModelHelper;
import com.dex.teufelsturmoffline.model.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Route item);
    }

    private final int MOUNTAIN = 0;
    private final int ROUTE = 1;

    private List<Pair<Integer,Route>> filteredRouteList;
    private List<Route> originalRouteList;
    private OnItemClickListener listener;
    private Context context;

    public RouteRecycleAdapter(Context context, List<Route> filteredRouteList, OnItemClickListener listener) {
        this.originalRouteList = filteredRouteList;
        this.filteredRouteList = convertViewList(filteredRouteList);
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return filteredRouteList.get(position).first;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case MOUNTAIN:{
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_mountain, viewGroup, false);
                return new MountainRecycleViewHolder(view);
            }
            case ROUTE:{
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_route_list, viewGroup, false);
                return new RouteRecycleViewHolder(view);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()){
            case MOUNTAIN:{
                MountainRecycleViewHolder mvh = (MountainRecycleViewHolder)viewHolder;
                mvh.mountainName.setText(filteredRouteList.get(i).second.getMountain());
                break;
            }
            case ROUTE:{
                RouteRecycleViewHolder rvh = (RouteRecycleViewHolder)viewHolder;
                rvh.routeName.setText(filteredRouteList.get(i).second.getName());
                rvh.routeScale.setText("Skala: "+ filteredRouteList.get(i).second.getScale());
                rvh.routeMountain.setText( "Gipfel: "+ filteredRouteList.get(i).second.getMountain());
                rvh.imageRating.setImageResource(ModelHelper.convertRatingToImage(filteredRouteList.get(i).second.getRating()));
                if(filteredRouteList.get(i).second.getDone() == 1){
                    rvh.imageDone.setImageResource(R.drawable.check_solid);
                } else {
                    rvh.imageDone.setImageResource(android.R.color.transparent);
                }
                rvh.bind(filteredRouteList.get(i).second,listener);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return filteredRouteList.size();
    }

    class RouteRecycleViewHolder extends RecyclerView.ViewHolder {

        TextView routeName, routeScale, routeMountain;
        ImageView imageRating, imageDone;

        public RouteRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.text_comment_row_user_name);
            routeScale = itemView.findViewById(R.id.text_route_row_scale);
            routeMountain = itemView.findViewById(R.id.text_comment_row_date);
            imageDone = itemView.findViewById(R.id.image_route_row_done);
            imageRating = itemView.findViewById(R.id.image_route_rating_row_route);
        }

        public void bind(final Route item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    class MountainRecycleViewHolder extends RecyclerView.ViewHolder {

        TextView mountainName;

        public MountainRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            mountainName = itemView.findViewById(R.id.text_row_mountain);
        }
    }

    public void updateData(List<Route> newList){
        this.originalRouteList = newList;
        this.filteredRouteList = convertViewList(newList);
        notifyDataSetChanged();
    }

    public void resetData(){
        this.filteredRouteList = convertViewList(this.originalRouteList);
        notifyDataSetChanged();
    }

    public void filterData(String filter){
        filter = filter.toLowerCase();
        List<Route> tempList = new ArrayList<>();
        for (Route route : this.originalRouteList){
            if (route.getName().toLowerCase().contains(filter) ||
                    route.getMountain().toLowerCase().contains(filter)){
                tempList.add(route);
            }
        }
        this.filteredRouteList = convertViewList(tempList);
        notifyDataSetChanged();
    }

    private List<Pair<Integer,Route>> convertViewList (List<Route> list){
        Map<String,List<Route>> map = new HashMap<>();
        List<Pair<Integer,Route>> resultList = new ArrayList<>();
        for(Route route: list){
            if(map.containsKey(route.getMountain())){
                map.get(route.getMountain()).add(route);
            } else {
                List<Route> sublist = new ArrayList<>();
                sublist.add(route);
                map.put(route.getMountain(),sublist);
            }
        }
        List<String> keys = new ArrayList<>();
        keys.addAll(map.keySet());
        Collections.sort(keys);
        for(String mountain: keys){
            Route fakeRoute = new Route();
            fakeRoute.setMountain(mountain);
            resultList.add(new Pair<>(MOUNTAIN, fakeRoute));
            for(Route route: map.get(mountain)){
                resultList.add(new Pair<>(ROUTE, route));
            }
        }
        return resultList;
    }

}
