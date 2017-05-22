package cn.gtgs.base.playpro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gt.okgo.OkGo;
import com.gt.okgo.cache.CacheEntity;
import com.gt.okgo.cache.CacheMode;
import com.gt.okgo.cookie.store.PersistentCookieStore;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.qiniu.pili.droid.streaming.StreamingEnv;

import java.io.File;
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
 * Created by  on 2017/4/26.
 */

public class PApplication extends Application {
    private static PApplication application;
    public static List<String> emoticonList = new ArrayList<String>();
    public static Map<String, Integer> emoticonsIdMap = new HashMap<String, Integer>();
    public static List<String> emoticonKeyList = new ArrayList<String>();
    public static Context applicationContext;
    public ArrayList<Activity> mActiviyts = new ArrayList<>();
    public ArrayList<String> mF = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
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
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效
                    .setCertificates();                               //方法一：信任所有证书,不安全有风险

        } catch (Exception e) {
            e.printStackTrace();
        }

        EMClient.getInstance().init(applicationContext, initChatOptions());
        EMClient.getInstance().setDebugMode(true);
        initEmotions();
        String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/jujing/";
        File dir = new File(ROOT_DIR);
        dir.mkdirs();
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
        for (int i = 0; i <= 9; i++) {
            Gift g = new Gift();
            g.setId(i + "");
            int b = i + 1;
            switch (i) {
                case 0:
                    g.setCredits("5");
                    g.setName("黄瓜");
                    break;
                case 1:
                    g.setName("抱抱");
                    g.setCredits("20");
                    break;
                case 2:
                    g.setName("玫瑰");
                    g.setCredits("40");
                    break;
                case 3:
                    g.setName("飞吻");
                    g.setCredits("88");
                    break;
                case 4:
                    g.setName("钻戒");
                    g.setCredits("188");
                    break;
                case 5:
                    g.setName("干杯");
                    g.setCredits("558");
                    break;
                case 6:
                    g.setName("跑车");
                    g.setCredits("666");
                    break;
                case 7:
                    g.setName("丘比特");
                    g.setCredits("888");
                    break;
                case 8:
                    g.setName("飞机");
                    g.setCredits("1600");
                    break;
                case 9:
                    g.setName("游艇");
                    g.setCredits("8888");
                    break;
            }

            int emoticonsId = getResources().getIdentifier("icon_" + b, "mipmap", getPackageName());
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

    public void getMF() {

        try {
            String s = ACache.get(this).getAsString(ACacheKey.CURRENT_FOLLOW);
            if (null != s) {
                ArrayList<String> ss = (ArrayList<String>) JSON.parseArray(s, String.class);

                if (null != ss) {
                    mF.clear();
                    mF.addAll(ss);
                }
                F.e(ss.toString());
            }

        } catch (Exception e) {
            F.e(e.toString());
        }


    }

    public ArrayList<String> getmFList() {
        getMF();
        return mF;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO" ,
            "android.permission.CAMERA"};
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
