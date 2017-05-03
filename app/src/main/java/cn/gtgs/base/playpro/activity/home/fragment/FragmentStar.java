package cn.gtgs.base.playpro.activity.home.fragment;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.StarPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.StarDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentStar extends FragmentPresenter<StarDelegate> {
    StarPresenter presenter;
    @Override
    protected Class getDelegateClass() {
        return StarDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        presenter = new StarPresenter(viewDelegate);
        presenter.initData();

    }

}
