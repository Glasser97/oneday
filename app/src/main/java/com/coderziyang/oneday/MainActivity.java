package com.coderziyang.oneday;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private List<Data> dataList = new ArrayList<>();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        MainFragment rePageFrag=MainFragment.newInstance(whichTime);
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
        setContentView(R.layout.activity_main);
        //设定工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //get the DAO Database
        //然后用这个dataDao下面的方法进行数据库操作
        //显示数据库中的data
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (id == R.id.main_page) {
            whichTime=MAIN;
            // Handle the camera action
        } else if (id == R.id.lunch_time) {
            whichTime=LUNCH;
        }  else if (id == R.id.travel_time) {
            whichTime=TRAVEL;
        } else if (id == R.id.day_time) {
            whichTime=DAY;
        } else if (id == R.id.work_time) {
            whichTime=WORK;
        }else if (id == R.id.party_time) {
            whichTime=PARTY;
        }else if (id == R.id.sport_time) {
            whichTime=SPORT;
        }
        MainFragment mainPageFrag=MainFragment.newInstance(whichTime);
        ft.replace(R.id.main_layout,mainPageFrag).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

