package cn.gtgs.base.playpro.activity.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.TyrantsPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.TyrantsDelegate;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by  on 2017/4/26.
 */

public class FragmentTyrants extends FragmentPresenter<TyrantsDelegate> implements IFollowItemListener, SwipeRefreshLayout.OnRefreshListener {
    TyrantsPresenter presenter;

    @Override
    protected Class getDelegateClass() {
        return TyrantsDelegate.class;
    }

    @Override
    public void init() {
        super.init();
        presenter = new TyrantsPresenter(viewDelegate, this);
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

    }

    @Override
    public void onRefresh() {
        presenter.initData();
    }
}
