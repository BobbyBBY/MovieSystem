package com.example.moviesystemmanager.activities;

import android.Manifest;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.Util.CheckPermissionUtils;
import com.example.moviesystemmanager.encryption.PasswordManagement;
import com.example.moviesystemmanager.fragments.MovieFragment;
import com.example.moviesystemmanager.fragments.QRFragment;
import com.example.moviesystemmanager.fragments.ScreeningFragment;
import com.example.moviesystemmanager.server.client.PasswordClient;
import com.example.moviesystemmanager.server.client.ScreeningClient;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements
        MovieFragment.OnFragmentInteractionListener,
        QRFragment.OnFragmentInteractionListener,
        ScreeningFragment.OnFragmentInteractionListener
       {

    final private String SIGNATURE = "DXhFV6GiolzdxhC3ydb4xsEJ0QsVy+bYGiyJ4oWrnXabeoUflpbQ0BTxxZMG+CGhoe/TKr/nPGrEQygY3UD40Q==";
    final private int SIGNATUREKEY = 661042377;

    final private int PAGE_MOVIE = 0;
    final private int PAGE_ORDER = 1;
    final private int PAGE_MINE = 2;

    private RadioGroup tabBarRG; //为其设置监听时调用
    private ViewPager viewPager; //为其设置设置适配器时调用

    private RadioButton movieItem = null;
    private RadioButton screeningItem = null;
    private RadioButton qrItem = null;
    private QRFragment qrFragment = null;
    private MovieFragment movieFragment = null;
    private ScreeningFragment screeningFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZXingLibrary.initDisplayOpinion(this);
        setContentView(R.layout.mainactivity);
        initViewPager();
        //获取视图中的RadioGroup对象
        tabBarRG = (RadioGroup) findViewById(R.id.tab_bar);
        //设置选项改变监听器对象，这里使用匿名内部类的方式来实现
        tabBarRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemId) {
                switch (itemId) {
                    case R.id.tab_item_movie:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_item_screening:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_item_qr:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run() {
                PasswordClient.getSlow(getApplicationContext());
                PasswordManagement.setSignature(SIGNATURE);
                PasswordManagement.setSignatureKey(SIGNATUREKEY);
            }
        }).start();
    }


    /**
     * 初始化组件
     */
    private void initViewPager() {
        movieItem = (RadioButton) findViewById(R.id.tab_item_movie);
        screeningItem = (RadioButton) findViewById(R.id.tab_item_screening);
        qrItem = (RadioButton) findViewById(R.id.tab_item_qr);
        movieItem.setChecked(true);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
                if (state == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case PAGE_MOVIE:
                            movieItem.setChecked(true);
                            break;
                        case PAGE_ORDER:
                            screeningItem.setChecked(true);
                            break;
                        case PAGE_MINE:
                            qrItem.setChecked(true);
                            break;
                    }
                }
            }
        });
        viewPager.setAdapter(new MyFragmentPagerAdapter(this.getSupportFragmentManager()));
    }
    public void onFragmentInteraction(Uri uri) {
    }
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private final int PAGER_COUNT = 3;
        private MovieFragment movieFragment = null;
        private ScreeningFragment screeningFragment = null;
        private QRFragment qrFragment = null;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            movieFragment = movieFragment.newInstance();
            screeningFragment = screeningFragment.newInstance();
            qrFragment = qrFragment.newInstance();
        }
        @Override
        public int getCount() {
            return PAGER_COUNT;
        }
        @Override
        public Object instantiateItem(ViewGroup vg, int position) {
            return super.instantiateItem(vg, position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            System.out.println("position Destory" + position);
            super.destroyItem(container, position, object);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment currentFragment = null;
            switch (position) {
                case 0:
                    currentFragment = movieFragment;
                    break;
                case 1:
                    currentFragment = screeningFragment;
                    break;
                case 2:
                    currentFragment = qrFragment;
                    break;
            }
            return currentFragment;
        }
    }




}
