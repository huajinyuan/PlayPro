package cn.gtgs.base.playpro.activity.login.model;


import java.io.Serializable;

public class RegisterInfo implements Serializable {

    public String name;
    public String gender;
    public String avatar_path;

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "avatar_path='" + avatar_path + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
