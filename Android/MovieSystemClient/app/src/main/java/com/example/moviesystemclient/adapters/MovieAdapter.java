package com.example.moviesystemclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.item.MovieItem;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<MovieItem> {

    private int newResourceId;
    public MovieAdapter(Context context, int resourceId, List<MovieItem> cityList){
        super(context, resourceId, cityList);
        newResourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        MovieItem listItem = getItem (position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView name = view.findViewById (R.id.name_text);
        TextView time = view.findViewById (R.id.name_duration);
        TextView introduction = view.findViewById (R.id.name_introduction);

        name.setText (listItem.getName());
        time.setText (listItem.getTime());
        introduction.setText (listItem.getIntroduction());
        return view;
    }


}
