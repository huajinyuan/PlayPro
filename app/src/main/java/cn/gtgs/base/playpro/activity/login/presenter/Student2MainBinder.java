package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.login.model.User;
import cn.gtgs.base.playpro.activity.login.view.LoginDelegate;
import cn.gtgs.base.playpro.base.presenter.databind.DataBinder;

/**
 * Created by gtgs on 2017/1/13.
 */

public class Student2MainBinder implements DataBinder<LoginDelegate,User> {
    @Override
    public void viewBindModel(LoginDelegate viewDelegate, User data) {
//        viewDelegate.setTip(data.getUserName());
    }

}
