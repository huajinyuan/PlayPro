package cn.gtgs.base.playpro.activity.center;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.view.MySyDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class MySyActivity extends ActivityPresenter<MySyDelegate> {


    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {
        viewDelegate.init();
    }

    @Override
    protected Class<MySyDelegate> getDelegateClass() {
        return MySyDelegate.class;
    }

    @OnClick({R.id.img_topbar_back, R.id.tv_topbar_right, R.id.tv_sy_tx})
    public void Onclick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.tv_topbar_right:
                intent = new Intent(this, CZListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sy_tx:
                intent = new Intent(this, TXActivity.class);
                startActivity(intent);
                break;
        }
    }
}
