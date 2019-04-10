package com.coderziyang.oneday;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class MainActivity extends AppCompatActivity{
    private List<Data> dataList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    private MomentAdapter adapter;
    public static MainActivity instance;
    private static final int MAIN =9;
    private static final int LUNCH = 0;
    private static final int TRAVEL = 1;
    private static final int DAY=2;
    private static final int WORK=3;
    private static final int PARTY=4;
    private static final int SPORT=5;
    int whichTime;
    int days;

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        MainFragment rePageFrag=MainFragment.newInstance(whichTime,days);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_layout,rePageFrag).commit();
    }
    @Override
    protected  void onPause(){
        super.onPause();
        savePreferences(whichTime);
    }
    public void savePreferences(int whichTime) {
        SharedPreferences pref = getSharedPreferences("TIME_KINDS", MODE_PRIVATE);
        pref.edit().putInt("whichTime", whichTime).apply();
    }
    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("TIME_KINDS", MODE_PRIVATE);
        whichTime=pref.getInt("whichTime", 9);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.app_bar_main);
        //设定工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int id=tab.getPosition();
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (id == 0) {
                    whichTime=MAIN;
                    // Handle the camera action
                } else if (id == 1) {
                    whichTime=LUNCH;
                }  else if (id == 2) {
                    whichTime=TRAVEL;
                } else if (id == 3) {
                    whichTime=DAY;
                } else if (id == 4) {
                    whichTime=WORK;
                }else if (id == 5) {
                    whichTime=PARTY;
                }else if (id == 6) {
                    whichTime=SPORT;
                }
                MainFragment mainPageFrag=MainFragment.newInstance(whichTime,days);
                ft.replace(R.id.main_layout,mainPageFrag).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DataDao dataDao = daoSession.getDataDao();
        //数据库中的data
        dataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId).list();
        Date date=new Date();
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(int i=dataList.size()-1;i>=0;i--){
            date.setTime(dataList.get(i).getDataId());
            str=sdf.format(date);
            if(!dayList.contains(str)){
                dayList.add(sdf.format(date));
            }
        }
        days=dayList.size();
        whichTime=MAIN;
        //悬浮小圆球，跳转activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddMomentActivity.class);
                startActivity(intent);
            }
        });
    }



}

