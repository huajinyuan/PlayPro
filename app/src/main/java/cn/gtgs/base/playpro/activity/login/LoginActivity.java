package cn.gtgs.base.playpro.activity.login;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.activity.login.model.Account;
import cn.gtgs.base.playpro.activity.login.presenter.ILoginListener;
import cn.gtgs.base.playpro.activity.login.presenter.ILoginPresenter;
import cn.gtgs.base.playpro.activity.login.presenter.LoginPresenter;
import cn.gtgs.base.playpro.activity.login.presenter.Student2MainBinder;
import cn.gtgs.base.playpro.activity.login.view.LoginDelegate;
import cn.gtgs.base.playpro.base.presenter.databind.DataBindActivity;
import cn.gtgs.base.playpro.base.presenter.databind.DataBinder;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.ToastUtil;

public class LoginActivity extends DataBindActivity<LoginDelegate> implements ILoginListener {
    ILoginPresenter mLoginPresenter;

    @Override
    protected Class<LoginDelegate> getDelegateClass() {
        return LoginDelegate.class;
    }

    @OnClick({R.id.btn_login_admin, R.id.tv_login_go2register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_admin:
                mLoginPresenter.login(viewDelegate, this);
                break;
            case R.id.tv_login_go2register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onInitPresenters() {
        mLoginPresenter = new LoginPresenter();
    }

    @Override
    protected void initData() {

        Account account = (Account) mACache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        if (null != account) {
            this.finish();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public DataBinder getDataBinder() {
        return new Student2MainBinder();
    }

    @Override
    public void UserNameError() {
        ToastUtil.showToast("UserNameError", this);
    }

    @Override
    public void PassWordError() {
        ToastUtil.showToast("PassWordError", this);
    }

    @Override
    public void LoginSuccess(Account account) {
        ToastUtil.showToast("LoginSuccess", this);
        mACache.put(ACacheKey.CURRENT_ACCOUNT, account);
        this.finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    @Override
    public void LoginFailed(String msg) {
        ToastUtil.showToast(msg, this);
    }

}
