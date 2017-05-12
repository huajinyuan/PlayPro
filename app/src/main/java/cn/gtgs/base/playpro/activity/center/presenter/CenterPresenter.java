package cn.gtgs.base.playpro.activity.center.presenter;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import cn.gtgs.base.playpro.activity.center.view.CenterDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by hjy on 17/5/6.
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
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                if (delegate.getmSwp().isRefreshing()) {
                    delegate.getmSwp().setRefreshing(false);
                }
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
                HttpBase<Follow> bs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (null != bs.getData()) {
                    aCache.put(ACacheKey.CURRENT_ACCOUNT, bs.getData());
                    delegate.init();
                }
            }
        });


    }
}
