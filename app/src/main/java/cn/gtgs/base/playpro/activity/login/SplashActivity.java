package cn.gtgs.base.playpro.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.okgo.OkGo;
import com.gt.okgo.download.DownloadInfo;
import com.gt.okgo.download.DownloadManager;
import com.gt.okgo.download.DownloadService;
import com.gt.okgo.listener.DownloadListener;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.Appinfo;
import cn.gtgs.base.playpro.http.BaseList;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.AppUtil;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;


public class SplashActivity extends AppCompatActivity {
    Context context;
    RelativeLayout layout;
    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        layout = (RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        layout.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                F.e("启动页面动画执行完毕...");
//                load();
                ACache aCache = ACache.get(SplashActivity.this);
                Follow info = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
                Intent intent;
                if (null != info) {
                    SplashActivity.this.finish();
                    overridePendingTransition(0, 0);
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    overridePendingTransition(0, 0);
//                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                F.e("启动页面动画重复执行...");
            }

            @Override
            public void onAnimationStart(Animation animation) {
                F.e("启动页面动画开始执行...");
            }

        });

    }

    public void chackVersion() {
        HttpParams parmas = new HttpParams();
        parmas.put("appType", "1");
        PostRequest request = OkGo.post(Config.CHECK_VERSION).params(parmas);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("网络连接失败，请检查网络后重新进入", SplashActivity.this);
            }

            @Override
            public void onNext(Response response) {
                BaseList<Appinfo> bs = Parsing.getInstance().ResponseToList3(response, Appinfo.class);
                ArrayList<Appinfo> apps = (ArrayList<Appinfo>) bs.getDataList();
                Appinfo appinfo = apps.get(0);
                int version = 1;
                if (null != appinfo) {
                    version = Integer.valueOf(appinfo.getAppVersion());
                }
                int i = AppUtil.getVersionCode(SplashActivity.this);
                if (version > i) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog mydialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.DialogTransBackGround);
                            mydialog = builder.create();
                            View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.item_dialog_releaseagent2, null);
                            TextView tv_content = (TextView) view.findViewById(R.id.tv_dialog_content);
                            Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
                            Button bt_yes = (Button) view.findViewById(R.id.bt_dialog_yes);
                            tv_content.setText("有新版本更新，是否更新？");
                            bt_yes.setText("更新");
                            mydialog.setCancelable(true);
                            mydialog.show();
                            mydialog.setContentView(view);

                            // dialog内部的点击事件
                            bt_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO 开始下载


                                }
                            });
                            bt_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mydialog.dismiss();
                                    finish();
                                }
                            });


                        }
                    });


                }


            }
        });
    }

    private DownloadManager downloadManager;

    public void load() {

//    public void load(Appinfo appinfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/jujing/");
        GetRequest request = OkGo.get("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");
//        GetRequest request = OkGo.get(Config.BASE + appinfo.getAppFile());
        DownloadListener downloadListener = new MyDownloadListener();
        downloadManager.addTask("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk", request, downloadListener);
//        downloadManager.addTask(Config.BASE + appinfo.getAppFile(), request, downloadListener);
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            if (getUserTag() == null) return;
//            ViewHolder holder = (ViewHolder) getUserTag();
//            holder.refresh();  //这里不能使用传递进来的 DownloadInfo，否者会出现条目错乱的问题
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(SplashActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null)
                Toast.makeText(SplashActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
