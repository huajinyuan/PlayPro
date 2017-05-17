package cn.gtgs.base.playpro.activity.login.presenter;//package cn.gtgs.base.playpro.activity.login.presenter;//package cn.gtgs.base.playpro.activity.login.presenter;

import android.widget.Toast;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.Timer;
import java.util.TimerTask;

import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;
import cn.gtgs.base.playpro.activity.login.view.RegisterDelegate;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.http.SimpleResponse;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 2017/2/10.
 */

public class RegisterPresenter implements IRegisterPresenter {
    RegisterDelegate delegate;
    IRegisterListener listener;
    int secondcount = 60;

    public RegisterPresenter(RegisterDelegate delegate, IRegisterListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public void register(RegisterInfo registerInfo) {
        String code = delegate.getCode();
        String pwd = delegate.getPwd();
        String phone = delegate.getPhone();
        if (null == phone || phone.length() < 1) {
            ToastUtil.showToast("请输入正确的手机号", delegate.getActivity());
            return;
        }
        if (null == pwd || pwd.length() < 1) {
            ToastUtil.showToast("请输入密码", delegate.getActivity());
            return;
        }
        if (null == code || code.length() < 1) {
            ToastUtil.showToast("输入正确的验证码", delegate.getActivity());
            return;
        }
        delegate.getmBtnRegister().setClickable(false);
        HttpParams params = HttpMethods.getInstance().getHttpParams();
        params.put("mbPhone", phone);
        params.put("smsAuthCode", code);
        params.put("mbPwd", MD5Util.getMD5(pwd));

        if (null != registerInfo) {
            params.put("mbPhoto", registerInfo.getAvatar_path());
            params.put("mbSex", registerInfo.gender.equals("f") ? 0 : 1);
            params.put("mbNickname", registerInfo.getName());

        }
        final PostRequest request = OkGo.post(Config.POST_REG).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                delegate.getmBtnRegister().setClickable(true);
                ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
            }

            @Override
            public void onNext(Response response) {
                delegate.getmBtnRegister().setClickable(true);
                HttpBase<Follow> u = Parsing.getInstance().ResponseToObject(response, Follow.class);
                if (u.getCode() == 1) {
                    if (null != listener) {
                        listener.RegisterSuccess(u.getData());
                    }
                } else {
                    if (null != listener) {
                        listener.RegisterFailed(u.msg);
                    }
                }


            }
        });


    }

    @Override
    public void getCode() {
        String phone = delegate.getPhone();
        if (phone.equals(""))
            Toast.makeText(delegate.getActivity(), "输入您的手机号", Toast.LENGTH_SHORT).show();
        else {
            delegate.getSmsView().setClickable(false);
            delegate.getSmsView().setClickable(false);
            HttpParams params = new HttpParams();
//            HttpParams params = HttpMethods.getInstance().getHttpParams();
            params.put("mbPhone", phone);
            GetRequest request = OkGo.get(Config.GET_CHECKCODE).params(params);
            HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    delegate.getSmsView().setClickable(true);
                    ToastUtil.showToast("请求失败，请检查网络", delegate.getActivity());
                }

                @Override
                public void onNext(Response response) {
                    delegate.getSmsView().setClickable(true);
                    SimpleResponse base = Parsing.getInstance().ResponseToSimPle(response, SimpleResponse.class);
                    if (base.code == 1) {
                        timecount();
                    } else {
                        Toast.makeText(delegate.getActivity(), base.msg, Toast.LENGTH_SHORT).show();
                        delegate.getSmsView().setClickable(true);
                    }
                }
            });
        }
    }

    public void timecount() {
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                delegate.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        secondcount--;
                        delegate.getSmsView().setText("已发送(" + secondcount + "S)");
                        if (secondcount == 0) {
                            secondcount = 60;
                            delegate.getSmsView().setText("获取验证码");
                            delegate.getSmsView().setClickable(true);
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }
}
