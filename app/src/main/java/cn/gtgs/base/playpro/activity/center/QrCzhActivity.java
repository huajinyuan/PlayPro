package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.QrCzhPresenter;
import cn.gtgs.base.playpro.activity.center.view.QrCzhDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class QrCzhActivity extends ActivityPresenter<QrCzhDelegate> {

    QrCzhPresenter presenter;

    @Override
    protected void onInitPresenters() {

        presenter = new QrCzhPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.init();
        presenter.getPic();
    }

    @Override
    protected Class<QrCzhDelegate> getDelegateClass() {
        return QrCzhDelegate.class;
    }

    @OnClick({R.id.img_topbar_back})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
        }
    }
}
