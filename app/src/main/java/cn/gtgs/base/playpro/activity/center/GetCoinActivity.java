package cn.gtgs.base.playpro.activity.center;

import cn.gtgs.base.playpro.activity.center.view.GetCoinDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class GetCoinActivity extends ActivityPresenter<GetCoinDelegate> {

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected Class getDelegateClass() {
        return GetCoinDelegate.class;
    }
}
