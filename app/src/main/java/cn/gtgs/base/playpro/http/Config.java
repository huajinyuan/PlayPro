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
    public static final String EXTRA_KEY_PUB_FOLLOW = "EXTRA_KEY_PUB_FOLLOW";

    public static final String HINT_ENCODING_ORIENTATION_CHANGED =
            "Encoding orientation had been changed. Stop streaming first and restart streaming will take effect";
    /**
     * -------------------------------------------------------------------------------------------------------------------------
     */
    /**
     * 加入聊天室
     */
    public static final String URL_JOINCHAT = "https://api.yequtv.cn/v1/chatrooms/anchors/{id}/join";
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

    String BASE = "http://www.jsonlan.com:8080/webcast/";

    String BASE_URL = BASE + "api/";
    /**
     * 请求验证码
     */
    String GET_CHECKCODE = BASE_URL + "member/sms";
    /**
     * 登录接口
     */
    String POST_LOGIN = BASE_URL + "member/login";
    /**
     * 直播列表
     */
    String POST_ANCHOR_LIST = BASE_URL + "anchor/list";
    /**
     * 在线直播列表
     */
    String POST_ANCHOR_LIST_LIVE = BASE_URL + "anchor/listLive";
    /**
     * 主播信息
     */
    String POST_ANCHOR_GET = BASE_URL + "anchor/get";
    /**
     * 土豪榜
     */
    String POST_MEMBER_TOP = BASE_URL + "member/top";
    /**
     * 明星榜
     */
    String POST_ANCHOR_TOP = BASE_URL + "anchor/top";
    /**
     * 关注（取消关注）
     */
    String POST_ANCHOR_MEMBER_fav = BASE_URL + "member/fav";
    /**
     * 开始直播
     */
    String POST_ANCHOR_OPEN = BASE_URL + "anchor/open";
    /**
     * 关注列表（params: mbId）
     */
    String POST_ANCHOR_MEMBER_FAVLIST = BASE_URL + "member/favList";
    /**
     * 主播认证（mbId=2&anQq=12345678&anSex&anPhoto）
     */
    String POST_ANCHOR_ADD = BASE_URL + "anchor/add";

    /**
     * 礼物赠送(mbId&anId&num)
     */
    String POST_MEMBER_SEND = BASE_URL + "member/send";
    /**
     * 图片上传（file）
     */
    String FileUpload = BASE_URL + "upload";

}
