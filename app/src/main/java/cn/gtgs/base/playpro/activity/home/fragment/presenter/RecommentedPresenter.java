package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.io.IOException;
import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.RecommentedDelegate;
import cn.gtgs.base.playpro.activity.home.model.ADInfo;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.BaseList;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 2017/2/10.
 */

public class RecommentedPresenter implements IRecommented {
    RecommentedDelegate delegate;
    IRecommentedItemListener listener;
    ACache aCache;
    Follow follow;
    UserInfo info;
    private int page = 1;

    public RecommentedPresenter(RecommentedDelegate delegate, IRecommentedItemListener listener) {
        this.delegate = delegate;
        this.listener = listener;
        aCache = ACache.get(delegate.getActivity());
        follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    @Override
    public void initData() {
        doRefresh();
        getData(true);
        getAdverts();
    }


    @Override
    public void doFollow(String anId) {

    }

    public void refresh() {
        page = 1;
        doRefresh();
        getData(true);
    }


    public void getData(final boolean isRefresh) {

//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");
        if (!isRefresh) {
            page++;
        }

        HttpParams params = new HttpParams();
        params.put("page", page);
        params.put("count", "5");
        params.put("isRecommend", "1");

//        PostRequest request = OkGo.post(Config.POST_ANCHOR_LIST).params(params);
        PostRequest request = OkGo.post(Config.POST_ANCHOR_LIST_LIVE).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
                if (!isRefresh) {
                    page--;
                }
                delegate.getmPullLoadMoreRecyclerView().setPullLoadMoreCompleted();
            }

            @Override
            public void onNext(Response response) {
                BaseList<Follow> bList = Parsing.getInstance().ResponseToList2(response, Follow.class);
                F.e("-----------------------------" + bList.toString());
                ArrayList<Follow> follows = (ArrayList<Follow>) bList.getDataList();
                F.e("-----------------------------" + follows.toString());
                delegate.setData(follows, listener, isRefresh);
                delegate.getmPullLoadMoreRecyclerView().setPullLoadMoreCompleted();
            }
        });

    }

    public void getAdverts() {
//        GET_ADVERTS
        GetRequest request = OkGo.get(Config.GET_ADVERTS);
        HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
                String Str = null;
                try {
                    Str = response.body().string();
                    JSONObject ob = JSON.parseObject(Str);
                    if (ob.containsKey("dataList")) {
                        ArrayList<ADInfo> r = (ArrayList<ADInfo>) JSON.parseArray(ob.getJSONArray("dataList").toJSONString(), ADInfo.class);
                        if (null != r && !r.isEmpty()) {
                            delegate.setAD(r);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void doRefresh() {
        HttpParams params = new HttpParams();
        params.put("mbId", info.getMbId());
        PostRequest request = OkGo.post(Config.POST_MEMBER_GET).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
//                if (delegate.getmSwp().isRefreshing()) {
//                    delegate.getmSwp().setRefreshing(false);
//                }
                delegate.getmPullLoadMoreRecyclerView().setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
//                if (delegate.getmSwp().isRefreshing()) {
//                    delegate.getmSwp().setRefreshing(false);
//                }
                delegate.getmPullLoadMoreRecyclerView().setPullLoadMoreCompleted();
            }

            @Override
            public void onNext(Response response) {
//                if (delegate.getmSwp().isRefreshing()) {
//                    delegate.getmSwp().setRefreshing(false);
//                }
                delegate.getmPullLoadMoreRecyclerView().setPullLoadMoreCompleted();
                HttpBase<Follow> bs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (null != bs.getData()) {
                    follow = bs.getData();
                    aCache.put(ACacheKey.CURRENT_ACCOUNT, bs.getData());
                }
            }
        });


    }
}
