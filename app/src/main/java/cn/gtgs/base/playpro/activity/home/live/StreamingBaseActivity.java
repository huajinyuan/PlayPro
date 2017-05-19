package cn.gtgs.base.playpro.activity.home.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting.CAMERA_FACING_ID;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.model.Gift;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.widget.PeriscopeLayout;
import cn.gtgs.base.playpro.widget.RotateLayout;
import cn.gtgs.base.playpro.widget.WheelView;
import cn.gtgs.base.playpro.widget.gles.FBO;
import okhttp3.Response;
import rx.Subscriber;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by  on 15/7/6.
 */
public class StreamingBaseActivity extends Activity implements
        View.OnLayoutChangeListener,
        StreamStatusCallback,
        StreamingPreviewCallback,
        SurfaceTextureCallback,
        AudioSourceCallback,
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener {

    //-----------以下为环信

    ListView listView;
    TextView tv_likes;
    String chatroomid = "261649293176209844";
    private List<EMMessage> msgList = new ArrayList<>();
    MessageChatroomAdapter adapter;
    private EMConversation conversation;
    private boolean isJoined = false;
    private int FACE_SIZE;// 表情大小
    String message_from, message_content;
    Timer timer_hide = new Timer();
    ImageView iv_gift;
    ACache aCache;
    UserInfo userInfo;
    //--------------------

    private static final String TAG = "StreamingBaseActivity";
    public LinearLayout ll_option;
    public boolean option_invisiable = true;

    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms

    private Context mContext;

    //    RequestQueue requestQueue = NoHttp.newRequestQueue();
    ImageView iv_live_option;
    AlertDialog dialogsettings;
    SeekBar seekBarBeauty;
    ImageView iv_live_changecamera, iv_live_booking;
    //    LinearLayout ll_live_booking;
    RelativeLayout rl_live_bootombar;
    //    ListView lv_live_booking;
//    View view_booking_click;
    ImageView iv_live_close;
    TextView tv_live_id, tv_live_onlinenum;
    LinearLayout ll_live_onlinenum;
    final int Int_StopStreaming = 0, Int_BookingValid = 1, Int_OnlineNum = 2;

    private Button mMuteButton;
    private Button mCameraSwitchBtn;
    private Button mCaptureFrameBtn;
    private Button mEncodingOrientationSwitcherBtn;
    private Button mFaceBeautyBtn;
    private RotateLayout mRotateLayout;

    protected TextView mSatusTextView;
    private TextView mLogTextView;
    private TextView mStreamStatus;

    private boolean mIsNeedMute = false;
    private boolean mIsNeedFB = false;
    private boolean isEncOrientationPort = true;

    protected static final int MSG_START_STREAMING = 0;
    protected static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    private static final int MSG_MUTE = 3;
    private static final int MSG_FB = 4;

    protected String mStatusMsgContent;

    protected String mLogContent = "\n";

    private View mRootView;

    protected MediaStreamingManager mMediaStreamingManager;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected StreamingProfile mProfile;
    protected JSONObject mJSONObject;
    private boolean mOrientationChanged = false;

    protected boolean mIsReady = false;

    private int mCurrentZoom = 0;
    private int mMaxZoom = 0;

    private FBO mFBO = new FBO();

    private Screenshooter mScreenshooter = new Screenshooter();
    private Switcher mSwitcher = new Switcher();
    private EncodingOrientationSwitcher mEncodingOrientationSwitcher = new EncodingOrientationSwitcher();

    private int mCurrentCamFacingIndex;

    public Follow mF;

    @BindView(R.id.danmakuView)
    DanmakuView mDanmakuView;
    @BindView(R.id.tv_play_toast_sys)
    TextView mTvSysToast;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_STREAMING:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // disable the shutter button before startStreaming
                            boolean res = mMediaStreamingManager.startStreaming();
                            Log.i(TAG, "res:" + res);
                            if (!res) {
                            }

//                            setShutterButtonEnabled(false);
//                            boolean res = mMediaStreamingManager.startStreaming();
//                            mShutterButtonPressed = true;
//                            Log.i(TAG, "res:" + res);
//                            if (!res) {
//                                mShutterButtonPressed = false;
//                                setShutterButtonEnabled(true);
//                            }
//                            setShutterButtonPressed(mShutterButtonPressed);
                        }
                    }).start();
                    break;
                case MSG_SET_ZOOM:
                    mMediaStreamingManager.setZoomValue(mCurrentZoom);
                    break;
                case MSG_MUTE:
                    mIsNeedMute = !mIsNeedMute;
                    mMediaStreamingManager.mute(mIsNeedMute);
                    updateMuteButtonText();
                    break;
                case MSG_FB:
                    mIsNeedFB = !mIsNeedFB;
                    mMediaStreamingManager.setVideoFilterType(mIsNeedFB ?
                            CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                            : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
                    updateFBButtonText();
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };

    Follow mAnchor;
    @BindView(R.id.periscope)
    public PeriscopeLayout periscopeLayout;
    @BindView(R.id.tv_camera_level)
    public TextView mTvLevel;
    @BindView(R.id.tv_camera_level_toast)
    public TextView mTvLevelToast;
    @BindView(R.id.tv_gold_count)
    public TextView mTvGoldCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        PApplication.getInstance().mActiviyts.add(this);
        FACE_SIZE = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
        aCache = ACache.get(this);
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        userInfo = mF.getMember();
        mAnchor = (Follow) getIntent().getSerializableExtra(Config.EXTRA_KEY_PUB_FOLLOW);
        chatroomid = mAnchor.getChatRoomId();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            isEncOrientationPort = true;
        } else if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            isEncOrientationPort = false;
        }
        setRequestedOrientation(Config.SCREEN_ORIENTATION);
        setContentView(R.layout.activity_camera_streaming);

        ButterKnife.bind(this);
        //----------------------------------------------------------以下为环信
        //-------------------------------------------------------------------
