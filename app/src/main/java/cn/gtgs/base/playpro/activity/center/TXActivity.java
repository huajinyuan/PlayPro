package cn.gtgs.base.playpro.activity.center;

import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.TxPresenter;
import cn.gtgs.base.playpro.activity.center.view.TXDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class TXActivity extends ActivityPresenter<TXDelegate> {

    TxPresenter presenter;
    @Override
    protected void onInitPresenters() {
        presenter = new TxPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.setmTvTitle();

    }

    @Override
    protected Class<TXDelegate> getDelegateClass() {
        return TXDelegate.class;
    }
    @OnClick({R.id.img_topbar_back,R.id.btn_tx_submit})
    public void Onclick(View v){
        switch (v.getId()){
            case R.id.img_topbar_back:
                finish();
                break;
            case R.id.btn_tx_submit:
                presenter.Submit();
                break;
        }
    }
}
