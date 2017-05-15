package cn.gtgs.base.playpro.activity.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.StarPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.StarDelegate;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by  on 2017/4/26.
 */

public class FragmentStar extends FragmentPresenter<StarDelegate> implements IFollowItemListener, SwipeRefreshLayout.OnRefreshListener {
    StarPresenter presenter;

    @Override
    protected Class getDelegateClass() {
        return StarDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        presenter = new StarPresenter(viewDelegate, this);
        presenter.initData();
        viewDelegate.getmSwp().setOnRefreshListener(this);

    }

    @Override
    public void itemClick(Follow follow) {
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        intent.putExtra("anchoritem", follow);
        intent.putExtra("IsMember", true);
        getActivity().startActivity(intent);

    }

    @Override
    public void ItemFolloClick(Follow follow) {
        presenter.doFollow(follow.getAnId());
    }

    @Override
    public void onRefresh() {
        presenter.initData();
    }
}
