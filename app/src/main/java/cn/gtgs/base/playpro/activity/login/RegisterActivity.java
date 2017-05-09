package cn.gtgs.base.playpro.activity.login;//package cn.gtgs.base.playpro.activity.login;//package cn.gtgs.base.playpro.activity.login;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;
import cn.gtgs.base.playpro.activity.login.presenter.IRegisterListener;
import cn.gtgs.base.playpro.activity.login.presenter.IRegisterPresenter;
import cn.gtgs.base.playpro.activity.login.presenter.RegisterPresenter;
import cn.gtgs.base.playpro.activity.login.view.RegisterDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.ToastUtil;

public class RegisterActivity extends ActivityPresenter<RegisterDelegate> implements IRegisterListener {
    IRegisterPresenter mPresenter;
    RegisterInfo registerInfo;

    @Override
    protected void onInitPresenters() {
        mPresenter = new RegisterPresenter(viewDelegate, this);
    }

    @Override
    protected void initData() {
        registerInfo = (RegisterInfo) getIntent().getSerializableExtra("RegisterInfo");
    }

    @Override
    protected Class getDelegateClass() {
        return RegisterDelegate.class;
    }

    @OnClick({R.id.btn_register, R.id.tv_login_sms, R.id.img_topbar_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (null != registerInfo) {
                    mPresenter.register(registerInfo);
                }
                break;
            case R.id.tv_login_sms:
                mPresenter.getCode();
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
        }
    }


    @Override
    public void AccountError() {
        ToastUtil.showToast("AccountError", this);
    }

    @Override
    public void PassWordError() {
        ToastUtil.showToast("PassWordError", this);
    }

    @Override
    public void PassWordDifferent() {
        ToastUtil.showToast("PassWordDifferent", this);
    }

    @Override
    public void RegisterSuccess(Follow account) {
        mACache.put(ACacheKey.CURRENT_ACCOUNT, account);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void RegisterFailed(String msg) {
        ToastUtil.showToast(msg, this);
    }


}
