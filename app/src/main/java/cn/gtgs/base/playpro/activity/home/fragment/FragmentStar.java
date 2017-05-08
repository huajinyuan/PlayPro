package cn.gtgs.base.playpro.activity.home.fragment;

import android.content.Intent;

import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.StarPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.StarDelegate;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;

/**
 * Created by gtgs on 2017/4/26.
 */

public class FragmentStar extends FragmentPresenter<StarDelegate> implements IFollowItemListener {
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
        presenter.doFollow(follow.getAnId());
    }
}