//        aCache = ACache.get(this);
//        chatroomid = ((LoginInfo) aCache.getAsObject("logininfo")).huanxin_chatroom_id;
        listView = (ListView) findViewById(R.id.listView);
        tv_likes = (TextView) findViewById(R.id.tv_likes);
        iv_gift = (ImageView) findViewById(R.id.iv_gift);
        if (EMClient.getInstance().isLoggedInBefore()) {
            login();
            Log.e("main", "islogged");
        } else {
            login();
            Log.e("main", "logging");
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isJoined)
                    joinchatroom();
            }
        }, 5000);
        //-------------------------------------------------------------------
        //-------------------------------------------------------------------

        mContext = this;
        doPlay();
        initUIs();
    }


    public void doPlay() {
        String publishUrlFromServer = getIntent().getStringExtra(Config.EXTRA_KEY_PUB_URL);


        Log.i(TAG, "publishUrlFromServer:" + publishUrlFromServer);


        StreamingProfile.AudioProfile aProfile = new StreamingProfile.AudioProfile(44100, 96 * 1024);
        StreamingProfile.VideoProfile vProfile = new StreamingProfile.VideoProfile(30, 1000 * 1024, 48);
        StreamingProfile.AVProfile avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);

        mProfile = new StreamingProfile();
        if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_URL_PREFIX)) {
            // publish url
            try {
                mProfile.setPublishUrl(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_URL_PREFIX.length()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_JSON_PREFIX)) {
            try {
                mJSONObject = new JSONObject(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_JSON_PREFIX.length()));
                StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
                mProfile.setStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Invalid Publish Url", Toast.LENGTH_LONG).show();
        }

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH3)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
//                .setPreferredVideoEncodingSize(960, 544)
                .setEncodingSizeLevel(Config.ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
//                .setAVProfile(avProfile)
                .setDnsManager(getMyDnsManager())
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
//                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = cameraFacingId.ordinal();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setCameraFacingId(cameraFacingId)
                .setBuiltInFaceBeautyEnabled(true)
                .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.SMALL)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);
        mIsNeedFB = true;
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsReady = false;
        mHandler.removeCallbacksAndMessages(null);
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
        Updatestatus("1", 0);
        //-----------------------------------以下为环信
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);

