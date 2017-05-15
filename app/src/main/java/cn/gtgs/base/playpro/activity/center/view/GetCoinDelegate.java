package cn.gtgs.base.playpro.activity.center.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.adapter.CoinAdapter;
import cn.gtgs.base.playpro.activity.center.model.Gold;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by  on 2017/4/25.
 */

public class GetCoinDelegate extends AppDelegate {
    @BindView(R.id.rec_getcoin_content)
    RecyclerView mRecContent;
    @BindView(R.id.tv_gold_1)
            TextView mTvGold;
    CoinAdapter adapter;
    LinearLayoutManager manager;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_get_coin;
    }

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    private ArrayList<Gold> mData = new ArrayList<>();

    public void init()
    {
        setmTvTitle("我的金币");
        mData.add(new Gold("100",null,"10"));
        mData.add(new Gold("200",null,"20"));
        mData.add(new Gold("300",null,"30"));
        mData.add(new Gold("500",null,"50"));
        mData.add(new Gold("1000","5","100"));
        mData.add(new Gold("2000","10","200"));
        mData.add(new Gold("3000","20","300"));
        mData.add(new Gold("5000","30","500"));
        mData.add(new Gold("10000","50","1000"));
        setData();



    }
    public void setmTvGold(String gold)
    {
        mTvGold.setText(gold);
    }

    public void setmTvTitle(String title)
    {
        mTvTitle.setText(title);
    }
    public void setData() {
        if (null == adapter) {
            manager = new LinearLayoutManager(getActivity());
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new CoinAdapter(mData, getActivity());
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();
            F.e("-------------------notifyDataSetChanged");
        }
    }
}
