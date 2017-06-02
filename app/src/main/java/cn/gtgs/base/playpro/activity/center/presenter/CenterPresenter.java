package cn.gtgs.base.playpro.activity.center.presenter;

import android.content.Intent;
import android.os.Handler;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.activity.center.view.CenterDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 17/5/6.
 */

public class CenterPresenter {
    CenterDelegate delegate;
    UserInfo info;
    ACache aCache;
    public String path;

    public CenterPresenter(CenterDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    public void doRefresh() {
        HttpParams params = new HttpParams();
        params.put("mbId", info.getMbId());
        PostRequest request = OkGo.post(Config.POST_MEMBER_GET).params(params);
        HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                if (delegate.getmSwp().isRefreshing()) {
                    delegate.getmSwp().setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
                if (delegate.getmSwp().isRefreshing()) {
                    delegate.getmSwp().setRefreshing(false);
                }
            }

            @Override
            public void onNext(Response response) {
                if (delegate.getmSwp().isRefreshing()) {
                    delegate.getmSwp().setRefreshing(false);
                }
                HttpBase<Follow> bs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (bs.getCode() == 1) {
                    if (null != bs.getData()) {
                        String token = info.getToken();
                        info = bs.getData().getMember();
                        info.setToken(token);
                        Follow follow = bs.getData();
                        follow.setMember(info);
                        aCache.put(ACacheKey.CURRENT_ACCOUNT, follow);
                        delegate.init();
                    }
                } else if (bs.getCode() == 0) {
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
        });


    }
}
