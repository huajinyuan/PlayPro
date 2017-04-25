package cn.gtgs.base.playpro.activity.login.model;


import cn.gtgs.base.playpro.base.model.IModel;

/**
 * Created by gtgs on 2017/2/10.
 */

public class User implements IModel {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
