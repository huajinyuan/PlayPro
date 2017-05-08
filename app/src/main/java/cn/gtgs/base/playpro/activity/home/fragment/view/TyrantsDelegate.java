package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.TyrantsAdapter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by gtgs on 2017/4/25.
 */

public class TyrantsDelegate extends AppDelegate {
    @BindView(R.id.rec_tyrants_content)
    RecyclerView mRecContent;
    @BindView(R.id.swp_pull_comment)
    SwipeRefreshLayout mSwp;
    TyrantsAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<UserInfo> mData = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_tyrants;
    }

    public void setData(ArrayList<UserInfo> data, IFollowItemListener listener) {
        if (mSwp.isRefreshing())
        {
            mSwp.setRefreshing(false);
        }
        mData.clear();
        mData.addAll(data);
        if (null == adapter) {
            manager = new LinearLayoutManager(getActivity());
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new TyrantsAdapter(mData, getActivity());
            adapter.setListener(listener);
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();

        }
    }

    public SwipeRefreshLayout getmSwp() {
        return mSwp;
    }
}
