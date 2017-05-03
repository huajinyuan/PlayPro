package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;

/**
 * Created by gtgs on 2017/2/10.
 */

public interface ILoginPresenter {
    void login(RegisterInfo registerInfo);

    void getCode();
}
