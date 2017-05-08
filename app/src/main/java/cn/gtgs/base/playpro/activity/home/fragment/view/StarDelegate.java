package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.Follow_StarAdapter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by gtgs on 2017/4/25.
 */

public class StarDelegate extends AppDelegate{
    @BindView(R.id.rec_star_content)
    RecyclerView mRecContent;
    Follow_StarAdapter mFollowAdapter;
    LinearLayoutManager manager;
    ArrayList<Follow> mData = new ArrayList<>();


    @Override
    public int getRootLayoutId() {

        return R.layout.fragment_star;
    }

    public void setData(ArrayList<Follow> data,IFollowItemListener listener) {
        manager = new LinearLayoutManager(getActivity());
        if (null == mFollowAdapter) {
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            mRecContent.setLayoutManager(manager);
            mFollowAdapter = new Follow_StarAdapter(mData, getActivity());
            mFollowAdapter.setiFollowItemListener(listener);
            mRecContent.setAdapter(mFollowAdapter);
        }
        mData.clear();
        mData.addAll(data);
        mFollowAdapter.notifyDataSetChanged();


    }

}
