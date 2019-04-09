package com.coderziyang.oneday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    long id;
    String[] category;

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
        DataDao dataDao = daoSession.getDataDao();
        Data data = dataDao.load(id);
        Date date =new Date(id);
        SimpleDateFormat sdf=new SimpleDateFormat("dd/mm/yyyy");
        Glide.with(this).load(data.getImageUri()).into(imageShow);
        dateShow.setText(sdf.format(date));
        classShow.setText(category[data.getCategory()]);
        titleShow.setText(data.getTitle());
        contentShow.setText(data.getContent());
    }


}
