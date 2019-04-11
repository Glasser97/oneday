package com.coderziyang.oneday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class DetailActivity extends AppCompatActivity {
    public final int MODIFY_CODE=3;
    long id;
    public static DetailActivity instance;
    String[] category;
    DataDao dataDao;
    private ViewPager myViewPager;
    List<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        instance=this;
        id =getIntent().getLongExtra("dataId",0);
        dataDao = daoSession.getDataDao();
        Data data = dataDao.load(id);
        dataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId).list();
        myViewPager=(ViewPager) findViewById(R.id.myViewPager);
        int numOfData = dataList.size();
        int position=0;
        for(int i=0;i<numOfData;i++){
            if (dataList.get(i).getDataId()==id){
                position=i;
                break;
            }
        }
        DetailAdapter myDetailAdapter=new DetailAdapter(getSupportFragmentManager(),dataList,numOfData);
        myViewPager.setAdapter(myDetailAdapter);
        myViewPager.setCurrentItem(position,true);

    }

    @Override
    public void onBackPressed() {
        if (MainActivity.instance!=null){
            MainActivity.instance.finish();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
