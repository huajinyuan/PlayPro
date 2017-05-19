package cn.gtgs.base.playpro.activity.login.model;

import java.io.Serializable;

/**
 * Created by gtgs on 2017/5/19.
 */

public class Appinfo implements Serializable {
    private String appVersion;
    private String createBy;
    private String flag;
    private String createTime;
    private String updateBy;
    private String appType;
    private String appId;
    private String appUrl;
    private String updateTime;
    private String appFile;
    private String appTitle;

    public String getAppFile() {
        return appFile;
    }

    public void setAppFile(String appFile) {
        this.appFile = appFile;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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
        return "Appinfo{" +
                "appFile='" + appFile + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", createBy='" + createBy + '\'' +
                ", flag='" + flag + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", appType='" + appType + '\'' +
                ", appId='" + appId + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", appTitle='" + appTitle + '\'' +
                '}';
    }
}
