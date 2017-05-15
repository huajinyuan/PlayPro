package cn.gtgs.base.playpro.activity.home.presenter;


import com.alibaba.fastjson.JSON;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.view.HomeDelegate;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 2017/2/10.
 */

public class HomePresenter implements IHome {
    HomeDelegate delegate;
    IHomeRefreshListener listener;
    UserInfo info;
    ACache aCache;

    public HomePresenter(HomeDelegate delegate, IHomeRefreshListener listener) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow fl = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = fl.getMember();
        this.delegate = delegate;
        this.listener = listener;

    }

    @Override
    public void initData() {
        getInfo();
        getData();
    }


    private void getData() {

        if (null != info) {
            HttpParams params = HttpMethods.getInstance().getHttpParams();
            params.put("mbId", info.getMbId());
            params.put("page", "1");
            params.put("count", "10000");
            GetRequest request = OkGo.get(Config.POST_ANCHOR_MEMBER_FAVLIST).params(params);
            HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {
//                        F.e(response.body().toString());
                    try {

                        ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList2(response, Follow.class).dataList;
                        ArrayList<String> foll = new ArrayList<>();
                        for (Follow f : lists) {
                            foll.add(f.getAnId());
                        }
                        String str = JSON.toJSONString(foll);
                        aCache.put(ACacheKey.CURRENT_FOLLOW, str);
                        //                        delegate.setData(lists, listener);
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

    public void getInfo() {

        HttpParams params = new HttpParams();
        params.put("mbId", info.getMbId());
        PostRequest request = OkGo.post(Config.POST_MEMBER_GET).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
                HttpBase<Follow> bs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (null != bs.getData()) {
                    aCache.put(ACacheKey.CURRENT_ACCOUNT, bs.getData());
                    if (null != listener) {
                        listener.Refresh(bs.getData());
                    }
                }
            }
        });
    }

}
