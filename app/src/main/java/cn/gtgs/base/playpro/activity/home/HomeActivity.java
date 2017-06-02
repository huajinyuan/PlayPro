package cn.gtgs.base.playpro.activity.home;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.CenterActivity;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentFollow;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRanking;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRecommented;
import cn.gtgs.base.playpro.activity.home.live.HWCodecCameraStreamingActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.presenter.HomePresenter;
import cn.gtgs.base.playpro.activity.home.presenter.IHomeRefreshListener;
import cn.gtgs.base.playpro.activity.home.search.SearchActivity;
import cn.gtgs.base.playpro.activity.home.view.HomeDelegate;
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.DESUtil;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

public class HomeActivity extends ActivityPresenter<HomeDelegate> implements IHomeRefreshListener {
    private FragmentManager fragmentManager;
    FragmentRanking mRanking;
    FragmentFollow mFollow;
    private FragmentTransaction fragmentTransaction;
    FragmentRecommented mRecommented;
    ACache aCache;
    Follow userInfo;
    HomePresenter presenter;

    @Override
    protected void onInitPresenters() {
        if (null == mRanking) {
            mRanking = new FragmentRanking();
        }
        if (null == mRecommented) {
            mRecommented = new FragmentRecommented();
        }
        if (null == mFollow) {

            mFollow = new FragmentFollow();
        }
        aCache = ACache.get(this);
        userInfo = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        presenter = new HomePresenter(viewDelegate, this);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= 23) {
            PApplication.getInstance().verifyStoragePermissions(this);
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_home_content, mRanking);
        fragmentTransaction.add(R.id.fragment_home_content, mRecommented);
        fragmentTransaction.add(R.id.fragment_home_content, mFollow);
        fragmentTransaction.commit();
        setTab(1);
        presenter.initData();
        login();
    }

    @Override
    protected Class getDelegateClass() {
        return HomeDelegate.class;
    }


    public void setTab(int i) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (i) {
            case 0:
                viewDelegate.setStatus(0);
                fragmentTransaction.hide(mRecommented);
                fragmentTransaction.hide(mFollow);
                fragmentTransaction.show(mRanking);
                break;
            case 1:
                viewDelegate.setStatus(1);
                fragmentTransaction.hide(mFollow);
                fragmentTransaction.hide(mRanking);
                fragmentTransaction.show(mRecommented);

                break;
            case 2:
                viewDelegate.setStatus(2);
                fragmentTransaction.hide(mRecommented);
                fragmentTransaction.hide(mRanking);
                fragmentTransaction.show(mFollow);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();

    }

    @OnClick({R.id.btn_ranking, R.id.btn_recommented, R.id.btn_follow, R.id.img_home_play, R.id.img_home_top_userinfo, R.id.img_home_search})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ranking:
                setTab(0);
                break;
            case R.id.btn_recommented:
                setTab(1);
                if (null != mRecommented) {
                    mRecommented.getPresend().initData();
                }
                break;
            case R.id.btn_follow:
                setTab(2);
                break;
            case R.id.img_home_play:

                getAnchorInfo();
                break;
            case R.id.img_home_top_userinfo:
                Intent intent2 = new Intent(this, CenterActivity.class);
                startActivity(intent2);
                break;
            case R.id.img_home_search:
                Intent intent3 = new Intent(this, SearchActivity.class);
                startActivity(intent3);
                break;
        }
    }

    public void getAnchorInfo() {
        HttpParams params = new HttpParams();
        if (null != userInfo && null != userInfo.getAnId()) {
            params.put("anId", userInfo.getAnId());
            PostRequest request = OkGo.post(Config.POST_ANCHOR_OPEN).params(params);
            viewDelegate.showPro();
            viewDelegate.getmPlayView().setClickable(false);
            HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showToast("请求失败，请检查网络", HomeActivity.this);
                    viewDelegate.hidePro();
                    viewDelegate.getmPlayView().setClickable(true);
                }

                @Override
                public void onNext(Response response) {
                    viewDelegate.hidePro();
                    viewDelegate.getmPlayView().setClickable(true);
                    HttpBase<Follow> baseF = Parsing.getInstance().ResponseToObject(response, Follow.class);
                    if (baseF.getCode()==1){
                        Follow follow = baseF.getData();
                        F.e("--------------" + follow.toString());
                        if (StringUtils.isNotEmpty(follow.getAnStatus()) && follow.getAnStatus().equals("0")) {
                            Intent intent = new Intent(HomeActivity.this, HWCodecCameraStreamingActivity.class);
                            try {
                                intent.putExtra(Config.EXTRA_KEY_PUB_URL, Config.EXTRA_PUBLISH_URL_PREFIX + new DESUtil().decrypt(follow.getWcPushAddress()));
                                intent.putExtra(Config.EXTRA_KEY_PUB_FOLLOW, follow.chatRoomId);
                                intent.putExtra(Config.EXTRA_KEY_PUB_FOLLOW, follow);
                                startActivity(intent);
                                Updatestatus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            ToastUtil.showToast("您还未申请开通直播，请跳转到个人中心申请", HomeActivity.this);
                        }

                    }else if (baseF.getCode()==0){

                        ToastUtil.showToast("token已过期，请重新登录", HomeActivity.this);
                        ACache.get(HomeActivity.this).clear();
                        new Handler() {
                        }.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                HomeActivity.this.startActivity(intent);
                                PApplication.getInstance().finishActivity();
                            }
                        }, 3000);
                    }


                }
            });
        } else {
            ToastUtil.showToast("您还未申请开通直播，请跳转到个人中心申请", this);
        }


    }

    public void Updatestatus() {

        HttpParams params = new HttpParams();
        if (null != userInfo && null != userInfo.getAnId()) {
            params.put("anId", userInfo.getAnId());
            params.put("status", "2");
            params.put("anPrice", 0);
            PostRequest request = OkGo.post(Config.MEMBER_LIVESTATUS).params(params);
            HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewDelegate.hidePro();
                    viewDelegate.getmPlayView().setClickable(true);
                }

                @Override
                public void onNext(Response response) {
                    viewDelegate.hidePro();
                    viewDelegate.getmPlayView().setClickable(true);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getInfo();

    }

    @Override
    public void Refresh(Follow follow) {
        userInfo = follow;
    }

    public void login() {

        String userName = "111";
        String password = "111";
        UserInfo loginInfo = userInfo.getMember();
        if (null != loginInfo) {
            userName = loginInfo.getMbPhone() + "";
            password = MD5Util.getMD5("webcast" + loginInfo.getMbId());
        }


        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("main", "progress" + progress + "");
            }

            @Override
            public void onError(int code, String message) {
                Log.e("main", "登录聊天服务器失败！");
                if (code == 200) {
                    EMClient.getInstance().logout(true);
                    login();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomeActivity.this, "登录失败，可能是服务器不稳定", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        addConnectionListener();
    }

    void addConnectionListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(final int error) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED) {
                            HomeActivity.this.finish();
                            Toast.makeText(HomeActivity.this, "帐号已经被移除", Toast.LENGTH_LONG).show();
                        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            HomeActivity.this.finish();
                            Toast.makeText(HomeActivity.this, "帐号在其他设备登录", Toast.LENGTH_LONG).show();
                        } else {
//                            HomeActivity.this.finish();
//                            if (NetUtils.hasNetwork(HomeActivity.this))
//                                Toast.makeText(HomeActivity.this, "连接不到聊天服务器", Toast.LENGTH_LONG).show();
//                            else
//                                Toast.makeText(HomeActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
