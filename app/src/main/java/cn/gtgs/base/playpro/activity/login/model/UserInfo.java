package cn.gtgs.base.playpro.activity.login.model;

import java.io.Serializable;

/**
 * Created by gtgs on 2017/5/2.
 */

public class UserInfo implements Serializable {
    public int mbId;
    public String mbPhone;
    public String flag;
    public String mbCode;
    public int mbAge;
    public String mbGoldPay;
    public int mbGold;
    public int MbLevel;
    public int auditAnchor;
    public int mbSex;
    public int auditVideo;
    public long createTime;
    public long updateTime;

    public int getAuditAnchor() {
        return auditAnchor;
    }

    public void setAuditAnchor(int auditAnchor) {
        this.auditAnchor = auditAnchor;
    }

    public int getAuditVideo() {
        return auditVideo;
    }

    public void setAuditVideo(int auditVideo) {
        this.auditVideo = auditVideo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getMbAge() {
        return mbAge;
    }

    public void setMbAge(int mbAge) {
        this.mbAge = mbAge;
    }

    public String getMbCode() {
        return mbCode;
    }

    public void setMbCode(String mbCode) {
        this.mbCode = mbCode;
    }

    public int getMbGold() {
        return mbGold;
    }

    public void setMbGold(int mbGold) {
        this.mbGold = mbGold;
    }

    public String getMbGoldPay() {
        return mbGoldPay;
    }

    public void setMbGoldPay(String mbGoldPay) {
        this.mbGoldPay = mbGoldPay;
    }

    public int getMbId() {
        return mbId;
    }

    public void setMbId(int mbId) {
        this.mbId = mbId;
    }

    public int getMbLevel() {
        return MbLevel;
    }

    public void setMbLevel(int mbLevel) {
        MbLevel = mbLevel;
    }

    public String getMbPhone() {
        return mbPhone;
    }

    public void setMbPhone(String mbPhone) {
        this.mbPhone = mbPhone;
    }

    public int getMbSex() {
        return mbSex;
    }

    public void setMbSex(int mbSex) {
        this.mbSex = mbSex;
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
                "auditAnchor=" + auditAnchor +
                ", mbId=" + mbId +
                ", mbPhone='" + mbPhone + '\'' +
                ", flag='" + flag + '\'' +
                ", mbCode='" + mbCode + '\'' +
                ", mbAge=" + mbAge +
                ", mbGoldPay='" + mbGoldPay + '\'' +
                ", mbGold=" + mbGold +
                ", MbLevel=" + MbLevel +
                ", mbSex=" + mbSex +
                ", auditVideo=" + auditVideo +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
