package cn.gtgs.base.playpro.activity.center.presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.io.IOException;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.activity.center.view.EdtInfoDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.LoginActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 17/5/11.
 */

public class EdtInfoPresenter {
    EdtInfoDelegate delegate;
    AlertDialog mydialog;
    ACache aCache;
    Follow follow;
    UserInfo info;

    public EdtInfoPresenter(EdtInfoDelegate delegate) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
    }

    public void edtInfo(final String UrlPath) {
        if (StringUtils.isEmpty(UrlPath) && StringUtils.isEmpty(delegate.getEdtName())) {
            ToastUtil.showToast("昵称不能为空", delegate.getActivity());
            return;
        }
        if (delegate.getEdtName().equals(info.getMbNickname()) && StringUtils.isEmpty(UrlPath)) {
            ToastUtil.showToast("您没有修改任何信息", delegate.getActivity());
            return;
        }
        HttpParams params = new HttpParams();
        if (StringUtils.isNotEmpty(UrlPath)) {
            params.put("mbPhoto", UrlPath);
        }
        if (StringUtils.isNotEmpty(delegate.getEdtName())) {
            params.put("mbNickname", delegate.getEdtName());

        }
        params.put("mbId", info.getMbId());

        PostRequest request = OkGo.post(Config.POST_MEMBER_EDIT).params(params);
        HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络",delegate.getActivity());
            }

            @Override
            public void onNext(Response response) {

                try {
                    String Str = response.body().string();
                    F.e("-----------------" + Str);
                    JSONObject ob = JSON.parseObject(Str);
                    if (ob.containsKey("code")) {
                        int code = ob.getInteger("code");
                        if (code == 1) {
                            ToastUtil.showToast("修改成功", delegate.getActivity());
                            delegate.getActivity().finish();
                            if (StringUtils.isNotEmpty(UrlPath)) {
                                info.setMbPhoto(UrlPath);
                            }
                            if (StringUtils.isNotEmpty(delegate.getEdtName())) {
                                info.setMbNickname(delegate.getEdtName());
                            }
                            follow.setMember(info);
                            aCache.put(ACacheKey.CURRENT_ACCOUNT, follow);
                        }
                        else  if (code==0){
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
                        else {
                            if (ob.containsKey("msg")) {
                                ToastUtil.showToast(ob.getString("msg"), delegate.getActivity());
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
