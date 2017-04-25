package cn.gtgs.base.playpro.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.gtgs.base.playpro.base.model.BaseError;
import cn.gtgs.base.playpro.base.model.CheckError;
import cn.gtgs.base.playpro.widget.SweetAlert.SweetAlertDialog;
import okhttp3.Response;

/**
 * Created by gtgs on 2017/2/17.
 */

public class Parsing {
    private static Parsing parsing;

    public static Parsing getInstance() {
        if (parsing == null) {
            parsing = new Parsing();
        }
        return parsing;
    }
//
//    /**
//     * 判断返回状态（非200为请求失败）
//     */
//    public boolean checkNotError(Response response) {
//        return response.code() == 200;
//    }

    /**
     * 解析错误信息
     */
    public CheckError parsingError(Response response, final Context context) {
        CheckError checkError = new CheckError();
        if (response.code() == 401) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Login has expired")
                    .setContentText("Return to the login page")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            OApplication.getInstance().finishAll();
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            context.startActivity(intent);
                        }
                    })
                    .show();
            return null;
        } else if (response.code() == 200) {
            checkError.setSuccess(true);
        } else {
            BaseError baseError = new BaseError();
            try {
                String Str = response.body().string();
                JSONObject json = JSON.parseObject(Str);
                String msg = null;
                String data = null;
                if (json.containsKey("message")) {
                    msg = json.getString("message");
                }
                if (json.containsKey("data")) {
                    data = json.getString("data");
                }
                baseError.setError(response.code(), msg, data);
                checkError.setSuccess(false);
                checkError.setBaseError(baseError);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return checkError;
    }

    /**
     * 解析对象
     */
    public <T extends BaseError> T ResponseToObject(Response response, Class<T> current, Context context) {
        T t = null;
        try {
            t = current.newInstance();
            if (response.code() == 200) {
                try {
                    String Str = response.body().string();
                    JSON json = JSON.parseObject(Str);
                    t = json.toJavaObject(current);
                    t.setCode(response.code());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                CheckError checkerror = parsingError(response, context);
                t.setError(checkerror.getBaseError().getCode(), checkerror.getBaseError().getMessage(), checkerror.getBaseError().getData());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 列表解析
     */
    public <T> ArrayList<T> ResponseToList(Response response, Class<T> u) {
        ArrayList<T> t = new ArrayList<>();
        try {
            String Str = response.body().string();
            t = (ArrayList<T>) JSON.parseArray(Str, u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }


//    /**
//     * 购物车列表解析
//     */
//    public ArrayList<ShopCarGoods> ResponseToShoppingCar(Response response) {
//        ArrayList<ShopCarGoods> goodses = null;
//        try {
//            String Str = response.body().string();
//            JSONObject object = JSON.parseObject(Str);
//            goodses = (ArrayList<ShopCarGoods>) JSON.parseArray(object.getJSONArray("products").toJSONString(), ShopCarGoods.class);
//            F.e(goodses.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return goodses;
//    }


}
