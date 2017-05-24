package cn.gtgs.base.playpro.activity.center.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.io.IOException;

import cn.gtgs.base.playpro.activity.center.view.TXDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 17/5/6.
 */

public class TxPresenter {
    TXDelegate delegate;
    ACache aCache;

    public TxPresenter(TXDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
    }


    public void Submit() {
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        HttpParams params = new HttpParams();
//        if (info.getAuditAnchor() == 1 && StringUtils.isNotEmpty(follow.getAnId())) {
//            params.put("anId", follow.getAnId());
//        }
        if (null != follow && StringUtils.isNotEmpty(follow.getAnId())) {
            if (StringUtils.isEmpty(delegate.getzh())) {
                ToastUtil.showToast("请填写您的账号", delegate.getActivity());
                return;
            }
            if (delegate.getCount() <= 0) {
                ToastUtil.showToast("请输入提现金额", delegate.getActivity());
                return;
            }
            if (delegate.getCount() > follow.getAnGoldAble()) {
                ToastUtil.showToast("您输入的金额已经超出，请重新输入", delegate.getActivity());
                return;
            }
            String value = delegate.getSpinnerValue() + ":" + delegate.getzh();
            params.put("anId", follow.getAnId());
            params.put("exchargeType", value);
            params.put("amount", delegate.getCount());
            PostRequest request = OkGo.post(Config.COMMON_TX).params(params);
            HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
                }

                @Override
                public void onNext(Response response) {
                    String Str = null;
                    try {
                        Str = response.body().string();
                        JSONObject ob = JSON.parseObject(Str);
                        if (ob.containsKey("code")) {
                            int code = ob.getInteger("code");
                            if (code == 1) {
                                ToastUtil.showToast("订单已提交，请耐心等候", delegate.getActivity());
                                delegate.getActivity().finish();
                            } else {
                                ToastUtil.showToast(ob.getString("msg"), delegate.getActivity());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }
}
