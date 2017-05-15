package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.FollowAdapter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by  on 2017/4/25.
 */

public class FollowDelegate extends AppDelegate {
    @BindView(R.id.rec_recomment_content)
    RecyclerView mRecContent;
    @BindView(R.id.swp_pull_comment)
    SwipeRefreshLayout mSwp;
    LinearLayoutManager manager;
    FollowAdapter adapter;
    ArrayList<Follow> mData = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_recommented;
    }

    public void setData(ArrayList<Follow> data, IFollowItemListener listener) {
        if (mSwp.isRefreshing()) {
            mSwp.setRefreshing(false);
        }
        mData.clear();
        mData.addAll(data);
        if (null == adapter) {
            manager = new LinearLayoutManager(getActivity());
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new FollowAdapter(mData, getActivity());
            adapter.setListener(listener);
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();
            F.e("-------------------notifyDataSetChanged");
        }
    }

    public SwipeRefreshLayout getmSwp() {
        return mSwp;
    }
}
