package com.coderziyang.oneday;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MomentViewHolder> {
    private List<Data> mMomentsList;
    Context mContext;
    static class MomentViewHolder extends RecyclerView.ViewHolder{
        ImageView momentImg;
        TextView momentTitle;
        TextView momentCategory;
        public MomentViewHolder(View view){
            super(view);
            momentImg = (ImageView)view.findViewById(R.id.moment_img);
            momentTitle = (TextView)view.findViewById(R.id.moment_title);
            momentCategory = (TextView)view.findViewById(R.id.moment_category);
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
                        DaoMaster.DevOpenHelper openHelper=new DaoMaster.DevOpenHelper(mContext,"OneDay_DB");
                        Database database=openHelper.getWritableDb();
                        DaoMaster daoMaster=new DaoMaster(database);
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
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                int position = holder.getAdapterPosition();
//                Intent intent = new Intent()
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder,int position){
        Data data = mMomentsList.get(position);
        holder.itemView.setTag(data.getDataId());
        Glide.with(mContext).load(data.getImageUri()).into(holder.momentImg);
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

}
