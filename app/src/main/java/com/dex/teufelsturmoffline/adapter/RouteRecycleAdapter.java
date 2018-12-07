package com.dex.teufelsturmoffline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.model.ModelHelper;
import com.dex.teufelsturmoffline.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteRecycleAdapter extends RecyclerView.Adapter<RouteRecycleAdapter.RouteRecycleViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Route item);
    }

    private List<Route> filteredRouteList;
    private List<Route> originalRouteList;
    private OnItemClickListener listener;
    private Context context;

    public RouteRecycleAdapter(Context context, List<Route> filteredRouteList, OnItemClickListener listener) {
        this.filteredRouteList = filteredRouteList;
        this.originalRouteList = originalRouteList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_route_list, viewGroup, false);
        return new RouteRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteRecycleViewHolder viewHolder, int i) {
        viewHolder.routeName.setText(filteredRouteList.get(i).getName());
        viewHolder.routeScale.setText("Skala: "+ filteredRouteList.get(i).getScale());
        viewHolder.routeMountain.setText( "Gipfel: "+ filteredRouteList.get(i).getMountain());
        viewHolder.imageRating.setImageResource(ModelHelper.convertRatingToImage(filteredRouteList.get(i).getRating()));
        if(filteredRouteList.get(i).getFav() == 1){
            viewHolder.imageFav.setImageResource(R.drawable.ic_favorite_blk);
        } else {
            viewHolder.imageFav.setImageResource(R.drawable.ic_favorite_border_blk);
        }
        viewHolder.bind(filteredRouteList.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return filteredRouteList.size();
    }

    class RouteRecycleViewHolder extends RecyclerView.ViewHolder {

        TextView routeName, routeScale, routeMountain;
        ImageView imageRating, imageFav;

        public RouteRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.text_comment_row_user_name);
            routeScale = itemView.findViewById(R.id.text_route_row_scale);
            routeMountain = itemView.findViewById(R.id.text_comment_row_date);
            imageFav = itemView.findViewById(R.id.image_route_row_fav);
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

    public void updateData(List<Route> newList){
        this.originalRouteList = newList;
        this.filteredRouteList = newList;
        notifyDataSetChanged();
    }

    public void resetData(){
        this.filteredRouteList = this.originalRouteList;
        notifyDataSetChanged();
    }

    public void filterData(String filter){
        filter = filter.toLowerCase();
        this.filteredRouteList = new ArrayList<>();
        for (Route route : this.originalRouteList){
            if (route.getName().toLowerCase().contains(filter) ||
                    route.getMountain().toLowerCase().contains(filter)){
                filteredRouteList.add(route);
            }
        }
        notifyDataSetChanged();
    }

}
