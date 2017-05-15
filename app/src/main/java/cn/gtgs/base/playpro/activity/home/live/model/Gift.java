package cn.gtgs.base.playpro.activity.home.live.model;

import java.io.Serializable;

/**
 * Created by  on 2016/10/31.
 */

public class Gift implements Serializable {
    public String id, name, credits;
    public int picture;

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits='" + credits + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
