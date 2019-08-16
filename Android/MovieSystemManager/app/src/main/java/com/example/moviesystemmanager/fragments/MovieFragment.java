package com.example.moviesystemmanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.activities.Movie_2;
import com.example.moviesystemmanager.adapters.MovieAdapter;
import com.example.moviesystemmanager.bean.Movie;
import com.example.moviesystemmanager.bean.item.MovieItem;
import com.example.moviesystemmanager.server.client.MovieClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment  extends Fragment {
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;
    private View myView;

    private EditText movieName;
    private Button search;
    private Button newmovie;
    private RadioGroup tabBar;
    private RadioButton today;
    private RadioButton more;
    private ListView listView;
    private MovieAdapter listAdapternew;
    private List<MovieItem> listItemList;
    private RefreshLayout refreshLayout;

    //分页查询页码
    private int page;
    private List<Movie> movieList;
    private int radioStatus;
    private List<Movie> listforloading;

    //异步更新
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(movieList==null){
                        return ;
                    }
                    List<MovieItem> itemList = MovieItem.getItemList(movieList);
                    setList(itemList);
                    break;
                }
                case 1:{
                    if(listforloading==null){
                        return ;
                    }
                    List<MovieItem> itemList = MovieItem.getItemList(listforloading);
                    movieList.addAll(listforloading);
                    updateList(itemList);
                    break;
                }
            }
        }
    };



    public MovieFragment() {
    }

    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = 0;
            radioStatus = 1;
        }
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        movieList = new ArrayList<Movie>();
        listforloading = new ArrayList<Movie>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.moviefragment, container, false);
        movieName = myView.findViewById(R.id.movie_name_text);
        search = myView.findViewById(R.id.search);
        newmovie = myView.findViewById(R.id.newmovie);
        tabBar = myView.findViewById(R.id.tab_bar);
        today = myView.findViewById(R.id.tab_item_today);
        more = myView.findViewById(R.id.tab_item_more);
        listView = myView.findViewById(R.id.list_new);
        refreshLayout = myView.findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                listforloading.clear();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=1;
                        listforloading = MovieClient.getMovies(getContext(),  movieName.getText().toString(),3+radioStatus, ++page);
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });
        newmovie.setOnClickListener(new ButtonListener());
        search.setOnClickListener(new ButtonListener());
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie temp = movieList.get(position);
                        Intent intent = new Intent(getActivity(), Movie_2.class);
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");

                        intent.putExtra("movieId",temp.getMovieId());
                        intent.putExtra("movieName",temp.getMovieName());
                        intent.putExtra("doubanId",temp.getMovieDoubanid());
                        intent.putExtra("onlineTime",sdf.format(temp.getMovieOnlinetime()));
                        intent.putExtra("offlineTime",sdf.format(temp.getMovieOfflinetime()));

                        intent.putExtra("introduction",temp.getMovieIntroduction());
                        intent.putExtra("duration",String.valueOf(temp.getMovieDuration()));
                        intent.putExtra("status",String.valueOf(temp.getMovieStatus()));
                        startActivity(intent);
                    }
                }
        );
        tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemId) {
                switch (itemId) {
                    case R.id.tab_item_today:{
                        page=0;
                        radioStatus = 1;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                movieList.clear();
                                Message message = Message.obtain();
                                message.what=0;
                                movieList = MovieClient.getMovies(getContext(), movieName.getText().toString(),4,page);
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    }
                    case R.id.tab_item_more:{
                        page=0;
                        radioStatus = 2;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                movieList.clear();
                                Message message = Message.obtain();
                                message.what=0;
                                movieList = MovieClient.getMovies(getContext(), "",5,page);
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    }
                }
            }
        });
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    //貌似没什么用
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void setList(List<MovieItem> itemList){
        listItemList = itemList;
        listAdapternew = new MovieAdapter(getActivity(), R.layout.movie_list, listItemList);
        listView.setAdapter(listAdapternew);
    }
    public void updateList(List<MovieItem> itemList){
        listItemList.addAll(itemList);
        listAdapternew.notifyDataSetChanged();
    }

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search: {
                    String movieNameStr = movieName.getText().toString();
                    page=0;
                    if(movieNameStr!=null&&!movieNameStr.equals("")){
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                movieList.clear();
                                movieList.addAll(MovieClient.getMovies(getContext(),  movieName.getText().toString(),4,page));
                                Message message = Message.obtain();
                                message.what=0;
                                handler.sendMessage(message);
                            }
                        }).start();
//                        movieList.add(new Movie("001300012019","123","追龙2","12324214",new Date(Long.valueOf("1560476476763")),new Date(Long.valueOf("1560476476763")),128,2,"追龙2简介"));
                    }
                    break;
                }
                case R.id.newmovie: {
                    Intent intent = new Intent(getActivity(), Movie_2.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

}
