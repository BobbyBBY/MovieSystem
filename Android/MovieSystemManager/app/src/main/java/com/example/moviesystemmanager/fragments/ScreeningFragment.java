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
import android.widget.ListView;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.activities.Screening_2;
import com.example.moviesystemmanager.adapters.MovieAdapter;
import com.example.moviesystemmanager.bean.Movie;
import com.example.moviesystemmanager.bean.item.MovieItem;
import com.example.moviesystemmanager.server.client.MovieClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ScreeningFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View myView;

    private ListView listView;
    private RefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private MovieAdapter listAdapternew;
    private List<MovieItem> listItemList;

    //分页查询页码
    private int page;
    private List<Movie> movieList;
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



    public ScreeningFragment() {
    }

    public static ScreeningFragment newInstance() {
        ScreeningFragment fragment = new ScreeningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = 0;
        }
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        movieList = new ArrayList<Movie>();
        listforloading = new ArrayList<Movie>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.screeningfragment, container, false);
        listView = myView.findViewById(R.id.list_new);
        refreshLayout = myView.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=0;
                        page = 0;
                        movieList = MovieClient.getMovies(getContext(),  "",4, page);
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=1;
                        listforloading = MovieClient.getMovies(getContext(),  "",4, ++page);
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie temp = movieList.get(position);
                        Intent intent = new Intent(getActivity(), Screening_2.class);
                        intent.putExtra("movieId",temp.getMovieId());
                        startActivity(intent);
                    }
                }
        );


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





}
