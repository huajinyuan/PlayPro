package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.home.model.Follow;

/**
 * Created by  on 2017/2/10.
 */

public interface ILoginListener {
    void UserNameError();

    void PassWordError();

    void LoginSuccess(Follow userInfo);

    void LoginFailed(String msg);
}
