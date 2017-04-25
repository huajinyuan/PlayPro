package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.login.model.Account;

/**
 * Created by gtgs on 2017/2/10.
 */

public interface ILoginListener {
    void UserNameError();

    void PassWordError();

    void LoginSuccess(Account account);

    void LoginFailed(String msg);
}
