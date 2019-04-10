package com.coderziyang.oneday;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMomentActivity extends AppCompatActivity implements addDialogFragment.OnDialogChosenListener{

    ImageView image_display;
    private Uri imgUri = null;
    public final int TAKE_PHOTO_REQUEST = 1, REQUEST_CODE_PICK_IMAGE = 2;
    public Data new_data;
    Button submit_button;
    String[] category;
    Spinner category_spinner;
    EditText title, content;
    TextView big_title, edit_date;
    int category_index;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moment);
        submit_button = (Button) findViewById(R.id.button);
        category_spinner = (Spinner) findViewById(R.id.spinner);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        big_title = (TextView) findViewById(R.id.edit_class);
        edit_date = (TextView) findViewById(R.id.edit_date);
        image_display = (ImageView) findViewById(R.id.image_view);
        category = getResources().getStringArray(R.array.category_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        category_spinner.setAdapter(adapter);
        if (getIntent().hasExtra("DataId")){
            long inputId=getIntent().getLongExtra("DataId",0);
            DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
            final DataDao mDataDao = daoSession.getDataDao();
            final Data inputData=mDataDao.load(inputId);
            Date inputDate =new Date(inputId);
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            imgUri=inputData.getImageUri();
            Glide.with(this).load(inputData.getImageUri()).into(image_display);
            edit_date.setText(sdf.format(inputDate));
            category_spinner.setSelection(inputData.getCategory(),true);
            title.setText(inputData.getTitle());
            content.setText(inputData.getContent());
            submit_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String title_s = title.getText().toString();
                    String content_s = content.getText().toString();
                    if (title_s.equals("") || content_s.equals("") || imgUri == null) {
                        Toast.makeText(AddMomentActivity.this, R.string.submit_moment_warning,
                                Toast.LENGTH_LONG).show();
                    }else{
                        new_data=new Data(inputData.getDataId(),category_index,title_s,imgUri,content_s);
                        mDataDao.update(new_data);
                        Intent intent2 = new Intent();
                        intent2.putExtra("dataId2",inputData.getDataId());
                        AddMomentActivity.this.setResult(RESULT_OK,intent2);
                        AddMomentActivity.this.finish();
                    }
                }
            });
        }else{
            image_display.setImageResource(R.drawable.plus);
            new_data = new Data();
            date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_s = sdf.format(date);
            edit_date.setText(date_s);
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String title_s = title.getText().toString();
                    String content_s = content.getText().toString();

                    if (title_s.equals("") || content_s.equals("") || imgUri == null) {
                        Toast.makeText(AddMomentActivity.this, R.string.submit_moment_warning,
                                Toast.LENGTH_LONG).show();
                    }else{
                        new_data.setTitle(title_s);
                        new_data.setContent(content_s);
                        new_data.setCategory(category_index);
                        new_data.setImage(imgUri);
                        new_data.setDataId(new Date().getTime());

                        DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
                        DataDao dataDao = daoSession.getDataDao();

                        dataDao.insert(new_data);
                        if(MainActivity.instance!=null){
                            MainActivity.instance.finish();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                category_index = index;
                big_title.setText(category[index]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        image_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialogFragment dialogFragment = new addDialogFragment();
                dialogFragment.show(getSupportFragmentManager(),null);

                dialogFragment.setOnDialogListener(new addDialogFragment.OnDialogChosenListener() {
                    @Override
                    public void onDialogChosen(int option) {
                            switch (option){
                            case 1:
                                openCamera();
                                break;
                            case 2:
                                getImageFromAlbum();
                                break;
                        }
                    }
                });
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_PICK_IMAGE){
            if (data != null){
                imgUri = data.getData();
                showImage(imgUri, image_display);
            }
        }else if(requestCode == TAKE_PHOTO_REQUEST){
            if (data != null){
                showImage(imgUri, image_display);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imgName = "oneday_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        File image = File.createTempFile(
                imgName,         /* prefix */
                ".jpg",    /* suffix */
                pictureDir       /* directory */
        );
        return image;
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photofile = null;
            try {
                photofile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photofile != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    imgUri = Uri.fromFile(photofile);
                } else {
                    imgUri = FileProvider.getUriForFile(this, "com.coderziyang.oneday.fileprovider", photofile);
                }
            }
        }

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    private void showImage(Uri imageUri, ImageView imageView){
        if(imageUri!=null){
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    @Override
    public void onDialogChosen(int option) {
        switch (option) {
            case 1:
                openCamera();
                showImage(imgUri, image_display);
                break;
            case 2:
                getImageFromAlbum();
                break;
        }
    }

}


