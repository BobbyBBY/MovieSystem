package com.example.moviesystemclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.item.OrderItem;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderItem> {
    private int newResourceId;
    public OrderAdapter(Context context, int resourceId, List<OrderItem> cityList){
        super(context, resourceId, cityList);
        newResourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        OrderItem listItem = getItem (position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView phone = view.findViewById (R.id.phone);
        TextView name = view.findViewById (R.id.moviename);
        TextView time = view.findViewById (R.id.time);
        TextView price = view.findViewById (R.id.price);
        TextView count = view.findViewById (R.id.count);
        TextView status = view.findViewById (R.id.status);

        phone.setText (listItem.getPhone());
        name.setText (listItem.getName());
        time.setText (listItem.getTime());
        price.setText (listItem.getPrice());
        count.setText (listItem.getCount());
        status.setText (listItem.getStatus());
        return view;
    }
}
