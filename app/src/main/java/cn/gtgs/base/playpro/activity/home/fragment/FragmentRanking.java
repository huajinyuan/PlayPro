package cn.gtgs.base.playpro.activity.home.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.view.RankingDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by  on 2017/4/26.
 */

public class FragmentRanking extends FragmentPresenter<RankingDelegate> {
    private FragmentManager fragmentManager;
    FragmentTyrants tyrants;
    FragmentStar star;
    FragmentWeek mWeek;

    @Override
    protected Class getDelegateClass() {
        return RankingDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        tyrants = new FragmentTyrants();
        star = new FragmentStar();
        mWeek = new FragmentWeek();
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_ranking_content, tyrants);
        fragmentTransaction.add(R.id.layout_ranking_content, star);
        fragmentTransaction.add(R.id.layout_ranking_content, mWeek);
        fragmentTransaction.show(tyrants);
        fragmentTransaction.hide(star);
        fragmentTransaction.hide(mWeek);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.btn_tyrants, R.id.btn_star, R.id.btn_star_week})
    public void Onclick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_tyrants:
                viewDelegate.setStatus(0);
                fragmentTransaction.show(tyrants);
                fragmentTransaction.hide(star);
                fragmentTransaction.hide(mWeek);

                break;
            case R.id.btn_star:
                fragmentTransaction.show(star);
                fragmentTransaction.hide(mWeek);
                fragmentTransaction.hide(tyrants);
                viewDelegate.setStatus(1);
                break;
            case R.id.btn_star_week:
                fragmentTransaction.show(mWeek);
                fragmentTransaction.hide(star);
                fragmentTransaction.hide(tyrants);
                viewDelegate.setStatus(2);
                break;
        }
        fragmentTransaction.commit();
    }

}
