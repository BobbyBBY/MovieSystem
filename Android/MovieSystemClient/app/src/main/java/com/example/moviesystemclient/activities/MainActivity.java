package com.example.moviesystemclient.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.moviesystemclient.R;
import com.example.moviesystemclient.fragments.MineFragment;
import com.example.moviesystemclient.fragments.MovieFragment;
import com.example.moviesystemclient.fragments.OrderFragment;

/**
 * @Title: MainActivity.java
 * @Package: com.example.moviesystemclient.activities
 * @Description:
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:00
 * @version V1.0
 */
public class MainActivity extends AppCompatActivity implements
        MovieFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener{

    final private int PAGE_MOVIE = 0;
    final private int PAGE_ORDER = 1;
    final private int PAGE_MINE = 2;

    private RadioGroup tabBarRG; //为其设置监听时调用
    private ViewPager viewPager; //为其设置设置适配器时调用

    private RadioButton movieItem = null;
    private RadioButton orderItem = null;
    private RadioButton mineItem = null;
    private MineFragment mineFragment = null;
    private MovieFragment movieFragment = null;
    private OrderFragment orderFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    case R.id.tab_item_order:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_item_mine:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }
    private void initViewPager() {
        movieItem = (RadioButton) findViewById(R.id.tab_item_movie);
        orderItem = (RadioButton) findViewById(R.id.tab_item_order);
        mineItem = (RadioButton) findViewById(R.id.tab_item_mine);
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
                            orderItem.setChecked(true);
                            break;
                        case PAGE_MINE:
                            mineItem.setChecked(true);
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
        private OrderFragment orderFragment = null;
        private MineFragment mineFragment = null;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            movieFragment = movieFragment.newInstance();
            orderFragment = orderFragment.newInstance();
            mineFragment = mineFragment.newInstance();
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
                    currentFragment = orderFragment;
                    break;
                case 2:
                    currentFragment = mineFragment;
                    break;
            }
            return currentFragment;
        }
    }
}
