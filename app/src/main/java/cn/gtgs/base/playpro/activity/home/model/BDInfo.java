package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

/**
 * Created by  on 2017/5/24.
 */

public class BDInfo implements Serializable {
    private String mbId;
    private String totalGold;
    private String mbPhoto;
    private String mbNickname;

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public String getMbNickname() {
        return mbNickname;
    }

    public void setMbNickname(String mbNickname) {
        this.mbNickname = mbNickname;
    }

    public String getMbPhoto() {
        return mbPhoto;
    }

    public void setMbPhoto(String mbPhoto) {
        this.mbPhoto = mbPhoto;
    }

    public String getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(String totalGold) {
        this.totalGold = totalGold;
    }

    @Override
    public String toString() {
        return "BDInfo{" +
                "mbId='" + mbId + '\'' +
                ", totalGold='" + totalGold + '\'' +
                ", mbPhoto='" + mbPhoto + '\'' +
                ", mbNickname='" + mbNickname + '\'' +
                '}';
    }
}
