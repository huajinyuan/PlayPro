package cn.gtgs.base.playpro.activity.login;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;
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

    boolean isRegist = false;
    RegisterInfo registerInfo;

    @Override
    protected Class<LoginDelegate> getDelegateClass() {
        return LoginDelegate.class;
    }

    @OnClick({R.id.bt_login, R.id.btn_register, R.id.img_topbar_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                mLoginPresenter.login();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, RegisterIconActivity.class);
                startActivity(intent);
//                startActivityForResult(intent, 999);
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onInitPresenters() {
        mLoginPresenter = new LoginPresenter(viewDelegate, this);
    }

    @Override
    protected void initData() {

        viewDelegate.setTitle();
//        UserInfo info = (UserInfo) mACache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
//        if (null != info) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            this.finish();
//        }
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
    public void LoginSuccess(Follow userInfo) {
        mACache.put(ACacheKey.CURRENT_ACCOUNT, userInfo);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void LoginFailed(String msg) {
        ToastUtil.showToast(msg, this);
    }

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            if (requestCode == 999) {
//                int three = data.getIntExtra("three", 0);
//                //设置结果显示框的显示数值
//                result.setText(String.valueOf(three));
                registerInfo = (RegisterInfo) data.getSerializableExtra("RegisterInfo");

            }
        }
    }
}
