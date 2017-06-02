package cn.gtgs.base.playpro.activity.center.presenter;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.center.model.TxInfo;
import cn.gtgs.base.playpro.activity.center.view.CZListDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.BaseList;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 17/5/6.
 */

public class TxListPresenter {
    CZListDelegate delegate;
    ACache aCache;

    public TxListPresenter(CZListDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
    }


    public void getdata(final boolean isOff) {
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        HttpParams params = new HttpParams();
        if (null != follow && StringUtils.isNotEmpty(follow.getAnId())) {
            params.put("anId", follow.getAnId());
            PostRequest request;
            if (!isOff) {
                request = OkGo.post(Config.COMMON_TXON).params(params);

            } else {
                request = OkGo.post(Config.COMMON_TXOFF).params(params);
            }
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
                    BaseList<TxInfo> bs = Parsing.getInstance().ResponseToList3(response, TxInfo.class);
                    delegate.setData(isOff, (ArrayList<TxInfo>) bs.getDataList());

                }
            });
        }


    }
}
