package cn.gtgs.base.playpro.activity.center;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.view.SettingDelegate;
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.utils.ACache;

public class SettingActivity extends ActivityPresenter<SettingDelegate> {


    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {
        viewDelegate.setmTvTitle("设置");
    }

    @Override
    protected Class getDelegateClass() {
        return SettingDelegate.class;
    }

    @OnClick({R.id.btn_cancel, R.id.img_topbar_back})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                ACache.get(this).clear();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                PApplication.getInstance().finishActivity();
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
        }

    }
}
