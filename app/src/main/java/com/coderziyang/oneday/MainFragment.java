package com.coderziyang.oneday;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.greendao.database.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TIME_NAME = "kind_of_string";
    private static final String DAYS ="days";
    private static final int MAIN =9;
//    private static final int LUNCH = 0;
////    private static final int TRAVEL = 1;
////    private static final int DAY=2;
////    private static final int WORK=3;
////    private static final int PARTY=4;
////    private static final int SPORT=5;
    private List<Data> dataList = new ArrayList<>();
    private MomentAdapter adapter;

    String[] category;
    // TODO: Rename and change types of parameters
    private int timeName;
    private int days;
    Context mContext=this.getActivity();
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int time,int days) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(TIME_NAME, time);
        args.putInt(DAYS,days);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timeName = getArguments().getInt(TIME_NAME);
            days=getArguments().getInt(DAYS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        TextView daysNum = (TextView) view.findViewById(R.id.the_days);
        TextView momentsNum = (TextView) view.findViewById(R.id.the_moments);
        TextView classNum = (TextView) view.findViewById(R.id.class_moments);
        category=getResources().getStringArray(R.array.category_array);
        DataDao dataDao = daoSession.getDataDao();
        daysNum.setText(days+getResources().getString(R.string.days));
        momentsNum.setText(dataDao.count()+getResources().getString(R.string.moments));

        if(timeName==MAIN){
            dataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId).list();
            classNum.setText(getResources().getString(R.string.app_name)+dataDao.count()+getResources().getString(R.string.moments));
        }else{
            dataList=dataDao.queryBuilder().orderDesc(DataDao.Properties.DataId)
                    .where(DataDao.Properties.Category.eq(timeName)).list();
            classNum.setText(category[timeName]+dataList.size()+getResources().getString(R.string.moments));
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MomentAdapter(dataList);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
