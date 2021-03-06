package com.coderziyang.oneday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddMomentActivity extends AppCompatActivity implements addDialogFragment.OnDialogChosenListener{

    ImageView image_display;
    private Uri imgUri = null, cropUri = null;
    public final int TAKE_PHOTO_REQUEST = 1, REQUEST_CODE_PICK_IMAGE = 2;
    public Data new_data;
    Button submit_button;
    String[] category;
    Spinner category_spinner;
    EditText title, content;
    TextView big_title, edit_date;
    int category_index;
    String token;
    String jinrishici;
    Token mToken;
    OkHttpClient mOkHttpClient;
    Date date;
    int index;

    public void savePreferences(String token) {
        SharedPreferences pref = getSharedPreferences("token", MODE_PRIVATE);
        pref.edit().putString("token", token).apply();
    }
    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("token", MODE_PRIVATE);
        token=pref.getString("token", null);
    }


    @Override
    protected  void onResume() {
        super.onResume();
        loadPreferences();
        //网络获取token与每日一词相关
        mOkHttpClient = new OkHttpClient();
        if (token == null) {
            Request request = new Request.Builder()
                    .url("https://v2.jinrishici.com/token")
                    .build();
            Call call = mOkHttpClient.newCall(request);
            //异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("getToken", "Failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        mToken = gson.fromJson(responseData, Token.class);
                        savePreferences(mToken.getData());
                    }
                }
            });
        } else {
            Request request = new Request.Builder()
                    .url("https://v2.jinrishici.com/sentence")
                    .addHeader("X-User-Token", token)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("response", "Failure to get sentence!!!");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject=new JSONObject(responseData);
                            JSONObject jsonData= jsonObject.getJSONObject("data");
                            jinrishici=jsonData.getString("content");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


        }
    }
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

        if (getIntent().hasExtra("dataId")){

            long inputId = getIntent().getLongExtra("dataId",0);
            DaoSession daoSession = ((DaoApplication)getApplication()).getDaoSession();
            final DataDao mDataDao = daoSession.getDataDao();
            final Data inputData = mDataDao.load(inputId);
            Date inputDate = new Date(inputId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            imgUri = inputData.getImageUri();
            Glide.with(this).load(inputData.getImageUri()).into(image_display);
            edit_date.setText(sdf.format(inputDate));
            category_spinner.setSelection(inputData.getCategory(),true);
            index=inputData.getCategory();
            category_index = index;
            big_title.setText(category[index]);
            title.setText(inputData.getTitle());
            content.setText(inputData.getContent());
            submit_button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    String title_s = title.getText().toString();
                    if (title_s.equals("")){
                        title_s=jinrishici;
                    }
                    String content_s = content.getText().toString();
                    if (title_s.equals("") || content_s.equals("") || imgUri == null) {
                        Toast.makeText(AddMomentActivity.this, R.string.submit_moment_warning,
                                Toast.LENGTH_LONG).show();
                    }else{
                        new_data=new Data(inputData.getDataId(),category_index,title_s,imgUri,content_s);
                        mDataDao.update(new_data);
                        Intent intent2 = new Intent(getApplicationContext(),DetailActivity.class);
                        intent2.putExtra("dataId",inputData.getDataId());
                        //AddMomentActivity.this.setResult(RESULT_OK,intent2);
                        if(DetailActivity.instance!=null){
                            DetailActivity.instance.finish();
                            startActivity(intent2);
                        }

                        AddMomentActivity.this.finish();
                    }
                }
            });

        }else{ //////////////////////////////////////////////////////////

            image_display.setImageResource(R.drawable.plus);
            new_data = new Data();
            date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_s = sdf.format(date);
            edit_date.setText(date_s);
            int position=getIntent().getIntExtra("timeName",0);
            if (position!=0){
                category_spinner.setSelection(position-1,true);
                index=position-1;
                category_index = index;
                big_title.setText(category[index]);
            }

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_s = title.getText().toString();
                    if (title_s.equals("")){
                        title_s=jinrishici;
                    }
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
                index = parent.getSelectedItemPosition();
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
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                File album_file;
                File crop_image = null;

                album_file = saveIntentDataToExternal("RAW", data.getData());
                imgUri = FileProvider.getUriForFile(this, "com.coderziyang.oneday.fileprovider", album_file);
                try {
                    crop_image = createImageFile("CROP");
                } catch (IOException e) {
                    crop_image.delete();
                    e.printStackTrace();
                }
                cropUri = Uri.fromFile(crop_image);
                cropRawPhoto(imgUri, cropUri);
                showImage(cropUri, image_display);
                imgUri = cropUri;
            }
            } else if (requestCode == TAKE_PHOTO_REQUEST) {
                    File crop_image = null;
                    try {
                        crop_image = createImageFile("CROP");
                    } catch (IOException e) {
                        crop_image.delete();
                        e.printStackTrace();
                    }
                    cropUri = Uri.fromFile(crop_image);
                    cropRawPhoto(imgUri, cropUri);
                    showImage(cropUri, image_display);
                    imgUri = cropUri;
            }
        }




    private File createImageFile(String option) throws IOException {              ///////create temp image file for saving image.
        switch (option){

            case "RAW":
                String imgName = "oneday_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File pictureDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/RAW");
                if (!pictureDir.exists()) {
                    pictureDir.mkdirs();
                }
                File image = File.createTempFile(
                        imgName,         /* prefix */
                        ".jpg",    /* suffix */
                        pictureDir       /* directory */
                );
                return image;

            case "CROP":
                String cropName = "oneday_crop_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File cropDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/CROP");
                if (!cropDir.exists()) {
                    cropDir.mkdirs();
                }
                File cropimage = File.createTempFile(
                        cropName,         /* prefix */
                        ".jpg",    /* suffix */
                        cropDir       /* directory */
                );
                return cropimage;

            default:System.out.println("createImageFile need a option.");
                return null;
        }
    }

    private void openCamera(){                                     /////////open camera to take photo,save uri for display.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photofile = null;
            try {
                photofile = createImageFile("RAW");
            } catch (IOException e) {
                photofile.delete();
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
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    private void showImage(Uri imageUri, ImageView imageView){
        if(imageUri!=null){
            Glide.with(this).load(imageUri).error(R.drawable.plus).into(imageView);
        }
    }

    @Override
    public void onDialogChosen(int option) {
        switch (option) {
            case 1:
                openCamera();
                break;
            case 2:
                getImageFromAlbum();
                break;
        }
    }

    private File saveIntentDataToExternal(String option, Uri image_uri){
        File file = null;
        try {
            InputStream  inputStream = getContentResolver().openInputStream(image_uri);    //////copy the chosen image into application picture directory.
            int  bytesAvailable = inputStream.available();
            int maxBufferSize = 1 * 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];

            file = createImageFile(option);
            FileOutputStream outputStream = new FileOutputStream(file);

            int read = 0;
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
        } catch (IOException e) {
            file.delete();
            e.printStackTrace();
        }
        return file;
    }

    public void cropRawPhoto(Uri source_uri, Uri destination_uri) {

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary, null));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
        options.setHideBottomControls(false);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setFreeStyleCropEnabled(false);

        UCrop.of(source_uri, destination_uri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

}


