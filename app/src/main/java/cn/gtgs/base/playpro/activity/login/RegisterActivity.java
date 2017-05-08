package cn.gtgs.base.playpro.activity.login;//package cn.gtgs.base.playpro.activity.login;
//
//import android.content.Intent;
//import android.view.View;
//
//import cn.gtgs.base.playpro.activity.home.HomeActivity;
//import cn.gtgs.base.playpro.activity.login.model.Account;
//import cn.gtgs.base.playpro.activity.login.presenter.IRegisterListener;
//import cn.gtgs.base.playpro.activity.login.presenter.IRegisterPresenter;
//import cn.gtgs.base.playpro.activity.login.presenter.RegisterPresenter;
//import cn.gtgs.base.playpro.activity.login.view.RegisterDelegate;
//import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
//import cn.gtgs.base.playpro.utils.ACacheKey;
//import cn.gtgs.base.playpro.utils.ToastUtil;
//
//public class RegisterActivity extends ActivityPresenter<RegisterDelegate> implements IRegisterListener {
//    IRegisterPresenter mPresenter;
//
//
//    @Override
//    protected void onInitPresenters() {
//        mPresenter = new RegisterPresenter();
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected Class getDelegateClass() {
//        return RegisterDelegate.class;
//    }
//
////    @OnClick({R.id.btn_register_admin})
//    public void Onclick(View v) {
//        switch (v.getId()) {
////            case R.id.btn_register_admin:
////                mPresenter.register(viewDelegate, this);
////                break;
//        }
//    }
//
//    @Override
//    public void AccountError() {
//        ToastUtil.showToast("AccountError", this);
//    }
//
//    @Override
//    public void PassWordError() {
//        ToastUtil.showToast("PassWordError", this);
//    }
//
//    @Override
//    public void PassWordDifferent() {
//        ToastUtil.showToast("PassWordDifferent", this);
//    }
//
//    @Override
//    public void RegisterSuccess(Account account) {
//        mACache.put(ACacheKey.CURRENT_ACCOUNT, account);
//        ToastUtil.showToast("LoginSuccess", this);
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void RegisterFailed(String msg) {
//        ToastUtil.showToast(msg, this);
//    }
//}
