package cn.gtgs.base.playpro.activity.center;

import cn.gtgs.base.playpro.activity.center.view.CenterDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class CenterActivity extends ActivityPresenter<CenterDelegate> {
    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected Class<CenterDelegate> getDelegateClass() {
        return CenterDelegate.class;
    }
}
