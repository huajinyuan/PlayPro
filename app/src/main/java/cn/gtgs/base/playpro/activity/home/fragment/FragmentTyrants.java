package cn.gtgs.base.playpro.activity.home.fragment;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.TyrantsPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.TyrantsDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentTyrants extends FragmentPresenter<TyrantsDelegate> {
    TyrantsPresenter presenter;
    @Override
    protected Class getDelegateClass() {
        return TyrantsDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        presenter = new TyrantsPresenter(viewDelegate);
        presenter.initData();

    }

}
