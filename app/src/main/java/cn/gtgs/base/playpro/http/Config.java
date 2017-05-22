package cn.gtgs.base.playpro.http;

import android.content.pm.ActivityInfo;

import com.qiniu.pili.droid.streaming.StreamingProfile;

/**
 * Created by  on 2017/2/16.
 */

public interface Config {
    boolean FILTER_ENABLED = false;
    int ENCODING_LEVEL = StreamingProfile.VIDEO_ENCODING_HEIGHT_480;
    int SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    String EXTRA_PUBLISH_URL_PREFIX = "URL:";
    String EXTRA_PUBLISH_JSON_PREFIX = "JSON:";

    String VERSION_HINT = "v2.0.1";

    String EXTRA_KEY_PUB_URL = "pub_url";
    String EXTRA_KEY_PUB_FOLLOW = "EXTRA_KEY_PUB_FOLLOW";

    String HINT_ENCODING_ORIENTATION_CHANGED =
            "Encoding orientation had been changed. Stop streaming first and restart streaming will take effect";
    /**
     * -------------------------------------------------------------------------------------------------------------------------
     */

//    String BASE = "http://www.vip177.cn:8080/webcast/";
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
     * 注册接口
     */
    String POST_REG = BASE_URL + "member/reg";
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
     * 个人信息
     */
    String POST_MEMBER_GET = BASE_URL + "member/get";
    /**
     * 个人信息修改
     */
    String POST_MEMBER_EDIT = BASE_URL + "member/edit";
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
     * 搜索
     */
    String POST_SEARCH = BASE_URL + "anchor/search";
    /**
     * 图片上传（file）
     */
    String FileUpload = BASE_URL + "upload";
    /**
     * 广告
     */
    String GET_ADVERTS = BASE_URL + "common/adverts";
    /**
     * 添加好友
     * userName=1&friendName=2
     */
    String MEMBER_ADDFRIEND = BASE_URL + "member/addFriend";
    String MEMBER_LIVESTATUS = BASE_URL + "anchor/liveStatus";
    /***/
    String MEMBER_CSLIST = BASE_URL + "common/attendants";
//    ?appType=2
    String CHECK_VERSION = BASE_URL + "app/update";

}
