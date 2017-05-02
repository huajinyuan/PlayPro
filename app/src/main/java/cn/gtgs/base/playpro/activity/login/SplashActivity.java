package cn.gtgs.base.playpro.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.HomeActivity;
import cn.gtgs.base.playpro.utils.F;


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

//        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
//        animation.setDuration(1500);
//        layout.startAnimation(animation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        layout.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                F.e("启动页面动画执行完毕...");
//                ACache aCache = ACache.get(SplashActivity.this);
//                Account account = (Account) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
                Intent intent;
//                if (null != account) {
//                    SplashActivity.this.finish();
//                    overridePendingTransition(0, 0);
//                    intent = new Intent(SplashActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                } else {
                    overridePendingTransition(0, 0);
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
//                }
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

//        layout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ACache aCache = ACache.get(SplashActivity.this);
//                Account account = (Account) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
//                Intent intent;
//                if (null != account) {
//                    SplashActivity.this.finish();
//                    overridePendingTransition(0, 0);
//                    intent = new Intent(SplashActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                } else {
//                    overridePendingTransition(0, 0);
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }, 2000);


    }

}
