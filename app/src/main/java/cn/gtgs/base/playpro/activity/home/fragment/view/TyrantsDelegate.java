package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.TyrantsAdapter;
import cn.gtgs.base.playpro.activity.home.model.AnchorItem;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by gtgs on 2017/4/25.
 */

public class TyrantsDelegate extends AppDelegate {
    @BindView(R.id.rec_tyrants_content)
    RecyclerView mRecContent;
    TyrantsAdapter adapter;
    LinearLayoutManager manager;
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_tyrants;
    }
    public void setData(ArrayList<UserInfo> data)
    {
        manager = new LinearLayoutManager(getActivity());
        if (null == adapter) {
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new TyrantsAdapter(data, getActivity());
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();

        }
    }
}
