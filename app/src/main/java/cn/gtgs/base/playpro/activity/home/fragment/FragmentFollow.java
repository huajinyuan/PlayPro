package cn.gtgs.base.playpro.activity.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

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

public class FragmentFollow extends FragmentPresenter<FollowDelegate> implements IFollowItemListener, SwipeRefreshLayout.OnRefreshListener {
    IFollow presenter;

    @Override
    protected Class getDelegateClass() {
        return FollowDelegate.class;
    }

    @Override
    public void init() {
        presenter = new FollowPresenter(viewDelegate, this);
        presenter.initData();
        viewDelegate.getmSwp().setOnRefreshListener(this);

    }

    @Override
    public void itemClick(Follow follow) {
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra("anchoritem", follow);
        intent.putExtra("IsMember", false);
        getActivity().startActivity(intent);
    }

    @Override
    public void ItemFolloClick(Follow follow) {
        presenter.unFollow(follow);
    }

    @Override
    public void onRefresh() {
        presenter.initData();
    }
}
