package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

/**
 * 描述：广告信息</br>
 */
public class ADInfo implements Serializable {

    String adImage = "";
    String adId = "";
    String flag = "";
    String adStatus = "";
    String adTitle = "";
    String adUrl = "";
    String adSort = "";

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

    public String getAdSort() {
        return adSort;
    }

    public void setAdSort(String adSort) {
        this.adSort = adSort;
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

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ADInfo{" +
                "adId='" + adId + '\'' +
                ", adImage='" + adImage + '\'' +
                ", flag='" + flag + '\'' +
                ", adStatus='" + adStatus + '\'' +
                ", adTitle='" + adTitle + '\'' +
                ", adUrl='" + adUrl + '\'' +
                ", adSort='" + adSort + '\'' +
                '}';
    }
}
