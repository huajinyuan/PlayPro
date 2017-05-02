package cn.gtgs.base.playpro.http;

import android.content.pm.ActivityInfo;

import com.qiniu.pili.droid.streaming.StreamingProfile;

/**
 * Created by gtgs on 2017/2/16.
 */

public interface Config {
    public static final boolean FILTER_ENABLED = false;
    public static final int ENCODING_LEVEL = StreamingProfile.VIDEO_ENCODING_HEIGHT_480;
    public static final int SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public static final String EXTRA_PUBLISH_URL_PREFIX = "URL:";
    public static final String EXTRA_PUBLISH_JSON_PREFIX = "JSON:";

    public static final String VERSION_HINT = "v2.0.1";

    public static final String EXTRA_KEY_PUB_URL = "pub_url";

    public static final String HINT_ENCODING_ORIENTATION_CHANGED =
            "Encoding orientation had been changed. Stop streaming first and restart streaming will take effect";
    /**
     * -------------------------------------------------------------------------------------------------------------------------
     */
//    //在线人数GET
    public static final String URL_OnlineNum = "https://api.yequtv.cn/v1/chatrooms/anchors/{anchor_id}/online_count?key=z45CasVgh8K3q6300g0d95VkK197291A";
//    //订台列表
    public static final String URL_BookingValid = "https://api.yequtv.cn/v1/anchor/bookings/valid?key=z45CasVgh8K3q6300g0d95VkK197291A";
//    public static final String URL_SMS = "https://api.yequtv.cn/v1/sms";
//    public static final String URL_LOGIN = "https://api.yequtv.cn/v1/anchors/login";

//    String BASE_URL = "http://foruo2o.com";
//    String REGISTER_URL = BASE_URL + "/wp-json/login/v1/wordpress/register";
//    String LOGIN_URL = BASE_URL + "/wp-json/login/v1/wordpress";
//    /**
//     * 产品列表
//     * */
//    String PRODUCTS_LIST_URL = BASE_URL + "/wp-json/wc/v1/products";
//    /**
//    * 单个产品
//    * */
//    String PRODUCTS_URL = PRODUCTS_LIST_URL + "/{id}";
//    /**
//     * 购物车
//     * */
//    String SHOPPINGCAR_URL = BASE_URL + "/wp-json/wc/v1/cart";
//    /**
//     * 当前用户
//     * */
//    String CURRENT_URL = BASE_URL + "/wp-json/wc/v1/customers/current";
//    /**
//     * 订单
//     * */
//    String ORDER_URL = BASE_URL + "/wp-json/wc/v1/orders";
//    /**
//     * 地址列表
//     */
//    String ADDRESS_URL = BASE_URL + "/wp-json/wc/v1/customers/address";
//    /**
//     * 单个地址
//     */
//    String ADDRESS_ACTION_URL = ADDRESS_URL + "/{id}";


    String BASE_URL = "http://www.jsonlan.com:8080/webcast";
    /**
     * 请求验证码
     * */
    String GET_CHECKCODE = BASE_URL + "/api/member/sms";
    /**
     * 登录接口
     * */
    String POST_LOGIN = BASE_URL + "/api/member/login";


}
