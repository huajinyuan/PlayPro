package cn.gtgs.base.playpro.http;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2017/5/2.
 */

public class HttpBase<T> implements Serializable {
    public String msg;
    public int code;
    public T data;
    public ArrayList<T> datas;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ArrayList<T> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<T> datas) {
        this.datas = datas;
    }

}
