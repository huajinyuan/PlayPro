package cn.gtgs.base.playpro.activity.center;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.view.CenterDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class CenterActivity extends ActivityPresenter<CenterDelegate> {
    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {
        viewDelegate.setmTvTitle("个人中心");
        viewDelegate.init();
    }

    @Override
    protected Class<CenterDelegate> getDelegateClass() {
        return CenterDelegate.class;
    }

    @OnClick({R.id.rel_center_approve, R.id.rel_center_getcoin, R.id.rel_center_setting, R.id.img_topbar_back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.rel_center_approve:
                Intent intent = new Intent(this, AnchorApproveActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_center_getcoin:
                Intent intent2 = new Intent(this, GetCoinActivity.class);
                startActivity(intent2);
                break;
            case R.id.rel_center_setting:
                Intent intent3 = new Intent(this, SettingActivity.class);
                startActivity(intent3);
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
        }
    }
}
