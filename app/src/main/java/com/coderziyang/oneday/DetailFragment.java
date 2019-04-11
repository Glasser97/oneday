package com.coderziyang.oneday;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import static com.coderziyang.oneday.DaoApplication.daoSession;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    String[] category;
    private long mId;
    private int mCatgory;
    private String mTitle;
    private String mImageUri;
    private String mContent;


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(long id,int category,String imageUri, String title,String content) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putInt("category", category);
        args.putString("imageUri",imageUri);
        args.putString("title",title);
        args.putString("content",content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mId = getArguments().getLong("id");
            mCatgory=getArguments().getInt("category");
            mImageUri=getArguments().getString("imageUri");
            mTitle=getArguments().getString("title");
            mContent=getArguments().getString("content");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detail, container, false);
        TextView dateShow=view.findViewById(R.id.date_show);
        TextView classShow=view.findViewById(R.id.class_show);
        TextView titleShow=view.findViewById(R.id.title_show);
        ImageView imageShow=view.findViewById(R.id.image_view);
        TextView contentShow=view.findViewById(R.id.content_show);
        category = getResources().getStringArray(R.array.category_array);
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        Glide.with(this).load(mImageUri).into(imageShow);
        dateShow.setText(sdf.format(mId));
        classShow.setText(category[mCatgory]);
        titleShow.setText(mTitle);
        contentShow.setText(mContent);
        return view;
    }

}
