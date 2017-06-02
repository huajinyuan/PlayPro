package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import android.content.Intent;
import android.os.Handler;

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
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
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
        HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
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
                        } else if (i == 0) {
                            ToastUtil.showToast("token已过期，请重新登录", delegate.getActivity());
                            ACache.get(delegate.getActivity()).clear();
                            new Handler() {
                            }.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(delegate.getActivity(), LoginActivity.class);
                                    delegate.getActivity().startActivity(intent);
                                    PApplication.getInstance().finishActivity();
                                }
                            }, 3000);
                        }
                    }
                } catch (Exception e) {
                    F.e(e.toString());
                }


            }
        });
    }

    private void getData() {

        HttpParams params = new HttpParams();
        params.put("page", "1");
        params.put("count", "100");
        GetRequest request = OkGo.get(Config.COMMON_MONTHTOP).params(params);
        HttpMethods.getInstance().doGet(request, true).subscribe(new Subscriber<Response>() {
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
                if (delegate.getmSwp().isRefreshing()) {
                    delegate.getmSwp().setRefreshing(false);
                }
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
