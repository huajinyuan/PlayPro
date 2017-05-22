package cn.gtgs.base.playpro.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.gt.okgo.OkGo;
import com.gt.okgo.callback.FileCallback;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.BaseRequest;
import com.gt.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;

import cn.gtgs.base.playpro.PApplication;
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
import okhttp3.Call;
import okhttp3.Response;
import rx.Subscriber;


public class SplashActivity extends AppCompatActivity {
    Context context;
    RelativeLayout layout;
    private static final int sleepTime = 2000;
    View mDialogTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 23) {
            PApplication.getInstance().verifyStoragePermissions(this);
        }
        context = this;
        layout = (RelativeLayout) findViewById(R.id.activity_splash);
        mDialogTip = findViewById(R.id.rel_splash_dialog);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        layout.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                F.e("启动页面动画执行完毕...");
                chackVersion();
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
    AlertDialog mydialog;
    Button bt_yes ;
    int version = 1;
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
                final Appinfo appinfo = apps.get(0);

                if (null != appinfo) {
                    String v = appinfo.getAppVersion();
                    String[] s = v.split("\\.");
                    version = Integer.valueOf(s[s.length-1]);

                }
                else
                {
                    //TODO
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
                int i = AppUtil.getVersionCode(SplashActivity.this);
                if (version >i) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.DialogTransBackGround);
                            mydialog = builder.create();
                            View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.item_dialog_releaseagent2, null);
                            TextView tv_content = (TextView) view.findViewById(R.id.tv_dialog_content);
                            Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
                             bt_yes = (Button) view.findViewById(R.id.bt_dialog_yes);
                            tv_content.setText("有新版本更新（1.0."+version+"），是否更新？");
                            bt_yes.setText("更新");
                            mydialog.setCancelable(true);
                            mydialog.setCanceledOnTouchOutside(false);
                            mydialog.show();
                            mydialog.setContentView(view);

                            // dialog内部的点击事件
                            bt_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO 开始下载
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDialogTip.setVisibility(View.VISIBLE);
                                            bt_yes.setClickable(false);
                                            mydialog.dismiss();
                                        }
                                    });


                                    load(Config.BASE+appinfo.getAppFile());

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
                else{
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
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
                }


            }
        });
    }


    public void load(final String Path) {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                OkGo.get(Path)//
                        .tag(this)//
                        .execute(new FileCallback("living.apk") {
                            @Override
                            public void onBefore(BaseRequest request) {
                            }

                            @Override
                            public void onSuccess(final File file, Call call, Response response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_yes.setClickable(true);
                                        mDialogTip.setVisibility(View.GONE);
//
                                        AppUtil.installAPK(SplashActivity.this,file.getPath());
                                    }
                                });

                            }

                            @Override
                            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                System.out.println("downloadProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
                            }

                            @Override
                            public void onError(Call call, @Nullable Response response, @Nullable Exception e) {
                                super.onError(call, response, e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialogTip.setVisibility(View.GONE);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                            mydialog.show();
                                            bt_yes.setClickable(true);

                                            }
                                        });
                                        bt_yes.setClickable(true);
                                        ToastUtil.showToast("下载出错",SplashActivity.this);
                                    }
                                });

                            }
                        });
            }
        });
        thread.start();

    }

}
