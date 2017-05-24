package cn.gtgs.base.playpro.activity.center;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.view.GetCoinDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;

public class GetCoinActivity extends ActivityPresenter<GetCoinDelegate> {

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void initData() {

        viewDelegate.init();
        Follow follow = (Follow) ACache.get(this).getAsObject(ACacheKey.CURRENT_ACCOUNT);

        if (null != follow) {
            UserInfo info = follow.getMember();
            if (null != info) {
                viewDelegate.setmTvGold(info.getMbGold() + "");
            }
        }
    }

    @Override
    protected Class getDelegateClass() {
        return GetCoinDelegate.class;
    }

    @OnClick({R.id.img_topbar_back, R.id.tv_getcoin_go2cs})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.tv_getcoin_go2cs:
//                Intent intent = new Intent(this, CSActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(this, QrCzhActivity.class);
                startActivity(intent);
                break;
        }
    }
}