//        Request<String> request = NoHttp.createStringRequest(Config.URL_StopStreaming, RequestMethod.DELETE);
//        request.addHeader("Authorization","Bearer "+loginInfo.token);
//        requestQueue.add(Int_StopStreaming, request, responseListener);
    }


    public void Updatestatus(String status, int price) {

        HttpParams params = new HttpParams();
        params.put("anId", mAnchor.getAnId());
        params.put("status", status);
        params.put("anPrice", price);
        PostRequest request = OkGo.post(Config.MEMBER_LIVESTATUS).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {

            }
        });
    }

    protected void startStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_START_STREAMING), 50);
    }

    protected void stopStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_STOP_STREAMING), 50);
    }

    @Override
    public boolean onRecordAudioFailedHandled(int err) {
        F.e("==========================onRecordAudioFailedHandled" + err);
        mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
        mMediaStreamingManager.startStreaming();
        return true;
    }

    @Override
    public boolean onRestartStreamingHandled(int err) {
        F.e("==========================onRestartStreamingHandled" + err);
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        Camera.Size size = null;
        if (list != null) {
            for (Camera.Size s : list) {
                if (s.height >= 480) {
                    size = s;
                    break;
                }
            }
        }
//        Log.e(TAG, "selected size :" + size.width + "x" + size.height);
        return size;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());

        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);

            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:" + mMaxZoom);
            if (!mHandler.hasMessages(MSG_SET_ZOOM)) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ZOOM), ZOOM_MINIMUM_WAIT_MILLIS);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.i(TAG, "view!!!!:" + v);
    }

    @Override
    public void onSurfaceCreated() {
        Log.i(TAG, "onSurfaceCreated");
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        // newTexId should not equal with texId. texId is from the SurfaceTexture.
        // Otherwise, there is no filter effect.
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
//        Log.i(TAG, "onDrawFrame texId:" + texId + ",newTexId:" + newTexId + ",texWidth:" + texWidth + ",texHeight:" + texHeight);
        return newTexId;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int size, long tsInNanoTime, boolean eof) {
//        for (int i = 0; i < size; i++) {
//            byteBuffer.put(i, (byte) 0x00);
//        }
    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mStreamStatus.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
//                        + "\naudio:" + streamStatus.audioFps + " fps"
//                        + "\nvideo:" + streamStatus.videoFps + " fps");
                mStreamStatus.setText("fps:" + streamStatus.audioFps + "帧/秒");
            }
        });
    }

    long errorTime = 0;

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        switch (streamingState) {
            case PREPARING:
//                mStatusMsgContent = getString(R.string.string_state_preparing);
                mStatusMsgContent = "准备中";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_live_onlinenum.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case READY:
                mIsReady = true;
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
//                mStatusMsgContent = getString(R.string.string_state_ready);
                mStatusMsgContent = "就绪";
                // start streaming when READY
                startStreaming();
                break;
            case CONNECTING:
//                mStatusMsgContent = getString(R.string.string_state_connecting);
                F.e("===================SENDING_BUFFER_EMPTY");

                mStatusMsgContent = "连接中";
                break;
            case STREAMING:
//                mStatusMsgContent = getString(R.string.string_state_streaming);
                mStatusMsgContent = "正在直播";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_live_onlinenum.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case SHUTDOWN:
//                mStatusMsgContent = getString(R.string.string_state_ready);
                mStatusMsgContent = "就绪";
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
                break;
            case IOERROR:
//                mLogContent += "IOERROR\n";
//                mStatusMsgContent = getString(R.string.string_state_ready);

                if (System.currentTimeMillis() - errorTime < 50000) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mMediaStreamingManager.startStreaming();
                            Log.e("aaa", "trying reStart");
                        }
                    }, 1000);
                } else {
                    mStatusMsgContent = "无法直播！";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll_live_onlinenum.setVisibility(View.INVISIBLE);
                            //TODO 直播超时
                            final AlertDialog mydialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(StreamingBaseActivity.this, R.style.DialogTransBackGround);
                            mydialog = builder.create();
                            mydialog.setCanceledOnTouchOutside(false);
                            View view = LayoutInflater.from(StreamingBaseActivity.this).inflate(R.layout.item_dialog_releaseagent, null);
                            TextView tv_content = (TextView) view.findViewById(R.id.tv_dialog_content);
                            Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
                            Button bt_yes = (Button) view.findViewById(R.id.bt_dialog_yes);
                            tv_content.setText("网络连接超时，请退出检查网络后再直播");
                            bt_yes.setText("退出直播");
                            mydialog.setCancelable(true);
                            mydialog.show();
                            mydialog.setContentView(view);
                            // dialog内部的点击事件
                            bt_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    StreamingBaseActivity.this.finish();
                                    mydialog.dismiss();
                                }
                            });
                            bt_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    StreamingBaseActivity.this.finish();
                                    mydialog.dismiss();
                                }
                            });




                        }
                    });




                }


                break;
            case UNKNOWN:
                mStatusMsgContent = "READY";
                break;
            case SENDING_BUFFER_EMPTY:
                F.e("===================SENDING_BUFFER_EMPTY");
                break;
            case SENDING_BUFFER_FULL:
                F.e("===================SENDING_BUFFER_FULL");
                break;
            case AUDIO_RECORDING_FAIL:
                F.e("===================AUDIO_RECORDING_FAIL");
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case DISCONNECTED://TODO 网络重连或失败
                F.e("===================DISCONNECTED");
