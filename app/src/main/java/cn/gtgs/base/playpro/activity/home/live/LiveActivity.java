package cn.gtgs.base.playpro.activity.home.live;//package cn.gtgs.base.playpro.activity.home.live;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.gt.okgo.model.Response;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.URL;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.gtgs.base.playpro.R;
//import cn.gtgs.base.playpro.activity.home.live.model.LoginInfo;
//import cn.gtgs.base.playpro.http.Config;
//import cn.gtgs.base.playpro.utils.ACache;
//
//public class LiveActivity extends Activity {
//    @BindView(R.id.bt_startLive)
//    Button bt_startLive;
//
//    LoginInfo loginInfo;
//    RequestQueue requestQueue = NoHttp.newRequestQueue();
//    private static String TAG = "LiveActivity";
//    private static final String url = "http://42.159.246.0/zhengyuapi/pili/test.php";
//    private static String url2 = "rtmp://pili-publish.yequtv.cn/yequtv/57c42f241013859e15030ed1?key=new_secret_words";
//    private static final String INPUT_TYPE_STREAM_JSON = "StreamJson";
//    private static final String INPUT_TYPE_AUTHORIZED_URL = "AuthorizedUrl";
//    private static final String INPUT_TYPE_UNAUTHORIZED_URL = "UnauthorizedUrl";
//
//    private static final String[] mInputTypeList = {
//            "Please select input type of publish url:",
//            INPUT_TYPE_STREAM_JSON,
//            INPUT_TYPE_AUTHORIZED_URL,
//            INPUT_TYPE_UNAUTHORIZED_URL
//    };
//
//    private Button mHwCodecCameraStreamingBtn;
//    private Button mSwCodecCameraStreamingBtn;
//    private Button mAudioStreamingBtn;
//
//    private EditText mInputUrlEditText;
//
//    private String mSelectedInputType = null;
//
//    private static boolean isSupportHWEncode() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
//    }
//
//    private String requestStream(String appServerUrl) {
//        try {
//            HttpURLConnection httpConn = (HttpURLConnection) new URL(appServerUrl).openConnection();
//            httpConn.setRequestMethod("POST");
//            httpConn.setConnectTimeout(5000);
//            httpConn.setReadTimeout(10000);
//            int responseCode = httpConn.getResponseCode();
//            if (responseCode != HttpURLConnection.HTTP_OK) {
//                return null;
//            }
//
//            int length = httpConn.getContentLength();
//            if (length <= 0) {
//                length = 16 * 1024;
//            }
//            InputStream is = httpConn.getInputStream();
//            byte[] data = new byte[length];
//            int read = is.read(data);
//            is.close();
//            if (read <= 0) {
//                return null;
//            }
//            return new String(data, 0, read);
//        } catch (Exception e) {
//            showToast("Network error!");
//        }
//        return null;
//    }
//
//    void showToast(final String msg) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(LiveActivity.this, msg, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void startStreamingActivity(final Intent intent) {
//        final String inputUrl = mInputUrlEditText.getText().toString().trim();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String publishUrl = null;
//                Log.i(TAG, "mSelectedInputType:" + mSelectedInputType + ",inputUrl:" + inputUrl);
//                if (!"".equalsIgnoreCase(inputUrl)) {
//                    publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + inputUrl;
//                } else {
//                    if (mSelectedInputType != null) {
//                        if (INPUT_TYPE_STREAM_JSON.equalsIgnoreCase(mSelectedInputType)) {
//                            publishUrl = requestStream(url);
//                            if (publishUrl != null) {
//                                publishUrl = Config.EXTRA_PUBLISH_JSON_PREFIX + publishUrl;
//                            }
//                        } else if (INPUT_TYPE_AUTHORIZED_URL.equalsIgnoreCase(mSelectedInputType)) {
////                            publishUrl = requestStream(url2);
//                            publishUrl = url2;
//                            if (publishUrl != null) {
//                                publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + publishUrl;
//                            }
//                        } else if (INPUT_TYPE_UNAUTHORIZED_URL.equalsIgnoreCase(mSelectedInputType)) {
//                            // just for test
////                            publishUrl = requestStream(url2);
//                            publishUrl = url2;
//                            try {
//                                URI u = new URI(publishUrl);
//                                publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + String.format("rtmp://401.qbox.net%s?%s", u.getPath(), u.getRawQuery());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                publishUrl = null;
//                            }
//                        } else {
//                            throw new IllegalArgumentException("Illegal input type");
//                        }
//                    }
//                }
//
//                if (publishUrl == null) {
//                    showToast("Publish Url Got Fail!");
//                    return;
//                }
//                intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
//                startActivity(intent);
//            }
//        }).start();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_live);
//        ButterKnife.bind(this);
//        ACache aCache = ACache.get(getApplicationContext());
//        loginInfo = (LoginInfo) aCache.getAsObject("logininfo");
//        Request<String> request = NoHttp.createStringRequest(Config.URL_StartStreaming, RequestMethod.POST);
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        requestQueue.add(0, request, responseListener);
//        bt_startLive.setVisibility(View.INVISIBLE);
//
//        TextView mVersionInfoTextView = (TextView) findViewById(R.id.version_info);
//        mVersionInfoTextView.setText(Config.VERSION_HINT);
//
//        Spinner inputTypeSpinner = (Spinner) findViewById(R.id.spinner_input_type);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mInputTypeList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        inputTypeSpinner.setAdapter(adapter);
//        inputTypeSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
//        inputTypeSpinner.setVisibility(View.INVISIBLE);
//
//        mInputUrlEditText = (EditText) findViewById(R.id.input_url);
//
//        mHwCodecCameraStreamingBtn = (Button) findViewById(R.id.hw_codec_camera_streaming_btn);
//        mHwCodecCameraStreamingBtn.setVisibility(View.GONE);
//        mHwCodecCameraStreamingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LiveActivity.this, HWCodecCameraStreamingActivity.class);
//                startStreamingActivity(intent);
//            }
//        });
//
//        mSwCodecCameraStreamingBtn = (Button) findViewById(R.id.sw_codec_camera_streaming_btn);
//        mSwCodecCameraStreamingBtn.setVisibility(View.GONE);
//        mSwCodecCameraStreamingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LiveActivity.this, SWCodecCameraStreamingActivity.class);
//                startStreamingActivity(intent);
//            }
//        });
//
//        mAudioStreamingBtn = (Button) findViewById(R.id.start_pure_audio_streaming_btn);
//        mAudioStreamingBtn.setVisibility(View.GONE);
//        mAudioStreamingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LiveActivity.this, AudioStreamingActivity.class);
//                startStreamingActivity(intent);
//            }
//        });
//    }
//
//    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
//
//        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            if (arg2 != 0) {
//                mSelectedInputType = mInputTypeList[arg2];
//                if (isSupportHWEncode()) {
//                    mHwCodecCameraStreamingBtn.setVisibility(View.VISIBLE);
//                }
//                mSwCodecCameraStreamingBtn.setVisibility(View.VISIBLE);
//                mAudioStreamingBtn.setVisibility(View.VISIBLE);
//            }
//        }
//
//        public void onNothingSelected(AdapterView<?> arg0) {
//        }
//    }
//
//    @OnClick(R.id.bt_startLive)
//    void setstartlive() {
//        Intent intent = new Intent(LiveActivity.this, HWCodecCameraStreamingActivity.class);
//        String publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + url2;
//        intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
//        startActivity(intent);
//        Log.e("asda", "StartStreaming");
//    }
//
//    @OnClick(R.id.bt_share2wx)
//    void share2wx() {
//        doShare();
//    }
//
//    public void doShare() {
//        WXWebpageObject webpageObject = new WXWebpageObject();
//        webpageObject.webpageUrl = "https://www.yequtv.cn/anchors?anchor_id=" + loginInfo.id + "&from=wechat";
//        WXMediaMessage msg = new WXMediaMessage(webpageObject);
//        msg.title = "也趣";
//        msg.description = "也趣分享内容";
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
////        msg.thumbData=Ui
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
////        msg.setThumbImage();
//        req.message = msg;
//        IWXAPI mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, true);
//        mApi.sendReq(req);
//    }
//    OnResponseListener<String> responseListener = new OnResponseListener<String>() {
//        @Override
//        public void onStart(int what) {
//
//        }
//
//        @Override
//        public void onSucceed(int what, Response<String> response) {
//            if (response.responseCode() == 200) {
//                Streaming streaming = JSON.parseObject(response.get(), Streaming.class);
//                url2 = streaming.publish;
////                url2 = "http://playback.yequtv.cn/appleplayback.m3u8";
//                bt_startLive.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onFailed(int what, Response<String> response) {
//
//        }
//
//        @Override
//        public void onFinish(int what) {
//
//        }
//    };
//}
