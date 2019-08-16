package com.example.moviesystemclient.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.item.ScreeningItem;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.client.PasswordClient;
import com.example.moviesystemclient.server.client.ScreeningClient;

import java.util.List;


public class MineFragment extends Fragment {
    private MineFragment.OnFragmentInteractionListener mListener;
    private View myView;

    private EditText phone;
    private EditText phoneCode;
    private Button save;
    private Button phoneCodeBtn;
    private TextView loginPhone;
    private SharedPreferences sharedPreferences;

    private int saveResult;
    private int phoneCodeResult;

    public MineFragment() {
    }
    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(saveResult==1){
//                        Looper.prepare();
                        Toast.makeText(getContext(), "注册/登陆成功",Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                    }
                    else{
//                        Looper.prepare();
                        Toast.makeText(getContext(), "请重试",Toast.LENGTH_SHORT).show();
//                        Looper.loop();

                    }
                    break;
                }
                case 1:{
                    if(phoneCodeResult==1){
//                        Looper.prepare();
                        Toast.makeText(getContext(), "已发送验证码",Toast.LENGTH_SHORT).show();
//                        Looper.loop();

                    }
                    else{
//                        Looper.prepare();
                        Toast.makeText(getContext(), "请30秒后重试",Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                    }
                    break;
                }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveResult=0;
        phoneCodeResult=0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.minefragment, container, false);
        phone = myView.findViewById(R.id.phone);
        phoneCode = myView.findViewById(R.id.vn);
        save = myView.findViewById(R.id.save);
        phoneCodeBtn = myView.findViewById(R.id.phonecode);
        loginPhone = myView.findViewById(R.id.loginphone);

        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String phonetemo = sharedPreferences.getString("phone","");
        if(phonetemo!=null&&!phonetemo.equals("")){
            loginPhone.setText("当前登陆的手机号是： "+PhoneManagement.getPhone());
        }
        save.setOnClickListener(new ButtonListener());
        phoneCodeBtn.setOnClickListener(new ButtonListener());
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MineFragment.OnFragmentInteractionListener) {
            mListener = (MineFragment.OnFragmentInteractionListener) context;
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

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save: {
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.what=0;
                            saveResult = PasswordClient.register(getContext(),phoneCode.getText().toString(),phone.getText().toString());
                            handler.sendMessage(message);
                        }
                    }).start();
                    break;
                }
                case R.id.phonecode: {
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.what=1;
                            phoneCodeResult = PasswordClient.getPhoneCode(getContext(),phone.getText().toString());
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            loginPhone.setEnabled(false);
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loginPhone.setEnabled(true);
                        }
                    }).start();
                    break;
                }
            }
        }
    }
}
