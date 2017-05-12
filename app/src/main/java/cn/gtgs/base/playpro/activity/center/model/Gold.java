package cn.gtgs.base.playpro.activity.center.model;

import java.io.Serializable;

/**
 * Created by hjy on 17/5/12.
 */

public class Gold implements Serializable {

    private String gold;
    private String value;
    private String gifts;

    public Gold(String gold, String gifts, String value) {
        this.gold = gold;
        this.value = value;
        this.gifts = gifts;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGifts() {
        return gifts;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        return "Gold{" +
                "gold='" + gold + '\'' +
                ", value='" + value + '\'' +
                ", gifts='" + gifts + '\'' +
                '}';
    }
}
