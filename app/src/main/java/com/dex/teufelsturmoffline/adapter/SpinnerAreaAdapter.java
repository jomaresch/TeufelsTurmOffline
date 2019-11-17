package com.dex.teufelsturmoffline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.model.AreaSpinnerData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAreaAdapter extends ArrayAdapter<AreaSpinnerData> {

    private Context context;
    private List<AreaSpinnerData> dataList;

    public SpinnerAreaAdapter(Context context, int resource, List<AreaSpinnerData> objects) {
        super(context, resource, objects);


        this.context = context;
        this.dataList = objects;
    }


    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.row_spinner_area,parent,false);
        TextView textView = customView.findViewById(R.id.text_row_spinner_area_area);
        TextView textViewRoutes = customView.findViewById(R.id.text_row_spinner_area_routes);
        textView.setText(dataList.get(position).getArea());
        textViewRoutes.setText("Wege: " + String.valueOf(dataList.get(position).getRouteCount()));
        return customView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.row_spinner_area_item,parent,false);
        TextView textView = customView.findViewById(R.id.text_spinner_item_area);
        TextView textViewRoutes = customView.findViewById(R.id.text_spinner_item_amount);
        textView.setText(dataList.get(position).getArea());
        textViewRoutes.setText("Wege: " + String.valueOf(dataList.get(position).getRouteCount()));
        return customView;
    }



}
