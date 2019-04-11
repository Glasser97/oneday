package com.coderziyang.oneday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {
    public List<Data> mMomentsList;
    Context mContext;

    String shareContent;
    String shareMoment;
    Uri ImageURI;
    Bitmap generatedImage;
    static class MomentViewHolder extends RecyclerView.ViewHolder{
        ImageView momentImg;
        TextView momentTitle;
        TextView momentCategory;
        Button momentViewBtn;
        Button shareButton;
        public MomentViewHolder(View view){
            super(view);
            momentImg = (ImageView)view.findViewById(R.id.moment_img);
            momentTitle = (TextView)view.findViewById(R.id.moment_title);
            momentCategory = (TextView)view.findViewById(R.id.moment_category);
            momentViewBtn = (Button)view.findViewById(R.id.moment_button_view);
            shareButton=(Button)view.findViewById(R.id.moment_button_share);
        }
    }
    public MomentAdapter(List<Data> MomentList){
        this.mMomentsList = MomentList;
    }

    @Override
    public MomentViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moment_layout,parent,false);
        mContext=parent.getContext();
        final MomentViewHolder holder=new MomentViewHolder(view);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, 0, 0,"delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DataDao dataDao = daoSession.getDataDao();
                        long id = (Long) holder.itemView.getTag();
                        dataDao.deleteByKey(id);
                        int position = holder.getLayoutPosition();
                        mMomentsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(0,mMomentsList.size());
                        return true;
                    }
                });
            }
        });
        holder.momentViewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                long id = (Long) holder.itemView.getTag();
                //Toast.makeText(mContext,""+id,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("dataId",id);
                mContext.startActivity(intent);

            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                long sid = (Long) holder.itemView.getTag();
                DataDao dataDao = daoSession.getDataDao();
                Data sData=dataDao.load(sid);
//              shareTitle=sData.getTitle();
//              i=sData.getCategory();
//              shareCategory=mContext.getResources().getStringArray(R.array.category_array)[i];
//              shareImageURI=sData.getImage();
//              ImageURI=Uri.parse(shareImageURI);
                ImageURI=sData.getImageUri();
                Log.d("55555555555555555555555", "onClick: "+ ImageURI);
                shareContent=sData.getContent();             //从数据库拿数据
                generatedImage=shareGenerate(ImageURI,shareContent);
                saveToLocal(generatedImage,shareMoment);
                String ImagePath = "content://" + "/sdcard/DCIM/Camera/"+shareMoment+".jpg";
                Uri uri = Uri.parse(ImagePath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                mContext.startActivity(Intent.createChooser(intent,shareMoment));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder,int position){
        Data data = mMomentsList.get(position);
        holder.itemView.setTag(data.getDataId());
        Glide.with(mContext).load(data.getImageUri()).fitCenter().into(holder.momentImg);
        holder.momentTitle.setText(data.getTitle());
        holder.momentCategory.setText(mContext.getResources().getStringArray(R.array.category_array)[data.getCategory()]);
    }

    @Override
    public int getItemCount(){
        return mMomentsList.size();
    }

    public Data getItem(int position){return mMomentsList.get(position);}

    public void remove(int position){
        if (mMomentsList != null){
            mMomentsList.remove(position);
        }
        notifyDataSetChanged();
    }

    public void addItem(int position, Data data) {
        mMomentsList.add(position, data);
        notifyItemInserted(position);//通知演示插入动画
        notifyItemRangeChanged(position, mMomentsList.size() - position);//通知数据与界面重新绑定
    }
    public Bitmap shareGenerate(Uri ImageURI,String shareContent){
        Bitmap SourceImage=getBitmapFromUri(ImageURI);//原来的图片
        Bitmap generateBitmap=null;                   //要生成的图片
        int SourceImageWidth=SourceImage.getWidth();  //获取原图的长宽
        int SourceImageHeight=SourceImage.getHeight();
        float padding = 20;                           //配文与图片间的距离
        float linePadding = 5;                        //配文行间距
        float textSize = 40;                          //字体大小
        Paint bitmapPaint=new Paint();
        Paint textPaint=new Paint();
        int textColor = Color.BLACK;
        int lineTextCount = (int) ((SourceImage.getWidth()-50)/textSize);
        int contentLine = (int) Math.ceil(Double.valueOf(shareContent.length())/Double.valueOf(lineTextCount));
        generateBitmap=SourceImage.createBitmap(SourceImageWidth,(int) (SourceImageHeight+padding+textSize*contentLine+linePadding*contentLine), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(generateBitmap);
        canvas.drawBitmap(SourceImage,0,0,bitmapPaint);
        textPaint.setColor(Color.WHITE);
        canvas.drawRect(0,SourceImage.getHeight(),SourceImage.getWidth(),
                SourceImage.getHeight()+padding+textSize*contentLine+linePadding*contentLine,textPaint);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        Rect bounds = new Rect();
        for (int i=0; i<contentLine; i++) {
            String s;
            if (i == contentLine-1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                s = shareContent.substring(i*lineTextCount, shareContent.length());
            } else {//不是最后一行
                s = shareContent.substring(i*lineTextCount, (i+1)*lineTextCount);
            }
            //获取文字的字宽高以便把文字与图片中心对齐
            textPaint.getTextBounds(s,0,s.length(),bounds);
            //画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(s,SourceImage.getWidth()/2-bounds.width()/2,
                    SourceImage.getHeight()+padding+i*textSize+i*linePadding+bounds.height()/2,textPaint);
        }
        canvas.save();
        canvas.restore();
        return generateBitmap;
    }
    private Bitmap getBitmapFromUri(Uri uri) {

        try {
//            mContext.grantUriPermission("com.google.android.apps.photos.contentprovider", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 读取uri所在的图片
            Bitmap bitmapSource = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmapSource;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
    private void saveToLocal(Bitmap bitmap, String bitName)  {
        File file = new File("/sdcard/DCIM/Camera/" + bitName + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
                //保存图片后发送广播通知更新数据库
                // Uri uri = Uri.fromFile(file);
                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                mContext.sendBroadcast(intent);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
