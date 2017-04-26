package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.RecommentedAdapter;
import cn.gtgs.base.playpro.activity.home.model.AnchorItem;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by gtgs on 2017/4/25.
 */

public class RecommentedDelegate extends AppDelegate {
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.rec_recomment_content)
    RecyclerView mRecContent;
    RecommentedAdapter adapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_recommented;
    }

    public void setData(ArrayList<AnchorItem> anchorItems) {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (null == adapter) {
            mRecContent.setLayoutManager(gridLayoutManager);
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new RecommentedAdapter(anchorItems, getActivity());
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(gridLayoutManager);
        } else {
            adapter.notifyDataSetChanged();

        }
    }
}
