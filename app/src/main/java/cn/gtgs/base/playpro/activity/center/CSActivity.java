package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.CSPresenter;
import cn.gtgs.base.playpro.activity.center.view.CSDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class CSActivity extends ActivityPresenter<CSDelegate> {
    CSPresenter presenter;

    @Override
    protected void onInitPresenters() {
        presenter = new CSPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.init();
        presenter.getinfo();
    }

    @Override
    protected Class getDelegateClass() {
        return CSDelegate.class;
    }
    @OnClick({R.id.img_topbar_back})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_topbar_back:
                finish();
                break;
        }
    }
}
