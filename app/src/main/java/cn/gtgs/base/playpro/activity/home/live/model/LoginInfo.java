package cn.gtgs.base.playpro.activity.home.live.model;

import java.io.Serializable;

/**
 * Created by zuoyun on 2016/10/18.
 */

public class LoginInfo implements Serializable {

    public String error;
    public String id;
    public String name;
    public String realname;
    public String avatar;
    public String place_id;
    public String gender;
    public String role;
    public String status;
    public String jiguang_id;
    public Coordinate coordinate;
    public String level;
    public String place;
    public String huanxin_username;
    public String huanxin_password;
    public String huanxin_chatroom_id;
    public Stream stream;
    public String credits;
    public String region_id;
    public String phone;
    public String age;
    public String weight;
    public String height;
    public String signature;
    public String token;
    public class Coordinate implements Serializable {
        public String longitude;
        public String latitude;
    }
    public class Stream implements Serializable {
        public String key;
        public String publish;
        public String play;
        public String snapshot;
    }
}