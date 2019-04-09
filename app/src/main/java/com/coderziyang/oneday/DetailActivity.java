package com.coderziyang.oneday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    private TextView dateShow;
    private TextView classShow;
    private TextView titleShow;
    private ImageView imageShow;
    private TextView contentShow;
    public final int MODIFY_CODE=3;
    long id;
    String[] category;
    DataDao dataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dateShow=findViewById(R.id.date_show);
        classShow=findViewById(R.id.class_show);
        titleShow=findViewById(R.id.title_show);
        imageShow=findViewById(R.id.image_view);
        contentShow=findViewById(R.id.content_show);
        category = getResources().getStringArray(R.array.category_array);
        id =getIntent().getLongExtra("dataId",0);
        DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
        dataDao = daoSession.getDataDao();
        Data data = dataDao.load(id);
        Date date =new Date(id);
        SimpleDateFormat sdf=new SimpleDateFormat("dd/mm/yyyy");
        Glide.with(this).load(data.getImageUri()).into(imageShow);
        dateShow.setText(sdf.format(date));
        classShow.setText(category[data.getCategory()]);
        titleShow.setText(data.getTitle());
        contentShow.setText(data.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.delete:
                dataDao.deleteByKey(id);
                if (MainActivity.instance!=null) {
                    MainActivity.instance.finish();
                }
                Intent backIntent = new Intent(this,MainActivity.class);
                startActivity(backIntent);
                finish();
                return true;
            case R.id.modify:
                Intent intent = new Intent(this, AddMomentActivity.class);
                intent.putExtra("DataId",id);
                startActivityForResult(intent,MODIFY_CODE);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result){
        if (requestCode == MODIFY_CODE){
            if (result != null){
                long resId=result.getLongExtra("dataId2",0);
                Data data = dataDao.load(resId);
                Date date =new Date(id);
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                Glide.with(this).load(data.getImageUri()).into(imageShow);
                dateShow.setText(sdf.format(date));
                classShow.setText(category[data.getCategory()]);
                titleShow.setText(data.getTitle());
                contentShow.setText(data.getContent());
            }
        }
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
