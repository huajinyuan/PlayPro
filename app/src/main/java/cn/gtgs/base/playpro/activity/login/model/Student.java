package cn.gtgs.base.playpro.activity.login.model;


import cn.gtgs.base.playpro.base.model.IModel;

/**
 * Created by gtgs on 2017/1/13.
 */

public class Student implements IModel {
    private String userName;
    private String sex;
    private int age;

    public Student(String userName, String sex, int age) {
        this.userName = userName;
        this.sex = sex;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
