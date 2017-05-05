package cn.gtgs.base.playpro.activity.home.fragment;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.FollowPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollow;
import cn.gtgs.base.playpro.activity.home.fragment.view.FollowDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentFollow extends FragmentPresenter<FollowDelegate> {
    IFollow presenter;

    @Override
    protected Class getDelegateClass() {
        return FollowDelegate.class;
    }

    @Override
    public void init() {
        presenter = new FollowPresenter(viewDelegate);
        presenter.initData();

    }
}
