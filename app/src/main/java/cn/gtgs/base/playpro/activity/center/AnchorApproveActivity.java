package cn.gtgs.base.playpro.activity.center;

import cn.gtgs.base.playpro.activity.center.view.AnchorApproveDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class AnchorApproveActivity extends ActivityPresenter<AnchorApproveDelegate> {


    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected Class<AnchorApproveDelegate> getDelegateClass() {
        return AnchorApproveDelegate.class;
    }


}
