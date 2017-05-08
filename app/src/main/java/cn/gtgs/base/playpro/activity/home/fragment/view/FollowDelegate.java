package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.FollowAdapter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by gtgs on 2017/4/25.
 */

public class FollowDelegate extends AppDelegate {
    @BindView(R.id.rec_recomment_content)
    RecyclerView mRecContent;
    LinearLayoutManager manager;
    FollowAdapter adapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_recommented;
    }

    public void setData(ArrayList<Follow> data, IFollowItemListener listener) {
        manager = new LinearLayoutManager(getActivity());
        if (null == adapter) {
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new FollowAdapter(data, getActivity());
            adapter.setListener(listener);
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();

        }
    }
}
