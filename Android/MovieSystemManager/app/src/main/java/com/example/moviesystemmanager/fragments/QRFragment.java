package com.example.moviesystemmanager.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.Util.CheckPermissionUtils;
import com.example.moviesystemmanager.activities.MainActivity;
import com.example.moviesystemmanager.activities.Movie_2;
import com.example.moviesystemmanager.activities.MyCaptureActivity;
import com.example.moviesystemmanager.bean.Ordertable;
import com.example.moviesystemmanager.server.client.MovieClient;
import com.example.moviesystemmanager.server.client.OrderClient;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class QRFragment  extends Fragment implements  EasyPermissions.PermissionCallbacks{
    private OnFragmentInteractionListener mListener;
    private View myView;
    private Button qr;

    public QRFragment() {
    }

    public static QRFragment newInstance() {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.qrfragment, container, false);
        qr = myView.findViewById(R.id.qr);
        qr.setOnClickListener(new ButtonListener());
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

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.qr: {
                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                    startActivityForResult(intent, 1);
//                    startActivity(intent);
                    break;
                }
            }
        }
    }

    private String resultStr;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    resultStr = result;
                    if(result.length()==30){
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Looper.prepare();
                                Ordertable ordertable = new Ordertable();
                                ordertable.setOrderId(resultStr);
                                ordertable.setOrderStatus(3);
                                if(OrderClient.updateOrder(getContext(),ordertable)==1){
                                    Toast.makeText(getContext(), "扫码成功", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                                else{
                                    Toast.makeText(getContext(), "扫码失败", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(getContext(), "扫码失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
//                    Toast.makeText(getContext(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }


    }
    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(getContext());
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(getActivity(), permissions, 100);
        }
    }
    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;


    /**
     * EsayPermissions接管权限处理逻辑
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, grantResults);
    }
    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask(int viewId) {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
//            onClick(viewId);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(getActivity(), "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(getContext(), "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getContext(), "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(getActivity(), perms)) {
            new AppSettingsDialog.Builder(getActivity(), "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }
}
