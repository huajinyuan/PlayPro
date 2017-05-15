package cn.gtgs.base.playpro.activity.login.presenter;


import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;

/**
 * Created by  on 2017/2/10.
 */

public interface IRegisterPresenter {
    void register(RegisterInfo registerInfo);
    void getCode();
}
