package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

import cn.gtgs.base.playpro.activity.login.model.UserInfo;

/**
 * Created by  on 2017/5/4.
 */

public class Follow implements Serializable {
    public String anQq;
    public String isRecommend;
    public String anRemark;
    public String flag;
    public String mbId;
    public String anId;
    public String wcPushAddress;
    public String qqAudit;
    public String wcKey;
    public long updateTime;
    public long createTime;
    public String anStatus;
    public String faCount;
    public String updateBy;
    public String chatRoomId;
    public String wcPullAddress;
    public String anSex;
    public String liveCount;
    public String faId;
    public String anGold;
    public String anGoldGet;
    public String liveStatus;
    public String anPhoto;
    public String sysMsg;
    public String wordLimit;
    public int dayGold = 0;
    public double anGoldAble = 0;
    public int anPrice;

    public UserInfo member;

    public String getAnGold() {
        return anGold;
    }

    public void setAnGold(String anGold) {
        this.anGold = anGold;
    }

    public String getAnGoldGet() {
        return anGoldGet;
    }

    public void setAnGoldGet(String anGoldGet) {
        this.anGoldGet = anGoldGet;
    }

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getAnQq() {
        return anQq;
    }

    public void setAnQq(String anQq) {
        this.anQq = anQq;
    }

    public String getAnSex() {
        return anSex;
    }

    public void setAnSex(String anSex) {
        this.anSex = anSex;
    }

    public String getAnStatus() {
        return anStatus;
    }

    public void setAnStatus(String anStatus) {
        this.anStatus = anStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFaId() {
        return faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(String liveCount) {
        this.liveCount = liveCount;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public UserInfo getMember() {
        return member;
    }

    public void setMember(UserInfo member) {
        this.member = member;
    }

    public String getQqAudit() {
        return qqAudit;
    }

    public void setQqAudit(String qqAudit) {
        this.qqAudit = qqAudit;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getWcKey() {
        return wcKey;
    }

    public void setWcKey(String wcKey) {
        this.wcKey = wcKey;
    }

    public String getWcPullAddress() {
        return wcPullAddress;
    }

    public void setWcPullAddress(String wcPullAddress) {
        this.wcPullAddress = wcPullAddress;
    }

    public String getWcPushAddress() {
        return wcPushAddress;
    }

    public void setWcPushAddress(String wcPushAddress) {
        this.wcPushAddress = wcPushAddress;
    }

    public String getAnRemark() {
        return anRemark;
    }

    public void setAnRemark(String anRemark) {
        this.anRemark = anRemark;
    }

    public String getFaCount() {
        return faCount;
    }

    public void setFaCount(String faCount) {
        this.faCount = faCount;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getAnPhoto() {
        return anPhoto;
    }

    public void setAnPhoto(String anPhoto) {
        this.anPhoto = anPhoto;
    }

    public double getAnGoldAble() {
        return anGoldAble;
    }

    public void setAnGoldAble(double anGoldAble) {
        this.anGoldAble = anGoldAble;
    }

    public int getAnPrice() {
        return anPrice;
    }

    public void setAnPrice(int anPrice) {
        this.anPrice = anPrice;
    }

    public int getDayGold() {
        return dayGold;
    }

    public void setDayGold(int dayGold) {
        this.dayGold = dayGold;
    }

    public String getSysMsg() {
        return sysMsg;
    }

    public void setSysMsg(String sysMsg) {
        this.sysMsg = sysMsg;
    }

    public String getWordLimit() {
        return wordLimit;
    }

    public void setWordLimit(String wordLimit) {
        this.wordLimit = wordLimit;
    }


    @Override
    public String toString() {
        return "Follow{" +
                "anQq='" + anQq + '\'' +
                ", isRecommend='" + isRecommend + '\'' +
                ", anRemark='" + anRemark + '\'' +
                ", flag='" + flag + '\'' +
                ", mbId='" + mbId + '\'' +
                ", anId='" + anId + '\'' +
                ", wcPushAddress='" + wcPushAddress + '\'' +
                ", qqAudit='" + qqAudit + '\'' +
                ", wcKey='" + wcKey + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", anStatus='" + anStatus + '\'' +
                ", faCount='" + faCount + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", chatRoomId='" + chatRoomId + '\'' +
                ", wcPullAddress='" + wcPullAddress + '\'' +
                ", anSex='" + anSex + '\'' +
                ", liveCount='" + liveCount + '\'' +
                ", faId='" + faId + '\'' +
                ", anGold='" + anGold + '\'' +
                ", anGoldGet='" + anGoldGet + '\'' +
                ", liveStatus='" + liveStatus + '\'' +
                ", anPhoto='" + anPhoto + '\'' +
                ", sysMsg='" + sysMsg + '\'' +
                ", wordLimit='" + wordLimit + '\'' +
                ", dayGold=" + dayGold +
                ", anGoldAble=" + anGoldAble +
                ", anPrice=" + anPrice +
                ", member=" + member +
                '}';
    }
}
