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
//        dateShow=findViewById(R.id.date_show);
//        classShow=findViewById(R.id.class_show);
//        titleShow=findViewById(R.id.title_show);
//        imageShow=findViewById(R.id.image_view);
//        contentShow=findViewById(R.id.content_show);
//        category = getResources().getStringArray(R.array.category_array);
        id =getIntent().getLongExtra("dataId",0);
        dataDao = daoSession.getDataDao();
//        Data data = dataDao.load(id);
//        Date date =new Date(id);
//
//        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//        Glide.with(this).load(data.getImageUri()).into(imageShow);
//        dateShow.setText(sdf.format(date));
//        classShow.setText(category[data.getCategory()]);
//        titleShow.setText(data.getTitle());
//        contentShow.setText(data.getContent());
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present
//        getMenuInflater().inflate(R.menu.detail_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        switch (itemId) {
//            case R.id.delete:
//                dataDao.deleteByKey(id);
//                if (MainActivity.instance!=null) {
//                    MainActivity.instance.finish();
//                }
//                Intent backIntent = new Intent(this,MainActivity.class);
//                startActivity(backIntent);
//                finish();
//                return true;
//            case R.id.modify:
//                Intent intent = new Intent(this, AddMomentActivity.class);
//                intent.putExtra("DataId",id);
//                startActivityForResult(intent,MODIFY_CODE);
//                return true;
//        }
//        return false;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent result){
//        if (requestCode == MODIFY_CODE){
//            if (result != null){
//                long resId=result.getLongExtra("dataId2",0);
//                Data data = dataDao.load(resId);
////                Date date =new Date(id);
////                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
////                Glide.with(this).load(data.getImageUri()).into(imageShow);
////                dateShow.setText(sdf.format(date));
////                classShow.setText(category[data.getCategory()]);
////                titleShow.setText(data.getTitle());
////                contentShow.setText(data.getContent());
//                List<Data> thisDataList=new ArrayList<>();
//                thisDataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId).list();
//                int numOfData = dataList.size();
//                int position2=0;
//                for(int i=0;i<numOfData;i++){
//                    if (dataList.get(i).getDataId()==id){
//                        position2=i;
//                        break;
//                    }
//                }
//                ViewPager thisViewPager=findViewById(R.id.myViewPager);
//                DetailAdapter freshDetailAdapter=new DetailAdapter(getSupportFragmentManager(),thisDataList,numOfData);
//                thisViewPager.setAdapter(freshDetailAdapter);
//                thisViewPager.setCurrentItem(position2,true);
//            }
//        }
//    }
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
