package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.view.GetCoinDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class GetCoinActivity extends ActivityPresenter<GetCoinDelegate> {

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {
        viewDelegate.setmTvTitle("我的金币");
    }

    @Override
    protected Class getDelegateClass() {
        return GetCoinDelegate.class;
    }

    @OnClick(R.id.img_topbar_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
        }
    }
}
