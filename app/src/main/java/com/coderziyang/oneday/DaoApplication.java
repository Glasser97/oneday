package com.coderziyang.oneday;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

public class DaoApplication extends Application {
    private final static String databaseName="OneDay_DB";
    public static DaoSession daoSession=null;
    @Override
    public void onCreate(){
        super.onCreate();
        DaoMaster.DevOpenHelper openHelper=new DaoMaster.DevOpenHelper(this,databaseName);
        Database database=openHelper.getWritableDb();
        DaoMaster daoMaster=new DaoMaster(database);
        daoSession=daoMaster.newSession();
    }


    public  DaoSession getDaoSession(){
        return daoSession;
    }
}
