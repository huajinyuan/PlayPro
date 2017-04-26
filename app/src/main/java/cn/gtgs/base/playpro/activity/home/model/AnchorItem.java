package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

/**
 * Created by zuoyun on 2016/9/2.
 */
public class AnchorItem implements Serializable {
    public String id, name, realname, avatar, place_id, gender, role, status, level, place, count, count_name, huanxin_username, huanxin_password, huanxin_chatroom_id, live, credits, region_id, rank_change, online_count;
    public Coordinate coordinate;
    public Stream stream;

    @Override
    public String toString() {
        return "AnchorItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", realname='" + realname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", place_id='" + place_id + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", place='" + place + '\'' +
                ", count='" + count + '\'' +
                ", count_name='" + count_name + '\'' +
                ", huanxin_username='" + huanxin_username + '\'' +
                ", huanxin_password='" + huanxin_password + '\'' +
                ", huanxin_chatroom_id='" + huanxin_chatroom_id + '\'' +
                ", live='" + live + '\'' +
                ", credits='" + credits + '\'' +
                ", region_id='" + region_id + '\'' +
                ", rank_change='" + rank_change + '\'' +
                ", online_count='" + online_count + '\'' +
                ", coordinate=" + coordinate +
                ", stream=" + stream +
                '}';
    }
}
