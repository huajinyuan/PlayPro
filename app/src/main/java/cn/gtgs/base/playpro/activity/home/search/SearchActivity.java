package cn.gtgs.base.playpro.activity.home.search;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.search.presenter.ISearch;
import cn.gtgs.base.playpro.activity.home.search.presenter.ISearchItemListenser;
import cn.gtgs.base.playpro.activity.home.search.presenter.SearchPresenter;
import cn.gtgs.base.playpro.activity.home.search.view.SearchDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;

public class SearchActivity extends ActivityPresenter<SearchDelegate> implements ISearchItemListenser {

    ISearch presenter;

    @Override
    protected void onInitPresenters() {
        presenter = new SearchPresenter(viewDelegate, this);
    }

    @Override
    protected void initData() {
        viewDelegate.setTitle();
    }

    @Override
    protected Class getDelegateClass() {
        return SearchDelegate.class;
    }

    @OnClick({R.id.img_topbar_back, R.id.btn_search})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.btn_search:
                presenter.search();
                break;
        }
    }

    @Override
    public void itemClick(Follow follow) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("anchoritem", follow);
        intent.putExtra("IsMember", true);
        startActivity(intent);
        this.finish();
    }
}