//                mLogContent += "DISCONNECTED\n";
                errorTime = System.currentTimeMillis();
//                stopStreaming();
//                startStreaming();
//                mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
//                mMediaStreamingManager.startStreaming();
//                mMediaStreamingManager.pause();
//                mMediaStreamingManager.resume();
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
            case CAMERA_SWITCHED:
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogTextView != null) {
                    mLogTextView.setText(mLogContent);
                }
                mSatusTextView.setText(mStatusMsgContent);
            }
        });
    }

    private ArrayList<String> PLANETS = new ArrayList<>();

    private void initUIs() {
        mRootView = findViewById(R.id.content);
        mRootView.addOnLayoutChangeListener(this);
        for (int i = 5; i <= 25; i++) {
            PLANETS.add(i + "");
        }

        initDialogSettings();
        iv_live_option = (ImageView) findViewById(R.id.iv_live_option);
        mSatusTextView = (TextView) findViewById(R.id.streamingStatus);

        mLogTextView = (TextView) findViewById(R.id.log_info);
        mStreamStatus = (TextView) findViewById(R.id.stream_status);
        iv_live_changecamera = (ImageView) findViewById(R.id.iv_live_changecamera);
        iv_live_booking = (ImageView) findViewById(R.id.iv_live_booking);
//        ll_live_booking = (LinearLayout) findViewById(ll_live_booking);
        rl_live_bootombar = (RelativeLayout) findViewById(R.id.rl_live_bootombar);
//        lv_live_booking = (ListView) findViewById(lv_live_booking);
//        view_booking_click = findViewById(view_booking_click);
        iv_live_close = (ImageView) findViewById(R.id.iv_live_close);
        tv_live_id = (TextView) findViewById(R.id.tv_live_id);
        tv_live_onlinenum = (TextView) findViewById(R.id.tv_live_onlinenum);
        ll_live_onlinenum = (LinearLayout) findViewById(R.id.ll_live_onlinenum);
        if (StringUtils.isNotEmpty(mF.getFaCount())) {
            tv_live_onlinenum.setText(mF.getFaCount());
        }

//        tv_live_id.setText("ID:" + loginInfo.id);
        iv_live_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogsettings.show();
            }
        });
        iv_live_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogClose();
            }
        });

        iv_live_changecamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
            }
        });
        iv_live_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                // 构建弹出框View
                View outerView = LayoutInflater.from(mContext)
                        .inflate(R.layout.wheel_view, null);

                final WheelView wv = (WheelView) outerView
                        .findViewById(R.id.wheel_view_wv);
                // wv.setOffset(0);// 偏移量
                wv.setOffset(2);
                wv.setItems(PLANETS);// 实际内容
                wv.setSeletion(0);// 设置默认被选中的项目
                // wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        // 选中后的处理事件
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex
                                + ", item: " + item);
                    }
                });

                // 展示弹出框
                new AlertDialog.Builder(mContext)
                        .setTitle("设置收费单价(?钻石/分钟)").setView(outerView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = wv.getSeletedItem();
                                Shoufei(s);
                                Usercount = getChatRoomInfoCount();
                                Updatestatus("3", Integer.valueOf(s));
                                if (temp2) {
                                    timer2 = new MyTimer2(999999999, 60000);
                                    timer2.start();
                                    temp2 = false;
                                } else {
                                    timer2.cancel();
                                    timer2 = null;
                                    timer2 = new MyTimer2(999999999, 60000);
                                    timer2.start();
                                    temp2 = false;
                                }

                            }
                        })
                        .show();

            }


        });

        if (StringUtils.isNotEmpty(mF.getSysMsg())) {
            mTvSysToast.setVisibility(View.VISIBLE);
            mTvSysToast.setText(mF.getSysMsg());
//            Animation a = AnimationUtils.loadAnimation(this, R.anim.scalebig2);
//            mTvSysToast.startAnimation(a);
//            a.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mTvSysToast.getVisibility() == View.VISIBLE) {
//                                F.e("---------------------------------mTvGiftCount GONE");
//                                mTvSysToast.clearAnimation();
//                                mTvSysToast.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
        }
        mTvGoldCount.setText(mF.getAnGold() + "");

    }

    private int mPrice = 0;
    private int Usercount = 0;
    private boolean IsShouFei = false;

    public void Shoufei(String price) {
        mPrice = Integer.valueOf(price);
        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "Recharge", chatroomid);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setFrom(userInfo.getMbNickname());
        message.setAttribute("user_name", userInfo.getMbNickname());
        message.setAttribute("Recharge", price);
        message.setAttribute("level", userInfo.getMbLevel());
        EMClient.getInstance().chatManager().sendMessage(message);
