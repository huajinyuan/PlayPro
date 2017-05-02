package cn.gtgs.base.playpro.activity.login.model;

import java.io.Serializable;

import cn.gtgs.base.playpro.base.model.BaseError;

/**
 * Created by gtgs on 2017/5/2.
 */

public class UserInfo extends BaseError implements Serializable {
    public String mbPhone;
    public String flag;
    public int mbId;
    public long createTime;
    public long updateTime;

    public String getMbPhone() {
        return mbPhone;
    }

    public void setMbPhone(String mbPhone) {
        this.mbPhone = mbPhone;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getMbId() {
        return mbId;
    }

    public void setMbId(int mbId) {
        this.mbId = mbId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "mbPhone='" + mbPhone + '\'' +
                ", flag='" + flag + '\'' +
                ", mbId=" + mbId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
