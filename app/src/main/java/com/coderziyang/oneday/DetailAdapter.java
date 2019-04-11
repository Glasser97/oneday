package com.coderziyang.oneday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import static com.coderziyang.oneday.DaoApplication.daoSession;

public class DetailAdapter extends FragmentPagerAdapter {
    int numOfData;
    DataDao dataDao;
    List<Data> dataList;
    public DetailAdapter(FragmentManager fm, List<Data> dataList,int numOfData)
    {
        super(fm);
        this.numOfData=numOfData;
        this.dataList=dataList;
        dataDao=daoSession.getDataDao();
    }
    @Override
    public Fragment getItem(int position) {
        Data data=dataList.get(position);
        DetailFragment mFragment = DetailFragment.newInstance(data.getDataId(),data.getCategory(),data.getImage(),data.getTitle(),data.getContent());
        return mFragment;
    }
    @Override
    public int getCount() {
        return numOfData;
    }
}
