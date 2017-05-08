package cn.gtgs.base.playpro.activity.center.presenter;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import cn.gtgs.base.playpro.activity.center.view.AnchorApproveDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
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
 * Created by hjy on 17/5/6.
 */

public class ApprovePresenter implements IApprove
{
    AnchorApproveDelegate delegate;
    UserInfo info ;
    ACache aCache;

    public ApprovePresenter(AnchorApproveDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    @Override
    public void Submit() {
        HttpParams params = new HttpParams();
        if (null != info)
        {
            params.put("mbId",info.getMbId());
            params.put("anQq",delegate.getQQ());
//            params.put("anSex","");
//            params.put("anPhoto","");
            PostRequest request = OkGo.post(Config.POST_ANCHOR_ADD).params(params);
            HttpMethods.getInstance().doPost(request,false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {
                    HttpBase<Follow> mbs = Parsing.getInstance().ResponseToObject(response,Follow.class);
                    if (mbs.code==1)
                    {
                        ToastUtil.showToast("申请以提交",delegate.getActivity());
                    }
                }
            });
        }



    }
}
