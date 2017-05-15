package cn.gtgs.base.playpro.activity.login.presenter;//package cn.gtgs.base.playpro.activity.login.presenter;


//import cn.gtgs.base.playpro.activity.login.model.Account;

import cn.gtgs.base.playpro.activity.home.model.Follow;

/**
 * Created by  on 2017/2/10.
 */

public interface IRegisterListener {
    void AccountError();

    void PassWordError();

    void PassWordDifferent();

    void RegisterSuccess(Follow account);

    void RegisterFailed(String msg);
}
