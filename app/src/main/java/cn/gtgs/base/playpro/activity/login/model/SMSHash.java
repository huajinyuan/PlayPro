package cn.gtgs.base.playpro.activity.login.model;

import java.io.Serializable;

/**
 * Created by  on 2016/10/13.
 */

public class SMSHash implements Serializable {
    public String error;
    public String sms_hash;

    @Override
    public String toString() {
        return "SMSHash{" +
                "error='" + error + '\'' +
                ", sms_hash='" + sms_hash + '\'' +
                '}';
    }
}
