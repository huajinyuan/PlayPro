package cn.gtgs.base.playpro.activity.home;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.CenterActivity;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentFollow;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRanking;
import cn.gtgs.base.playpro.activity.home.fragment.FragmentRecommented;
import cn.gtgs.base.playpro.activity.home.live.HWCodecCameraStreamingActivity;
import cn.gtgs.base.playpro.activity.home.view.HomeDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class HomeActivity extends ActivityPresenter<HomeDelegate> {
    private FragmentManager fragmentManager;
    FragmentRanking mRanking;
    FragmentFollow mFollow;
    private FragmentTransaction fragmentTransaction;
    FragmentRecommented mRecommented;

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
                Intent intent = new Intent(this, HWCodecCameraStreamingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_home_top_userinfo:
                Intent intent2 = new Intent(this, CenterActivity.class);
                startActivity(intent2);
                break;
        }
    }

}
