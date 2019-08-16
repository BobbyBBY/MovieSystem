package com.example.moviesystemclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.item.TicketItem;

import java.util.List;

public class TicketAdapter extends ArrayAdapter<TicketItem> {
    private int newResourceId;
    public TicketAdapter(Context context, int resourceId, List<TicketItem> cityList){
        super(context, resourceId, cityList);
        newResourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        TicketItem listItem = getItem (position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView name = view.findViewById (R.id.moviename);
        TextView starttime = view.findViewById (R.id.starttime);
        TextView endtime = view.findViewById (R.id.endtime);
        TextView se = view.findViewById (R.id.se);
        TextView sr = view.findViewById (R.id.sr);
        TextView price = view.findViewById (R.id.price);
        TextView seat = view.findViewById (R.id.seat);
        TextView status = view.findViewById (R.id.status);

        name.setText (listItem.getName());
        starttime.setText (listItem.getStartime());
        endtime.setText (listItem.getEndtime());
        se.setText (listItem.getSe());
        sr.setText (listItem.getSr());
        price.setText (listItem.getPrice());
        seat.setText (listItem.getSeat());
        status.setText (listItem.getStatus());
        return view;
    }


}
