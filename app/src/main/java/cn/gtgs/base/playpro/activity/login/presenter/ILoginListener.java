package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.login.model.UserInfo;

/**
 * Created by gtgs on 2017/2/10.
 */

public interface ILoginListener {
    void UserNameError();

    void PassWordError();

    void LoginSuccess(UserInfo userInfo);

    void LoginFailed(String msg);
}