//        showLikes(message.getFrom());


    }

    private void initButtonText() {
        updateFBButtonText();
        updateCameraSwitcherButtonText(mCameraStreamingSetting.getReqCameraId());
        mCaptureFrameBtn.setText("截图");
        updateFBButtonText();
        updateMuteButtonText();
        updateOrientationBtnText();
    }

    private void updateOrientationBtnText() {
        if (isEncOrientationPort) {
            mEncodingOrientationSwitcherBtn.setText("切换横屏");
        } else {
            mEncodingOrientationSwitcherBtn.setText("切换竖屏");
        }
    }

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }

    private void updateFBButtonText() {
        if (mFaceBeautyBtn != null) {
            mFaceBeautyBtn.setText(mIsNeedFB ? "已开启" : "已关闭");
        }
    }

    private void updateMuteButtonText() {
        if (mMuteButton != null) {
            mMuteButton.setText(mIsNeedMute ? "已静音" : "未开启");
        }
    }

    private void updateCameraSwitcherButtonText(int camId) {
        if (mCameraSwitchBtn == null) {
            return;
        }
        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraSwitchBtn.setText("切换后置");
        } else {
            mCameraSwitchBtn.setText("切换前置");
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    private CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int i, int i1, int i2, int i3, long l) {
        return true;
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();

            CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
    }

    private class EncodingOrientationSwitcher implements Runnable {

        @Override
        public void run() {
            Log.i(TAG, "isEncOrientationPort:" + isEncOrientationPort);
            stopStreaming();
            mOrientationChanged = !mOrientationChanged;
            isEncOrientationPort = !isEncOrientationPort;
            mProfile.setEncodingOrientation(isEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
            mMediaStreamingManager.setStreamingProfile(mProfile);
            setRequestedOrientation(isEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaStreamingManager.notifyActivityOrientationChanged();
            updateOrientationBtnText();
            Toast.makeText(StreamingBaseActivity.this, Config.HINT_ENCODING_ORIENTATION_CHANGED,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "EncodingOrientationSwitcher -");
        }
    }

    private class Screenshooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    //------------------------------------------------------------------------------以下为环信功能
    //------------------------------------------------------------------------------
    public void login() {
        Log.e("sdad", "start login...");
//        41240285948    xnPPr2xCgR
        EMClient.getInstance().login(userInfo.getMbPhone() + "", MD5Util.getMD5("webcast" + userInfo.getMbId()), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joinchatroom();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("main", "progress" + progress + "");
            }

            @Override
            public void onError(int code, String message) {
                Log.e("main", "登录聊天服务器失败！" + message);
            }
        });

    }

    public void joinchatroom() {
        Log.e("adad", "startJoinChatRoom..");
        EMClient.getInstance().chatroomManager().joinChatRoom(chatroomid, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(chatroomid);
                if (room != null) {
                    showChatroomToast("已加入聊天室");
                    isJoined = true;
                    Log.e("adad", "JoinChatRoom succeed");
                } else {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadsomes();
                        addChatRoomChangeListenr();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Log.e("sdad", "joinchatroom OnError");
            }
        });
    }


    public void loadsomes() {
//        EMClient.getInstance().groupManager().loadAllGroups();
//        EMClient.getInstance().chatManager().loadAllConversations();
//        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++获取单聊、群聊 聊天记录
//        conversation = EMClient.getInstance().chatManager().getConversation(chatroomid, EMConversation.EMConversationType.ChatRoom ,true);//chatroom:226122948862280132
//        msgList = conversation.getAllMessages();
        adapter = new MessageChatroomAdapter(msgList, StreamingBaseActivity.this);
        listView.setAdapter(adapter);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        if (msgList.size() > 0)
            listView.setSelection(listView.getCount() - 1);
    }

    public void addChatRoomChangeListenr() {
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }

    private Gift mGetGift;

    EMChatRoomChangeListener chatRoomChangeListener = new EMChatRoomChangeListener() {

        @Override
        public void onChatRoomDestroyed(String roomId, String roomName) {
//            if (roomId.equals(chatroomid)) {
//                showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
//            }
        }

        @Override
        public void onMemberJoined(String roomId, String participant) {
//                checkOnlineNum();

//            EMMessage message = EMMessage.createTxtSendMessage(participant + " 加入了聊天室", chatroomid);
//            message.setChatType(EMMessage.ChatType.ChatRoom);
//            message.setFrom("动态");
//            msgList.add(message);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.notifyDataSetChanged();
//                    if (msgList.size() > 0) {
//                        listView.setSelection(listView.getCount() - 1);
//                        Log.e("sad", "setselection");
//                    }
//                }
//            });
        }

        @Override
        public void onMemberExited(String roomId, String roomName, String participant) {
//                checkOnlineNum();

//            EMMessage message = EMMessage.createTxtSendMessage(participant + "离开了聊天室", chatroomid);
//            message.setChatType(EMMessage.ChatType.ChatRoom);
//            message.setFrom("动态");
//            msgList.add(message);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.notifyDataSetChanged();
//                    if (msgList.size() > 0) {
//                        listView.setSelection(listView.getCount() - 1);
//                        Log.e("sad", "setselection");
//                    }
//                }
//            });
        }

        @Override
        public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {
            if (roomId.equals(chatroomid)) {
                String curUser = EMClient.getInstance().getCurrentUser();
                if (curUser.equals(participant)) {
                    EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
                } else {
                    showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                }
            }
        }

        @Override
        public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {

        }

        @Override
        public void onMuteListRemoved(String chatRoomId, List<String> mutes) {

        }

        @Override
        public void onAdminAdded(String chatRoomId, String admin) {

        }

        @Override
        public void onAdminRemoved(String chatRoomId, String admin) {

        }

        @Override
        public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {

        }


    };

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {

            Log.e("main", "收到消息");

            for (EMMessage message : list) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getFrom();
                    message_from = username;
                }
                // 如果是当前会话的消息，刷新聊天页面
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                message_content = txtBody.getMessage();
                Map<String, Object> map = message.ext();
                if (map.containsKey("Gift")) {
                    mGetGift = new Gift();
                    mGetGift = PApplication.getInstance().getGiftObject((String) map.get("Gift"));
                    message_from = (String) map.get("user_name");
                    final String icon = (String) map.get("user_url");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showGifts(message_from, icon, mGetGift);
                        }
                    });
                    msgList.add(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (msgList.size() > 0) {
                                listView.setSelection(listView.getCount() - 1);
                                Log.e("sad", "setselection");
                            }
                        }
                    });


                } else if (map.containsKey("DianZan")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLikes(message_from);
                        }
                    });
                } else if (map.containsKey("DanMu")) {
                    //TODO 弹幕
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doDanmu(message_content);
                        }
                    });
                } else if (map.containsKey("JOIN_CHATROOM")) {
                    //TODO 新人加入聊天室消息
                    message_from = (String) map.get("user_name");
                    mF.setFaCount(getChatRoomInfoCount() + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_live_onlinenum.setText(mF.getFaCount() + "");
                        }
                    });
                    if (map.containsKey("level")) {
                        final int level = Integer.valueOf((String) map.get("level"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation a = AnimationUtils.loadAnimation(StreamingBaseActivity.this, R.anim.scalebig2);
                                a.setFillAfter(true);
                                mTvLevel.setVisibility(View.VISIBLE);
                                mTvLevel.setText(message_from + "加入聊天室");
                                mTvLevel.startAnimation(a);
                                if (level >= 15) {
                                    mTvLevelToast.setVisibility(View.VISIBLE);
                                    mTvLevelToast.setText(message_from + "加入聊天室");
                                    mTvLevelToast.startAnimation(a);
                                }
                                a.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mTvLevelToast.getVisibility() == View.VISIBLE) {
                                                    mTvLevelToast.clearAnimation();
                                                    mTvLevelToast.setVisibility(View.GONE);

                                                }
                                                if (mTvLevel.getVisibility() == View.VISIBLE) {
                                                    mTvLevel.clearAnimation();
                                                    mTvLevel.setVisibility(View.GONE);
                                                }

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        });
                    }

                } else {
                    msgList.add(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (msgList.size() > 0) {
                                listView.setSelection(listView.getCount() - 1);
                                Log.e("sad", "setselection");
                            }
                        }
                    });
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    public void showChatroomToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, str, LENGTH_SHORT).show();
            }
        });
    }

    public void showLikes(String from) {
        tv_likes.setText("用户 " + from + " 给您点了赞");
        tv_likes.setVisibility(View.VISIBLE);
        timer_hide.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_likes.setText("");
                        tv_likes.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 1500);
        if (temp) {
            new MyTimer(2000, 300).start();
            temp = false;
        }

    }


    @BindView(R.id.tv_play_gift_count)
    TextView mTvGiftCount;
    @BindView(R.id.lin_play_gift_send)
    View linGiftSend;
    @BindView(R.id.img_play_gift_send_icon)
    ImageView img_play_gift_send_icon;
    @BindView(R.id.tv_play_gift_name)
    TextView tv_play_gift_name;
    long GETGIFTTIME = 0;
    int GiftCount = 1;
    String giftId = "";

    public void showGifts(String from, String icon, Gift gift) {
        int gold = Integer.valueOf(mF.getAnGold());
        gold = gold + Integer.valueOf(gift.getCredits());
        mF.setAnGold(gold + "");
        mTvGoldCount.setText(gold + "");

        linGiftSend.setVisibility(View.VISIBLE);
        Glide.with(this).load(null != icon ? Config.BASE + icon : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(img_play_gift_send_icon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img_play_gift_send_icon.setImageDrawable(circularBitmapDrawable);
            }
        });
        tv_play_gift_name.setText(from);
        if (System.currentTimeMillis() - GETGIFTTIME < 1000) {
            if (giftId.equals(gift.getId())) {
                GiftCount++;
            } else {
                GiftCount = 1;
            }
        } else {
            GiftCount = 1;
        }
        iv_gift.setVisibility(View.VISIBLE);
        giftId = gift.getId();
        GETGIFTTIME = System.currentTimeMillis();
        Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scalebig);
        iv_gift.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(gift.picture).into(new GlideDrawableImageViewTarget(iv_gift) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                Animation b = AnimationUtils.loadAnimation(mContext, R.anim.scalebig);
                iv_gift.setAnimation(b);
                b.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv_gift.clearAnimation();
                        iv_gift.setVisibility(View.GONE);
                        linGiftSend.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
        mTvGiftCount.setVisibility(View.VISIBLE);
        mTvGiftCount.setText("X" + GiftCount);
        a.setFillAfter(true);
        mTvGiftCount.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvGiftCount.getVisibility() == View.VISIBLE) {
                            F.e("---------------------------------mTvGiftCount GONE");
                            mTvGiftCount.clearAnimation();
                            mTvGiftCount.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //TODO 收到礼物


    }

    public void initDialogSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTransBackGround);
        dialogsettings = builder.create();
        View view_dialog = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_livesettings, null);
        dialogsettings.setCancelable(true);
        dialogsettings.show();
        dialogsettings.setContentView(view_dialog);
        seekBarBeauty = (SeekBar) view_dialog.findViewById(R.id.beautyLevel_seekBar);
        mCameraSwitchBtn = (Button) view_dialog.findViewById(R.id.camera_switch_btn);
        mMuteButton = (Button) view_dialog.findViewById(R.id.mute_btn);
        mCaptureFrameBtn = (Button) view_dialog.findViewById(R.id.capture_btn);
        mFaceBeautyBtn = (Button) view_dialog.findViewById(R.id.fb_btn);
        mEncodingOrientationSwitcherBtn = (Button) view_dialog.findViewById(R.id.orientation_btn);

        mFaceBeautyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_FB)) {
                    mHandler.sendEmptyMessage(MSG_FB);
                }
            }
        });

        mMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHandler.hasMessages(MSG_MUTE)) {
                    mHandler.sendEmptyMessage(MSG_MUTE);
                }
            }
        });
        mCameraSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mSwitcher);
                mHandler.postDelayed(mSwitcher, 100);
            }
        });

        mCaptureFrameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(mScreenshooter);
                mHandler.postDelayed(mScreenshooter, 100);
            }
        });

        mEncodingOrientationSwitcherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(mEncodingOrientationSwitcher);
                mHandler.post(mEncodingOrientationSwitcher);
            }
        });

        seekBarBeauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                fbSetting.beautyLevel = progress / 100.0f;
                fbSetting.whiten = progress / 100.0f;
                fbSetting.redden = progress / 100.0f;
                mMediaStreamingManager.updateFaceBeautySetting(fbSetting);

