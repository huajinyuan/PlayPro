package cn.gtgs.base.playpro.widget;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by gtgs on 2017/5/5.
 */

public class ParentViewPaperAdapter extends PagerAdapter  {

    List<ViewPager> pageViews;

    public ParentViewPaperAdapter(List<ViewPager> pageViews) {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        try {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        } catch (Exception e) {
        }

    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(pageViews.get(arg1));
        return pageViews.get(arg1);
    }

//    @Override
//    public LocalFaceGroup getFaceGroupItem(int position) {
//        if (position >= 2) {
//            return faceGroups.get(position - 2);
//        }
//        return null;
//    }

}