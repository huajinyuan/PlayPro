package cn.gtgs.base.playpro.activity.center;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.CenterPresenter;
import cn.gtgs.base.playpro.activity.center.view.CenterDelegate;
import cn.gtgs.base.playpro.activity.home.mymessage.MessageListActivity;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class CenterActivity extends ActivityPresenter<CenterDelegate> implements SwipeRefreshLayout.OnRefreshListener {
    CenterPresenter presenter;

    @Override
    protected void onInitPresenters() {
        presenter = new CenterPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.setmTvTitle("个人中心");
        viewDelegate.init();
        viewDelegate.getmSwp().setOnRefreshListener(this);
    }

    @Override
    protected Class<CenterDelegate> getDelegateClass() {
        return CenterDelegate.class;
    }

    @OnClick({R.id.rel_center_approve, R.id.rel_center_getcoin, R.id.rel_center_setting, R.id.img_topbar_back, R.id.rel_center_2edt, R.id.img_topbar_right})
    public void OnClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rel_center_approve:
                intent = new Intent(this, AnchorApproveActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_center_getcoin:
                intent = new Intent(this, GetCoinActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_center_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.rel_center_2edt:
                intent = new Intent(this, EdtInfoActivity.class);
                startActivity(intent);

                break;
            case R.id.img_topbar_right:
                intent = new Intent(this, MessageListActivity.class);
                startActivity(intent);

                break;
        }
    }


    @Override
    public void onRefresh() {
        presenter.doRefresh();
    }
}
