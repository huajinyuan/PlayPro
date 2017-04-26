package cn.gtgs.base.playpro.activity.home.model;

import java.io.Serializable;

/**
 * Created by gtgs on 2016/11/3.
 */

public class Coordinate implements Serializable {
    public double longitude, latitude;

    @Override
    public String toString() {
        return "Coordinate{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}