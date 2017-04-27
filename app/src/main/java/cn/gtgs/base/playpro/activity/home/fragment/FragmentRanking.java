package cn.gtgs.base.playpro.activity.home.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.view.RankingDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentRanking extends FragmentPresenter<RankingDelegate> {
    private FragmentManager fragmentManager;
    FragmentTyrants tyrants;

    @Override
    protected Class getDelegateClass() {
        return RankingDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        tyrants = new FragmentTyrants();
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_ranking_content, tyrants);
        fragmentTransaction.show(tyrants);
        fragmentTransaction.commit();

    }
}
