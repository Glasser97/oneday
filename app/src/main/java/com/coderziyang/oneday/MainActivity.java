package com.coderziyang.oneday;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Data> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the DAO Database
        DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
        DataDao dataDao = daoSession.getDataDao();
        Date date = new Date();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        + getResources().getResourcePackageName(R.drawable.img279) + "/"
        + getResources().getResourceTypeName(R.drawable.img279) + "/"
        + getResources().getResourceEntryName(R.drawable.img279));
        Data data = new Data(date.getTime(),1,"this is test",uri,"kabfjbajf");
        dataDao.insert(data);
        //然后用这个dataDao下面的方法进行数据库操作
        //显示数据库中的data
        dataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId).list();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MomentAdapter adapter = new MomentAdapter(dataList);
        recyclerView.setAdapter(adapter);

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

