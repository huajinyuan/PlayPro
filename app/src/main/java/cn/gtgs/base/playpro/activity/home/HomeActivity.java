package cn.gtgs.base.playpro.activity.home;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.CenterActivity;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentFollow;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRanking;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRecommented;
import cn.gtgs.base.playpro.activity.home.live.HWCodecCameraStreamingActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.view.HomeDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;
import rx.Subscriber;

public class HomeActivity extends ActivityPresenter<HomeDelegate> {
    private FragmentManager fragmentManager;
    FragmentRanking mRanking;
    FragmentFollow mFollow;
    private FragmentTransaction fragmentTransaction;
    FragmentRecommented mRecommented;
    ACache aCache;
    Follow userInfo;
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
    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_home_content, mRanking);
        fragmentTransaction.add(R.id.fragment_home_content, mRecommented);
        fragmentTransaction.add(R.id.fragment_home_content, mFollow);
        fragmentTransaction.hide(mRecommented);
        fragmentTransaction.hide(mFollow);
        fragmentTransaction.show(mRanking);
        fragmentTransaction.commit();
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

    @OnClick({R.id.btn_ranking, R.id.btn_recommented, R.id.btn_follow, R.id.img_home_play, R.id.img_home_top_userinfo})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ranking:
                setTab(0);
                break;
            case R.id.btn_recommented:
                setTab(1);
                break;
            case R.id.btn_follow:
                setTab(2);
                break;
            case R.id.img_home_play:
//                Intent intent = new Intent(this, HWCodecCameraStreamingActivity.class);
//                startActivity(intent);
                getAnchorInfo();
                break;
            case R.id.img_home_top_userinfo:
                Intent intent2 = new Intent(this, CenterActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void getAnchorInfo()
    {
        HttpParams params = new HttpParams();
        if (null != userInfo && null != userInfo.getAnId())
        {
            params.put("anId",userInfo.getAnId());
            PostRequest request = OkGo.post(Config.POST_ANCHOR_OPEN).params(params);
            HttpMethods.getInstance().doPost(request,false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {

                    HttpBase<Follow> baseF = Parsing.getInstance().ResponseToObject(response,Follow.class);
                    Follow follow = baseF.getData();
                    F.e("--------------"+follow.getWcPushAddress());
                    Intent intent = new Intent(HomeActivity.this, HWCodecCameraStreamingActivity.class);
                    intent.putExtra(Config.EXTRA_KEY_PUB_URL,Config.EXTRA_PUBLISH_URL_PREFIX+follow.getWcPushAddress());
                    intent.putExtra(Config.EXTRA_KEY_PUB_FOLLOW,follow.chatRoomId);
                    startActivity(intent);
//                startPush("URL:"+follow.getWcPullAddress());
                }
            });
        }

//        PostRequest request = OkGo.post(Config.POST_ANCHOR_GET).params(params);


    }

}
