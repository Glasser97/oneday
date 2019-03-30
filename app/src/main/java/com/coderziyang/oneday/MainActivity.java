package com.coderziyang.oneday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the DAO Database
        DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
        DataDao dataDao = daoSession.getDataDao();

        //然后用这个dataDao下面的方法进行数据库操作
    }
}
