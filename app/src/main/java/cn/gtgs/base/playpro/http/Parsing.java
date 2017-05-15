package cn.gtgs.base.playpro.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;

//import cn.gtgs.base.playpro.base.model.BaseError;

/**
 * Created by  on 2017/2/17.
 */

public class Parsing {
    private static Parsing parsing;

    public static Parsing getInstance() {
        if (parsing == null) {
            parsing = new Parsing();
        }
        return parsing;
    }

    /**
     * 判断返回状态（非200为请求失败）
     */
    public boolean checkNotError(Response response) {
        return response.code() == 200;
    }

    /**
     * 解析错误信息
     */
//    public CheckError parsingError(Response response, final Context context) {
//        CheckError checkError = new CheckError();
//        if (response.code() == 401) {
////            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
////                    .setTitleText("Login has expired")
////                    .setContentText("Return to the login page")
////                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                        @Override
////                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//////                            OApplication.getInstance().finishAll();
//////                            Intent intent = new Intent(context, LoginActivity.class);
//////                            context.startActivity(intent);
////                        }
////                    })
////                    .show();
//            return null;
//        } else if (response.code() == 200) {
//            checkError.setSuccess(true);
//        } else {
//            BaseError baseError = new BaseError();
//            try {
//                String Str = response.body().string();
//                JSONObject json = JSON.parseObject(Str);
//                String msg = null;
//                String data = null;
//                if (json.containsKey("message")) {
//                    msg = json.getString("message");
//                }
//                if (json.containsKey("data")) {
//                    data = json.getString("data");
//                }
//                baseError.setError(response.code(), msg, data);
//                checkError.setSuccess(false);
//                checkError.setBaseError(baseError);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return checkError;
//    }

    /**
     * 解析对象
     */
    public <T> T ResponseToSimPle(Response response, Class<T> current) {
        T t = null;
        try {
            String Str = response.body().string();
            t = JSON.parseObject(Str, current);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 解析对象
     */
    public <T> HttpBase<T> ResponseToObject(Response response, Class<T> current) {
        HttpBase<T> tb = new HttpBase<>();
        try {
            String Str = response.body().string();
            F.e("-----------------" + Str);
            JSONObject ob = JSON.parseObject(Str);
            tb.setCode(ob.getInteger("code"));
            tb.setMsg(ob.getString("msg"));
            if (ob.containsKey("data")) {
                T t = JSON.parseObject(ob.getString("data"), current);
                tb.setData(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tb;
    }

    /**
     * 解析列表
     */
    public <T> BaseList<T> ResponseToList2(Response response, Class<T> current) {
        BaseList<T> ls = new BaseList<>();
        String Str = null;
        try {
            Str = response.body().string();
            JSONObject ob = JSON.parseObject(Str);
            F.e("-----------------------------" + ob.toJSONString());
            if (ob.containsKey("data")) {
                JSONObject b = ob.getJSONObject("data");
                ls.setPage(b.getInteger("page"));
                ls.setTotalCount(b.getInteger("totalCount"));
                ls.setTotalPage(b.getInteger("totalPage"));
                if (b.containsKey("dataList")) {
                    List<T> r = JSON.parseArray(b.getJSONArray("dataList").toJSONString(), current);
                    ls.setDataList(r);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ls;
    }

    /**
     * 解析列表3
     */
    public <T> BaseList<T> ResponseToList3(Response response, Class<T> current) {
        BaseList<T> ls = new BaseList<>();
        String Str = null;
        try {
            Str = response.body().string();
            JSONObject ob = JSON.parseObject(Str);
            if (ob.containsKey("dataList")) {
                List<T> r = JSON.parseArray(ob.getJSONArray("dataList").toJSONString(), current);
                ls.setDataList(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ls;
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
//
//    /**
//     * 解析对象
//     */
//    public <T> HttpBase<T> ResponseToObj(Response response, Class<T> u) throws Exception {
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
//        if (null == u || u == Void.class) {
//            //无数据类型,表示没有data数据的情况（以  new DialogCallback<LzyResponse<Void>>(this)  以这种形式传递的泛型)
//            SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
//            response.close();
//            //noinspection unchecked
//            return simpleResponse.toBaseResponse();
//        } else {
//            //有数据类型，表示有data
//            HttpBase baseResponse = Convert.fromJson(jsonReader, HttpBase.class);
//            response.close();
//            int code = baseResponse.code;
//            //这里的0是以下意思
//            //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
//            if (code == 1) {
//                //noinspection unchecked
//                return baseResponse;
//            } else if (code == 104) {
//                //比如：用户授权信息无效，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//                throw new IllegalStateException("用户授权信息无效");
//            } else if (code == 105) {
//                //比如：用户收取信息已过期，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//                throw new IllegalStateException("用户收取信息已过期");
//            } else if (code == 106) {
//                //比如：用户账户被禁用，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//                throw new IllegalStateException("用户账户被禁用");
//            } else if (code == 300) {
//                //比如：其他乱七八糟的等，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//                throw new IllegalStateException("其他乱七八糟的等");
//            } else {
//                throw new IllegalStateException("错误代码：" + code + "，错误信息：" + baseResponse.msg);
//            }
//        }
//    }
//
//    /**
//     * 解析列表
//     */
//    public <T> HttpBase<ArrayList<T>> ResponseToList(Response response, Class<T> u) throws Exception {
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
//        //有数据类型，表示有data
////        HttpBase baseResponse = Convert.fromJson(jsonReader, new TypeToken<ArrayList<T>>() {
////        }.getType());
//        HttpBase baseResponse = Convert.fromJson(jsonReader, HttpBase.class);
//        response.close();
//        int code = baseResponse.code;
//        //这里的0是以下意思
//        //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
//        if (code == 1) {
//            //noinspection unchecked
//            return baseResponse;
//        } else if (code == 104) {
//            //比如：用户授权信息无效，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//            throw new IllegalStateException("用户授权信息无效");
//        } else if (code == 105) {
//            //比如：用户收取信息已过期，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//            throw new IllegalStateException("用户收取信息已过期");
//        } else if (code == 106) {
//            //比如：用户账户被禁用，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//            throw new IllegalStateException("用户账户被禁用");
//        } else if (code == 300) {
//            //比如：其他乱七八糟的等，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
//            throw new IllegalStateException("其他乱七八糟的等");
//        } else {
//            throw new IllegalStateException("错误代码：" + code + "，错误信息：" + baseResponse.msg);
//        }
//    }


}
