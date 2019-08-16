package com.example.moviesystemclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.item.ScreeningItem;

import java.util.List;

public class ScreeningAdapter extends ArrayAdapter<ScreeningItem> {
    private int newResourceId;
    public ScreeningAdapter(Context context, int resourceId, List<ScreeningItem> cityList){
        super(context, resourceId, cityList);
        newResourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        ScreeningItem listItem = getItem (position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView name = view.findViewById (R.id.moviename);
        TextView starttime = view.findViewById (R.id.starttime);
        TextView endtime = view.findViewById (R.id.endtime);
        TextView se = view.findViewById (R.id.se);
        TextView sr = view.findViewById (R.id.sr);
        TextView price = view.findViewById (R.id.price);

        name.setText (listItem.getName());
        starttime.setText (listItem.getStartime());
        endtime.setText (listItem.getEndtime());
        se.setText (listItem.getSe());
        sr.setText (listItem.getSr());
        price.setText (listItem.getPrice());
        return view;
    }


}


