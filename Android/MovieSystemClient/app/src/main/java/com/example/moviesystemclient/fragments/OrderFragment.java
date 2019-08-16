package com.example.moviesystemclient.fragments;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviesystemclient.R;
import com.example.moviesystemclient.activities.OrderActivity;
import com.example.moviesystemclient.activities.TicketActivity;
import com.example.moviesystemclient.adapters.OrderAdapter;
import com.example.moviesystemclient.bean.OrderView;
import com.example.moviesystemclient.bean.item.MovieItem;
import com.example.moviesystemclient.bean.item.OrderItem;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.client.AudienceClient;
import com.example.moviesystemclient.server.client.MovieClient;
import com.example.moviesystemclient.server.client.OrderClient;
import com.example.moviesystemclient.server.client.SeatClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;
    private View myView;

    private RadioGroup tabBar;
    private RadioButton taken;
    private RadioButton unpaid;
    private RadioButton canceled;
    private RadioButton untaken;
    private ListView listView;
    private List<OrderItem> listItemList;
    private OrderAdapter listAdapternew;
    private RefreshLayout refreshLayout;

    private int radioStatus;
    private boolean login;
    private int page;
    private List<OrderView> orderList;
    private List<OrderView> listforloading;

    public OrderFragment() {
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(orderList==null){
                        return ;
                    }
                    List<OrderItem> itemList = OrderItem.getItemList(orderList);
                    setList(itemList);
                    break;
                }
                case 1:{
                    if(listforloading==null){
                        return ;
                    }
                    List<OrderItem> itemList = OrderItem.getItemList(listforloading);
                    orderList.addAll(listforloading);
                    updateList(itemList);
                    break;
                }
            }
        }
    };


    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            radioStatus=1;
            login = false;
        }
        listforloading = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        new Thread(new Runnable(){
            @Override
            public void run() {
                if(AudienceClient.getAudience(getContext())==null){
                    login=false;
                }
                else{
                    login = true;
                }
            }
        }).start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.orderfragment, container, false);
        tabBar = myView.findViewById(R.id.tab_bar);
        untaken = myView.findViewById(R.id.tab_item_untaken);
        taken = myView.findViewById(R.id.tab_item_taken);
        unpaid = myView.findViewById(R.id.tab_item_unpaid);
        canceled = myView.findViewById(R.id.tab_item_canceled);
        listView = myView.findViewById(R.id.list_new);
        refreshLayout = myView.findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲
        if(refreshLayout!=null){
            refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                    listforloading.clear();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.what=1;
                            int status = 0;
                            if(radioStatus<3){
                                status = radioStatus+1;
                            }
                            else if(radioStatus==3){
                                status = 1;
                            }
                            else{
                                status = 4;
                            }
                            orderList.addAll( OrderClient.getOrders(getContext(), "",PhoneManagement.getPhone(),status,++page));
                            handler.sendMessage(message);
                        }
                    }).start();

                }
            });
        }
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(radioStatus==1){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("orderId", orderList.get(position).getOrderId());
                            editor.commit();
                            Intent intent = new Intent(getActivity(), TicketActivity.class);
                            startActivity(intent);
                        }
                        else if(radioStatus==3){
                            Intent intent = new Intent(getActivity(), OrderActivity.class);
                            OrderView temp = orderList.get(position);
                            intent.putExtra("ticketCount", temp.getOrderTicketcount());
                            intent.putExtra("totalPrice", temp.getOrderTotalprice());
                            intent.putExtra("movieName", temp.getMovieName());
                            intent.putExtra("orderId", temp.getOrderId());
                            startActivity(intent);
                        }

                    }
                }
        );
        tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemId) {
                switch (itemId) {
                    case R.id.tab_item_untaken:{

                        radioStatus=1;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                orderList = OrderClient.getOrders(getContext(), "",PhoneManagement.getPhone(),2,page);
                                handler.sendMessage(message);

                            }
                        }).start();
                       break;
                    }
                    case R.id.tab_item_taken:{
                        radioStatus=2;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                orderList = OrderClient.getOrders(getContext(), "",PhoneManagement.getPhone(),3,page);
                                handler.sendMessage(message);

                            }
                        }).start();
                        break;
                    }
                    case R.id.tab_item_unpaid:{
                        radioStatus=3;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                orderList = OrderClient.getOrders(getContext(), "",PhoneManagement.getPhone(),1,page);
                                handler.sendMessage(message);

                            }
                        }).start();
                        break;
                    }
                    case R.id.tab_item_canceled:{
//                        radioStatus=4;
//                        page=0;
//                        phoneNumber=sharedPreferences.getString("phoneNumber","");
//                        if(phoneNumber==null||phoneNumber.equals("")){
//                            Toast.makeText(getContext(), "还没有登陆",Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//                        new Thread(new Runnable(){
//                            @Override
//                            public void run() {
//                                Message message = Message.obtain();
//                                message.what=0;
//                                orderList = OrderClient.getOrders(getContext(), "",phoneNumber,radioStatus,page);
//                                handler.sendMessage(message);
//
//                            }
//                        }).start();
                        break;
                    }
                }
            }
        });
        return myView;
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

    public void setList(List<OrderItem> itemList){
        listItemList = itemList;
        listAdapternew = new OrderAdapter(getActivity(), R.layout.order_list, listItemList);
        listView.setAdapter(listAdapternew);
    }
    public void updateList(List<OrderItem> itemList){
        listItemList.addAll(itemList);
        listAdapternew.notifyDataSetChanged();
    }

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }
}