//                aCache.put("beautyLevel", progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        initButtonText();
        dialogsettings.hide();
    }

    public void showDialogClose() {
        final AlertDialog mydialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTransBackGround);
        mydialog = builder.create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_releaseagent, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
        Button bt_yes = (Button) view.findViewById(R.id.bt_dialog_yes);
        tv_title.setText("退出直播");
        tv_content.setText("是否确定退出直播？");
        bt_yes.setText("确定");
        mydialog.setCancelable(true);
        mydialog.show();
        mydialog.setContentView(view);

        // dialog内部的点击事件
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
                finish();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
    }

    public void doDanmu(String message) {
//        DanmakuItem dm = new DanmakuItem(this, message, mDanmakuView.getWidth());
//        dm.setTextColor(Color.parseColor("#49C3B8"));
//        mDanmakuView.addItem(dm);
//        mDanmakuView.setVisibility(View.VISIBLE);
//        mDanmakuView.show();
        int gold = Integer.valueOf(mF.getAnGold());
        gold = gold + 2;
        mF.setAnGold(gold + "");
        mTvGoldCount.setText(gold + "");

        Pattern pattern = buildPattern();
        Matcher matcher = pattern.matcher(message);
        SpannableString spannableString = new SpannableString(message);
        while (matcher.find()) {
            if (PApplication.emoticonsIdMap.containsKey(matcher.group())) {
                int id = PApplication.emoticonsIdMap.get(matcher.group());

                Drawable drawable = this.getResources().getDrawable(id);
                if (drawable != null) {
                    drawable.setBounds(0, 0, FACE_SIZE, FACE_SIZE);
                    ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
                    spannableString.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        IDanmakuItem item = new DanmakuItem(this, spannableString, mDanmakuView.getWidth(), 0, R.color.colorBlue, 0, 1.5f);
        mDanmakuView.addItem(item);
        mDanmakuView.setVisibility(View.VISIBLE);
        mDanmakuView.show();

    }


    private boolean temp = true;

    private class MyTimer extends CountDownTimer {
        private static final String TAG = "MyTimer";

        //millisInFuture为你设置的此次倒计时的总时长，比如60秒就设置为60000
        //countDownInterval为你设置的时间间隔，比如一般为1秒,根据需要自定义。
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
            periscopeLayout.addHeart();
            Log.d(TAG, "111");
        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            temp = true;
        }
    }

    public int getChatRoomInfoCount() {
        try {
            EMChatRoom chatRoom = EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(chatroomid);
            return chatRoom.getMemberCount();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private MyTimer2 timer2;
    private boolean temp2 = true;

    private class MyTimer2 extends CountDownTimer {
        private static final String TAG = "MyTimer";

        //millisInFuture为你设置的此次倒计时的总时长，比如60秒就设置为60000
        //countDownInterval为你设置的时间间隔，比如一般为1秒,根据需要自定义。
        public MyTimer2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
            if (System.currentTimeMillis() - times > 5 * 60000) {
                times = System.currentTimeMillis();
                doRefresh();
            } else {
                int gold = Integer.valueOf(mF.getAnGold());
                int addGold = mPrice * Usercount;
                int g = gold + addGold;
                mF.setAnGold(g + "");
//                userInfo.setMbGold(gold + addGold);
                mTvGoldCount.setText(g + "");
            }
            Usercount = getChatRoomInfoCount();
        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            temp = true;
        }
    }

    long times = 0;

    public void doRefresh() {
        HttpParams params = new HttpParams();
        params.put("mbId", userInfo.getMbId());
        PostRequest request = OkGo.post(Config.POST_MEMBER_GET).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Response response) {
                HttpBase<Follow> bs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (null != bs.getData()) {
                    mF = bs.getData();
                    aCache.put(ACacheKey.CURRENT_ACCOUNT, mF);
                    userInfo = mF.getMember();
                    mTvGoldCount.setText(userInfo.getMbGold() + "");
                }
            }
        });

    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(
                PApplication.emoticonKeyList.size() * 3);
        patternString.append('(');
        for (int i = 0; i < PApplication.emoticonKeyList.size(); i++) {
            String s = PApplication.emoticonKeyList.get(i);
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

}