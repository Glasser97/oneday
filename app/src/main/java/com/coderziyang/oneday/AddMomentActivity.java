package com.coderziyang.oneday;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMomentActivity extends AppCompatActivity implements addDialogFragment.OnDialogChosenListener{

    ImageView image_display;
    private Uri imgUri = null;
    public final int TAKE_PHOTO_REQUEST = 1, REQUEST_CODE_PICK_IMAGE = 2;
    public Data new_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moment);

        image_display = (ImageView) findViewById(R.id.image_view);
        image_display.setImageResource(R.drawable.plus);


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
    protected void onPause(){
        super.onPause();
        image_display.setImageResource(R.drawable.plus);
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


