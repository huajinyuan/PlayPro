package cn.gtgs.base.playpro.service;//package cn.gtgs.base.playpro.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Handler;
//import android.os.IBinder;
//
//import java.util.Set;
//
//import cn.gtgs.base.playpro.PApplication;
//import cn.gtgs.base.playpro.utils.ExampleUtil;
//import cn.gtgs.base.playpro.utils.F;
//
//
//public class JPushService extends Service {
//
//    public JPushService() {
//
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
////        String imei = AppUtil.getIMEI(this);
////        if (StringUtils.isEmpty(imei)) {
////
////        } else {
////            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, imei));
////
////        }`
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        registerMessageReceiver();
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, PApplication.getPhone()));
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    //for receive customer msg from jpush server
//    private MyReceiver mMessageReceiver;
//    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_MESSAGE = "message";
//    public static final String KEY_EXTRAS = "extras";
//
//    public void registerMessageReceiver() {
//        mMessageReceiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        registerReceiver(mMessageReceiver, filter);
//    }
//
//
//    @Override
//    public void onRebind(Intent intent) {
////        isForeground = true;
//        super.onRebind(intent);
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        unregisterReceiver(mMessageReceiver);
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onDestroy() {
//        unregisterReceiver(mMessageReceiver);
//        super.onDestroy();
//    }
//
//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    F.e("极光id==================================" + alias+"        " + JPushInterface.getRegistrationID(getApplicationContext()));
//                    PApplication.setJPushID(JPushInterface.getRegistrationID(getApplicationContext()));
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    F.e(logs);
//                    if (ExampleUtil.isConnected(getApplicationContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    } else {
//                        F.e("No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    F.e(logs);
//            }
//
//            ExampleUtil.showToast(logs, getApplicationContext());
//        }
//
//    };
//
//    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    F.e(logs);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    F.e(logs);
//                    if (ExampleUtil.isConnected(getApplicationContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
//                    } else {
//                        F.e("No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    F.e(logs);
//            }
//
//            ExampleUtil.showToast(logs, getApplicationContext());
//        }
//
//    };
//
//    private static final int MSG_SET_ALIAS = 1001;
//    private static final int MSG_SET_TAGS = 1002;
//
//
//
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_SET_ALIAS:
//                    F.e("Set alias in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
//                    break;
//
//                case MSG_SET_TAGS:
//                    F.e("Set tags in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
//                    break;
//                default:
//                    F.e("Unhandled msg - " + msg.what);
//            }
//        }
//    };
//}
