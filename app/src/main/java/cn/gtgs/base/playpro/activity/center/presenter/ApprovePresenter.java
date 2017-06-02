package cn.gtgs.base.playpro.activity.center.presenter;

import android.content.Intent;
import android.os.Handler;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.activity.center.view.AnchorApproveDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
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

public class ApprovePresenter implements IApprove {
    AnchorApproveDelegate delegate;
    UserInfo info;
    ACache aCache;
    public String path;

    public ApprovePresenter(AnchorApproveDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void Submit() {
        Follow follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
        HttpParams params = new HttpParams();
        if (info.getAuditAnchor() == 1 && StringUtils.isNotEmpty(follow.getAnId())) {
            params.put("anId", follow.getAnId());
        }
        if (null != info) {
            if (null == path) {
                ToastUtil.showToast("请设置头像", delegate.getActivity());
                return;
            }
            if (StringUtils.isEmpty(delegate.getQQ())) {
                ToastUtil.showToast("请填写QQ号", delegate.getActivity());
                return;
            }
            if (StringUtils.isEmpty(delegate.getWx())) {
                ToastUtil.showToast("请填写wx号", delegate.getActivity());
                return;
            }
            if (StringUtils.isNotEmpty(delegate.getIntroduce())) {
                params.put("anRemark", delegate.getIntroduce());
            }

            params.put("mbId", info.getMbId());
            params.put("anQq", delegate.getQQ());
            params.put("anSex", delegate.getSex());
            params.put("anWeixin", delegate.getWx());
            params.put("anQq", delegate.getVideoStatus());
//            params.put("anSex","");
            params.put("anPhoto", path);
            PostRequest request = OkGo.post(Config.POST_ANCHOR_ADD).params(params);
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
                    HttpBase<Follow> mbs = Parsing.getInstance().ResponseToObject(response, Follow.class);
                    if (mbs.code == 1) {
                        ToastUtil.showToast("申请已提交", delegate.getActivity());
                        delegate.getActivity().finish();
                    } else if (mbs.code == 0) {
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
                    } else {
                        if (StringUtils.isNotEmpty(mbs.getMsg())) {
                            ToastUtil.showToast(mbs.getMsg(), delegate.getActivity());

                        }
                    }
                }
            });
        }


    }
}
