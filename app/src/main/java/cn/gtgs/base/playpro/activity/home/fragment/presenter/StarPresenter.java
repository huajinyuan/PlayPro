package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.activity.home.fragment.view.StarDelegate;
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

public class StarPresenter implements IRecommented {
    StarDelegate delegate;
    IFollowItemListener listener;
    UserInfo info;
    ACache aCache;

    public StarPresenter(StarDelegate delegate, IFollowItemListener listener) {
        this.delegate = delegate;
        this.listener = listener;
        aCache = ACache.get(delegate.getActivity());
        Follow f = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = f.getMember();

    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void doFollow(final String anId) {
        HttpParams params = new HttpParams();
        params.put("mbId", info.getMbId());
        params.put("anId", anId);
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

//                    org.json.JSONObject ob = new org.json.JSONObject()
//                    JSONObject object = JSON.parseObject(response.body().toString());
                    if (ob.containsKey("code")) {
                        int i = ob.getIntValue("code");
                        if (i == 1) {
                            int a = ob.getInteger("data");
                            ArrayList<String> gs = PApplication.getInstance().getmFList();
                            if (a == 1) {
                                gs.add(anId);

                            } else {
                                gs.remove(anId);
                            }
                            String str = JSON.toJSONString(gs);
                            aCache.put(ACacheKey.CURRENT_FOLLOW, str);
//                            ToastUtil.showToast("操作成功",delegate.getActivity());
                            delegate.AddapterChange();
                        }
                    }
                } catch (Exception e) {
                    F.e(e.toString());
                }


            }
        });
    }

    private void getData() {

        GetRequest request = OkGo.get(Config.POST_ANCHOR_TOP);
//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");
        HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {

                try {
                    F.e(response.body().toString());
                    ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList3(response, Follow.class).dataList;
                    delegate.setData(lists, listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                delegate.setData(lists);
            }
        });

    }

}
