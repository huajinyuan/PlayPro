package cn.gtgs.base.playpro.activity.home.fragment;

import cn.gtgs.base.playpro.activity.home.fragment.view.FollowDelegate;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentFollow extends FragmentPresenter<FollowDelegate> {
    @Override
    protected Class getDelegateClass() {
        return FollowDelegate.class;
    }
}
