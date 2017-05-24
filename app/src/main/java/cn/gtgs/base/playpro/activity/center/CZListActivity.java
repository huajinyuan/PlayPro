
package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.TxListPresenter;
import cn.gtgs.base.playpro.activity.center.view.CZListDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class CZListActivity extends ActivityPresenter<CZListDelegate> {

    TxListPresenter presenter;

    @Override
    protected void onInitPresenters() {
        presenter = new TxListPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.init();
        presenter.getdata(false);
    }

    @Override
    protected Class<CZListDelegate> getDelegateClass() {
        return CZListDelegate.class;
    }

    @OnClick({R.id.img_topbar_back, R.id.tv_czlist_ing, R.id.tv_czlist_end})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_back:
                finish();
                break;
            case R.id.tv_czlist_ing:
                presenter.getdata(false);
                break;
            case R.id.tv_czlist_end:
                presenter.getdata(true);
                break;
        }
    }
}
