package com.coderziyang.oneday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class MainActivity extends AppCompatActivity{
    private List<Data> dataList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    public static MainActivity instance;
    private static final int MAIN =9;
    private static final int LUNCH = 0;
    private static final int TRAVEL = 1;
    private static final int DAY=2;
    private static final int WORK=3;
    private static final int PARTY=4;
    private static final int SPORT=5;
    private ViewPager myViewPager;
    private TabLayout tablayout;
    int whichTime;
    int days;
    String languageselect;

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        if (whichTime!=0){
            tablayout.getTabAt(whichTime).select();
        }
    }
    @Override
    protected  void onPause(){
        super.onPause();
        savePreferences(tablayout.getSelectedTabPosition());
    }
    public void savePreferences(int whichTime) {
        SharedPreferences pref = getSharedPreferences("TIME_KINDS", MODE_PRIVATE);
        pref.edit().putInt("whichTime", whichTime).apply();
    }
    public void savePreferences(String language) {
        SharedPreferences pref = getSharedPreferences("Language", MODE_PRIVATE);
        pref.edit().putString("language", language).apply();
    }
    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("TIME_KINDS", MODE_PRIVATE);
        whichTime=pref.getInt("whichTime", 0);
    }
    public void loadLanguagePreferences() {
        SharedPreferences pref = getSharedPreferences("Language", MODE_PRIVATE);
        languageselect=pref.getString("language","en");
    }

    public void selectLanguage(String language){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (language) {
            case "en":
                configuration.locale = Locale.ENGLISH;
                break;
            case "zh":
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                configuration.locale = Locale.ENGLISH;
                break;
        }
        savePreferences(languageselect);
        resources.updateConfiguration(configuration, displayMetrics);
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //保存设置语言的类型
//        SharedPreferenceUtils.putString(this,"language", language);
    }
    public void setLanguageStart(String lang){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (lang) {
            case "en":
                configuration.locale = Locale.ENGLISH;
                break;
            case "zh":
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                configuration.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.app_bar_main);
        loadLanguagePreferences();
        setLanguageStart(languageselect);
        //设定工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tablayout = (TabLayout) findViewById(R.id.tablayout);


        DataDao dataDao = daoSession.getDataDao();
        //数据库中的data,得到有多少天
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

        whichTime=0;
        myViewPager=(ViewPager) findViewById(R.id.myViewPager);
        FragmentAdapter myFragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),7,days);
        myViewPager.setAdapter(myFragmentAdapter);
        tablayout.setupWithViewPager(myViewPager);
        for (int i=0;i<7;i++){
            tablayout.getTabAt(i).setText(getResources().getStringArray(R.array.tablayout_array)[i]);
        }

        //悬浮小圆球，跳转activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddMomentActivity.class);
                intent.putExtra("timeName",tablayout.getSelectedTabPosition());
                startActivity(intent);
            }
        });

        final ImageButton language = (ImageButton) findViewById(R.id.image_button);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( languageselect.equals("zh")){
                    languageselect="en";
                }else if(languageselect.equals("en")){
                    languageselect="zh";
                }
                selectLanguage(languageselect);
            }
        });


    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//    private void freshFragment(int selection){
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        MainFragment mainPageFrag=MainFragment.newInstance(selection,days);
//        ft.replace(R.id.main_layout,mainPageFrag).commit();
//    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        if (id == R.id.main_page) {
//            whichTime=MAIN;
//            // Handle the camera action
//        } else if (id == R.id.lunch_time) {
//            whichTime=LUNCH;
//        }  else if (id == R.id.travel_time) {
//            whichTime=TRAVEL;
//        } else if (id == R.id.day_time) {
//            whichTime=DAY;
//        } else if (id == R.id.work_time) {
//            whichTime=WORK;
//        }else if (id == R.id.party_time) {
//            whichTime=PARTY;
//        }else if (id == R.id.sport_time) {
//            whichTime=SPORT;
//        }
//        MainFragment mainPageFrag=MainFragment.newInstance(whichTime,days);
//        ft.replace(R.id.main_layout,mainPageFrag).commit();
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}

