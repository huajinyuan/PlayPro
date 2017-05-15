package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

/**
 * Created by  on 2016/11/23.
 */

public class Stream implements Serializable {
    public String key, publish, play, snapshot;

    @Override
    public String toString() {
        return "Stream{" +
                "key='" + key + '\'' +
                ", publish='" + publish + '\'' +
                ", play='" + play + '\'' +
                ", snapshot='" + snapshot + '\'' +
                '}';
    }
}