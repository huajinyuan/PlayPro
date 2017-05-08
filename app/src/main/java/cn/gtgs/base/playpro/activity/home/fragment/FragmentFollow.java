package cn.gtgs.base.playpro.activity.home.fragment;

import android.content.Intent;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.FollowPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollow;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.fragment.view.FollowDelegate;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentFollow extends FragmentPresenter<FollowDelegate> implements IFollowItemListener {
    IFollow presenter;

    @Override
    protected Class getDelegateClass() {
        return FollowDelegate.class;
    }

    @Override
    public void init() {
        presenter = new FollowPresenter(viewDelegate, this);
        presenter.initData();

    }

    @Override
    public void itemClick(Follow follow) {
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra("anchoritem",follow);
        intent.putExtra("IsMember", false);
        getActivity().startActivity(intent);
    }

    @Override
    public void ItemFolloClick(Follow follow) {

    }
}
