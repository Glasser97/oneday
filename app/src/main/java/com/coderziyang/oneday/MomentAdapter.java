package com.coderziyang.oneday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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
        MomentViewHolder holder=new MomentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder,int position){
        Data data = mMomentsList.get(position);
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

}
