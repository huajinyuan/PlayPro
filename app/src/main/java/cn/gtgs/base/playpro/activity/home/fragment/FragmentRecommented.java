package cn.gtgs.base.playpro.activity.home.fragment;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.RecommentedPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.RecommentedDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentRecommented extends FragmentPresenter<RecommentedDelegate> {
    RecommentedPresenter presenter;

    @Override
    protected Class<RecommentedDelegate> getDelegateClass() {
        return RecommentedDelegate.class;
    }

    @Override
    public void init() {
        presenter = new RecommentedPresenter(viewDelegate);
        presenter.initData();
    }

}
