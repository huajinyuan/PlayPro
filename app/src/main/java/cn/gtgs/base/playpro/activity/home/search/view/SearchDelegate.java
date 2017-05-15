package cn.gtgs.base.playpro.activity.home.search.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.search.adapter.SearchAdapter;
import cn.gtgs.base.playpro.activity.home.search.presenter.ISearchItemListenser;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.widget.DividerGridItemDecoration;

/**
 * Created by  on 2017/1/13.
 */

public class SearchDelegate extends AppDelegate {
    @BindView(R.id.tv_topbar_title)
    TextView mTvtitle;
    @BindView(R.id.rec_search)
    RecyclerView mRecContent;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.progressbar_search_p)
    ProgressBar progressbar_search_p;
    LinearLayoutManager manager;
    ArrayList<Follow> mData = new ArrayList<>();
    SearchAdapter adapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_search;
    }

    public void setTitle() {
        mTvtitle.setText("搜索");
    }

    public void setData(ArrayList<Follow> data, ISearchItemListenser listener) {
        mData.clear();
        mData.addAll(data);
        if (null == adapter) {
            manager = new LinearLayoutManager(getActivity());
            mRecContent.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            adapter = new SearchAdapter(mData, getActivity());
            adapter.setListener(listener);
            mRecContent.setAdapter(adapter);
            mRecContent.setLayoutManager(manager);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public String getSearchContent() {
        return mEdtSearch.getText().toString().trim();
    }

    public Button getmBtnSearch() {
        return mBtnSearch;
    }
    public void hideProgressbar()
    {
        progressbar_search_p.setVisibility(View.GONE);
    }

    public void showProgressbar()
    {
        progressbar_search_p.setVisibility(View.VISIBLE);
    }
}
