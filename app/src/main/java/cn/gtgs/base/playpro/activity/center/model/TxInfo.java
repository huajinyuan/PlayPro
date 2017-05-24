package cn.gtgs.base.playpro.activity.center.model;

import java.io.Serializable;

/**
 * Created by  on 2017/5/24.
 */

public class TxInfo implements Serializable {
    private String exchargeType;
    private String tradeAmount;
    private String flag;
    private String anId;
    private long createTime;
    private String finalAmount;
    private String initialAmount;
    private String contactType;
    private long updateTime;
    private String id;
    private String status;
    private String updateBy;

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getExchargeType() {
        return exchargeType;
    }

    public void setExchargeType(String exchargeType) {
        this.exchargeType = exchargeType;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(String initialAmount) {
        this.initialAmount = initialAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "TxInfo{" +
                "anId='" + anId + '\'' +
                ", exchargeType='" + exchargeType + '\'' +
                ", tradeAmount='" + tradeAmount + '\'' +
                ", flag='" + flag + '\'' +
                ", createTime='" + createTime + '\'' +
                ", finalAmount='" + finalAmount + '\'' +
                ", initialAmount='" + initialAmount + '\'' +
                ", contactType='" + contactType + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}
