package cn.gtgs.base.playpro.activity.center.model;

import java.io.Serializable;

/**
 * Created by  on 2017/5/24.
 */

public class PicInfo implements Serializable {
    private String adImage;
    private String adType;
    private String createBy;
    private String adId;
    private String flag;
    private String createTime;
    private String updateBy;
    private String adStatus;
    private String adTitle;
    private String adUrl;
    private String updateTime;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PicInfo{" +
                "adId='" + adId + '\'' +
                ", adImage='" + adImage + '\'' +
                ", adType='" + adType + '\'' +
                ", createBy='" + createBy + '\'' +
                ", flag='" + flag + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", adStatus='" + adStatus + '\'' +
                ", adTitle='" + adTitle + '\'' +
                ", adUrl='" + adUrl + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
