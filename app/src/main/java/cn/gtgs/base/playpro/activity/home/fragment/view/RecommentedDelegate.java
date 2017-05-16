package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.adapter.RecommentedAdapter;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IRecommentedItemListener;
import cn.gtgs.base.playpro.activity.home.model.ADInfo;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.widget.bn.CarouselView;

/**
 * Created by  on 2017/4/25.
 */

public class RecommentedDelegate extends AppDelegate {
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.rec_recomment_content)
    RecyclerView mRecContent;
    @BindView(R.id.swp_pull_comment)
    SwipeRefreshLayout mSwp;
    @BindView(R.id.CarouselView)
    CarouselView cardView;
    RecommentedAdapter adapter;
    ArrayList<Follow> mDatas = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_recommented;
    }

    public void setData(ArrayList<Follow> follows, IRecommentedItemListener listener) {
        mDatas.clear();
        mDatas.addAll(follows);
        if (mSwp.isRefreshing()) {
            mSwp.setRefreshing(false);
        }
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (null == adapter) {
            mRecContent.setLayoutManager(gridLayoutManager);
//            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new RecommentedAdapter(mDatas, getActivity());
            adapter.setListener(listener);
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(gridLayoutManager);
        } else {
            adapter.notifyDataSetChanged();

        }
    }

    public SwipeRefreshLayout getmSwp() {
        return mSwp;
    }

    public void setAD(final ArrayList<ADInfo> ads) {
        cardView.setVisibility(View.VISIBLE);
        cardView.setAdapter(new CarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.ad_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                Glide.with(getActivity()).load(Config.BASE + ads.get(position).getAdImage()).into(imageView);
                return view;
            }

            @Override
            public int getCount() {
                return ads.size();
            }
        });
    }
    public RecyclerView getmRecContent()
    {
        return mRecContent;
    }

}
