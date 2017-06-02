package cn.gtgs.base.playpro.activity.center.presenter;

import com.gt.okgo.OkGo;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.center.model.PicInfo;
import cn.gtgs.base.playpro.activity.center.view.QrCzhDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.BaseList;
import cn.gtgs.base.playpro.http.Config;
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

public class QrCzhPresenter {
    QrCzhDelegate delegate;
    UserInfo info;
    ACache aCache;
    public String path;

    public QrCzhPresenter(QrCzhDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    public void getPic() {
        PostRequest request = OkGo.post(Config.COMMON_RECHARGEPIC);
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
                BaseList<PicInfo> bs = Parsing.getInstance().ResponseToList3(response, PicInfo.class);
                ArrayList<PicInfo> pics = (ArrayList<PicInfo>) bs.getDataList();
                delegate.setmImgQr(pics);


            }
        });


    }
}
