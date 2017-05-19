package cn.gtgs.base.playpro.activity.home.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IRecommentedItemListener;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.RecommentedPresenter;
import cn.gtgs.base.playpro.activity.home.fragment.view.RecommentedDelegate;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.presenter.FragmentPresenter;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.ToastUtil;

/**
 * Created by  on 2017/4/26.
 */

public class FragmentRecommented extends FragmentPresenter<RecommentedDelegate> implements SwipeRefreshLayout.OnRefreshListener, IRecommentedItemListener {
    RecommentedPresenter presenter;
    Follow mF;
    UserInfo info;

    @Override
    protected Class<RecommentedDelegate> getDelegateClass() {
        return RecommentedDelegate.class;
    }

    @Override
    public void init() {
        presenter = new RecommentedPresenter(viewDelegate, this);
        presenter.initData();
        viewDelegate.getmSwp().setOnRefreshListener(this);
        mF = (Follow) ACache.get(getActivity()).getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = mF.getMember();
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }


    public RecommentedPresenter getPresend()
    {
        return presenter;
    }
    @Override
    public void itemCliclk(Follow follow) {
        mF = (Follow) ACache.get(getActivity()).getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = mF.getMember();
        if (follow.getLiveStatus().equals("3") || follow.getAnPrice() != 0) {
            showShoufeiDialog(follow);
        } else {
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            intent.putExtra("anchoritem", follow);
            intent.putExtra("IsMember", false);
            getActivity().startActivity(intent);
        }
    }

    public void showShoufeiDialog(final Follow follow) {
        final AlertDialog mydialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTransBackGround);
        mydialog = builder.create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog_releaseagent2, null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_dialog_content);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
        Button bt_yes = (Button) view.findViewById(R.id.bt_dialog_yes);
        tv_content.setText("主播发起收费模式（" + follow.getAnPrice() + "钻石/每分钟），当前您有" + info.getMbGold() + "钻石，是否继续观看？");
        bt_yes.setText("继续观看");
        mydialog.setCancelable(true);
        mydialog.show();
        mydialog.setContentView(view);

        // dialog内部的点击事件
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (info.getMbGold() < follow.getAnPrice()) {
                    ToastUtil.showToast("您当前钻石不够，请充值后再观看", getActivity());
                } else {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    intent.putExtra("anchoritem", follow);
                    intent.putExtra("IsMember", false);
                    getActivity().startActivity(intent);
                }
                mydialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
    }

    public void initLoadMoreListener()
    {
        viewDelegate.getmRecContent().setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == viewDelegate.getmRecContent().getAdapter().getItemCount()) {

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            List<String> footerDatas = new ArrayList<String>();
//                            for (int i = 0; i< 10; i++) {
//
//                                footerDatas.add("footer  item" + i);
//                            }
//                            viewDelegate.getmRecContent().getAdapter().AddFooterItem(footerDatas);
//                        }
//                    }, 3000);
                    viewDelegate.getmSwp().setRefreshing(true);

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });


    }
}
