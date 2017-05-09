package cn.gtgs.base.playpro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gt.okgo.OkGo;
import com.gt.okgo.cache.CacheEntity;
import com.gt.okgo.cache.CacheMode;
import com.gt.okgo.cookie.store.PersistentCookieStore;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.qiniu.pili.droid.streaming.StreamingEnv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.activity.home.live.model.Gift;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;

/**
 * Created by gtgs on 2017/4/26.
 */

public class PApplication extends Application {
    private static PApplication application;
    public static List<String> emoticonList = new ArrayList<String>();
    public static Map<String, Integer> emoticonsIdMap = new HashMap<String, Integer>();
    public static List<String> emoticonKeyList = new ArrayList<String>();
    public static Context applicationContext;
    public static String Phone;
    public static String JPushID;
    public ArrayList<Activity> mActiviyts = new ArrayList<>();
    public ArrayList<String> mF = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
//        UnCeHandler catchExcep = new UnCeHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        application = this;
        applicationContext = this;
        OkGo.init(this);
        StreamingEnv.init(getApplicationContext());
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//                .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                               //方法一：信任所有证书,不安全有风险
//                    .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//                    //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//                    .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//                    .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers)  //设置全局公共头
//                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }

        EMClient.getInstance().init(applicationContext, initChatOptions());
        EMClient.getInstance().setDebugMode(true);
        initEmotions();
    }

    public static PApplication getInstance() {
        return application;
    }

    /**
     * 初始化表情 list
     */
    private void initEmotions() {

        emoticonList.addAll(Arrays.asList(this.getResources().getStringArray(R.array.emoticos)));
        emoticonKeyList.addAll(Arrays.asList(this.getResources().getStringArray(R.array.emoticoKeys)));
        for (int i = 0; i < emoticonKeyList.size(); i++) {
            int emoticonsId = getResources().getIdentifier(emoticonList.get(i), "drawable", getPackageName());
            emoticonsIdMap.put(emoticonKeyList.get(i), emoticonsId);
        }
    }

    private EMOptions initChatOptions() {
        Log.d("DemoHelper", "init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        return options;
    }


    public void finishActivity() {
        for (Activity a : mActiviyts) {
            a.finish();
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
//            finishActivity();
            restartApp();//发生崩溃异常时,重启应用
        }
    };

    public void restartApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    public ArrayList<Gift> getGift() {
        ArrayList<Gift> gifts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Gift g = new Gift();
            g.setId(i + "");
            g.setCredits(i + "");
            int emoticonsId = getResources().getIdentifier("icon_" + i, "mipmap", getPackageName());
            g.setPicture(emoticonsId);
            gifts.add(g);
        }
        return gifts;
    }

    public Gift getGiftObject(String id) {
        for (Gift g : getGift()) {
            if (g.id.equals(id)) {
                return g;
            }
        }
        return null;
    }
    public void getMF()
    {

        try {
            String s = ACache.get(this).getAsString(ACacheKey.CURRENT_FOLLOW);
            if (null!=s)
            {
                ArrayList<String> ss = (ArrayList<String>) JSON.parseArray(s,String.class);

                if (null != ss)
                {
                    mF.clear();
                    mF.addAll(ss);
                }
                F.e(ss.toString());
            }

        }catch (Exception e)
        {
            F.e(e.toString());
        }


    }

    public ArrayList<String> getmFList()
    {
        getMF();
        return mF;
    }
}
