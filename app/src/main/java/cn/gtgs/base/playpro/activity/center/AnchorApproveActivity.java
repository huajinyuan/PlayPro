package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.ApprovePresenter;
import cn.gtgs.base.playpro.activity.center.presenter.IApprove;
import cn.gtgs.base.playpro.activity.center.view.AnchorApproveDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class AnchorApproveActivity extends ActivityPresenter<AnchorApproveDelegate> {
    IApprove presenter;

    @Override
    protected void onInitPresenters() {
        presenter = new ApprovePresenter(viewDelegate);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected Class<AnchorApproveDelegate> getDelegateClass() {
        return AnchorApproveDelegate.class;
    }

    @OnClick({R.id.tv_approve_submit})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_approve_submit:
                presenter.Submit();
                break;
        }
    }

}
