package com.coderziyang.oneday;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
    int nNumOfTabs;
    int days;
    private static final int MAIN =9;
    private static final int LUNCH = 0;
    private static final int TRAVEL = 1;
    private static final int DAY=2;
    private static final int WORK=3;
    private static final int PARTY=4;
    private static final int SPORT=5;
    public FragmentAdapter(FragmentManager fm, int nNumOfTabs,int days)
    {
        super(fm);
        this.nNumOfTabs=nNumOfTabs;
        this.days=days;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                MainFragment tab1=MainFragment.newInstance(MAIN,days);
                return tab1;
            case 1:
                MainFragment tab2=MainFragment.newInstance(LUNCH,days);
                return tab2;
            case 2:
                MainFragment tab3=MainFragment.newInstance(TRAVEL,days);
                return tab3;
            case 3:
                MainFragment tab4=MainFragment.newInstance(DAY,days);
                return tab4;
            case 4:
                MainFragment tab5=MainFragment.newInstance(WORK,days);
                return tab5;
            case 5:
                MainFragment tab6=MainFragment.newInstance(PARTY,days);
                return tab6;
            case 6:
                MainFragment tab7=MainFragment.newInstance(SPORT,days);
                return tab7;
        }
        return null;
    }
    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
