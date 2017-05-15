package cn.gtgs.base.playpro.activity.home.fragment;

import android.support.v4.widget.SwipeRefreshLayout;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.RecommentedPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.RecommentedDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by  on 2017/4/26.
 */

public class FragmentRecommented extends FragmentPresenter<RecommentedDelegate> implements SwipeRefreshLayout.OnRefreshListener {
    RecommentedPresenter presenter;
//    private ArrayList<String> imageUrls = new ArrayList<>();

    @Override
    protected Class<RecommentedDelegate> getDelegateClass() {
        return RecommentedDelegate.class;
    }

    @Override
    public void init() {
        presenter = new RecommentedPresenter(viewDelegate);
        presenter.initData();
        viewDelegate.getmSwp().setOnRefreshListener(this);
//        imageUrls.add("http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg");
//        imageUrls.add("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
//        imageUrls.add("http://pic18.nipic.com/20111215/577405_080531548148_2.jpg");
//        viewDelegate.setAD(imageUrls);
    }

    @Override
    public void onRefresh() {
        presenter.initData();
    }


}
