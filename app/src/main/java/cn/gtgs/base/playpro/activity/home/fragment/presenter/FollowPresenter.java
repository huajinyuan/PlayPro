package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.activity.home.fragment.view.FollowDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class FollowPresenter implements IFollow {
    FollowDelegate delegate;
    IFollowItemListener listener;
    UserInfo info;
    ACache aCache;

    public FollowPresenter(FollowDelegate delegate, IFollowItemListener listener) {
        this.delegate = delegate;
        this.listener = listener;
        aCache = ACache.get(delegate.getActivity());
        Follow fl = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = fl.getMember();
        this.delegate = delegate;

    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void unFollow(final Follow follow) {
        HttpParams params = new HttpParams();
        params.put("mbId", info.getMbId());
        params.put("anId", follow.getAnId());
        PostRequest request = OkGo.post(Config.POST_ANCHOR_MEMBER_fav).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
//                HttpBase base = Parsing.getInstance().ResponseToObject(response)
                try {
                    String Str = response.body().string();
                    F.e("-----------------" + Str);
                    JSONObject ob = JSON.parseObject(Str);
                    if (ob.containsKey("code")) {
                        int i = ob.getIntValue("code");
                        if (i == 1) {
                            int a = ob.getInteger("data");
                            ArrayList<String> gs = PApplication.getInstance().getmFList();
                            if (a == 1) {
                                gs.add(follow.getAnId());

                            } else {
                                gs.remove(follow.getAnId());
                            }
                            String str = JSON.toJSONString(gs);
                            aCache.put(ACacheKey.CURRENT_FOLLOW, str);
                            getData();
                        }
                    }
                } catch (Exception e) {
                    F.e(e.toString());
                }
            }
        });
    }

    private void getData() {

        if (null != info) {
            HttpParams params = HttpMethods.getInstance().getHttpParams();
            params.put("mbId", info.getMbId());
            params.put("page", "1");
            params.put("count", "100");
            GetRequest request = OkGo.get(Config.POST_ANCHOR_MEMBER_FAVLIST).params(params);
            HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (delegate.getmSwp().isRefreshing()) {
                        delegate.getmSwp().setRefreshing(false);
                    }
                }

                @Override
                public void onNext(Response response) {
//                        F.e(response.body().toString());
                    try {
                        ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList2(response, Follow.class).dataList;
                        delegate.setData(lists, listener);
                        F.e("--------------------------" + lists.size());
                        F.e("--------------------------" + lists.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                ArrayList<AnchorItem> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
                }
            });
        }
//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");


    }


}
