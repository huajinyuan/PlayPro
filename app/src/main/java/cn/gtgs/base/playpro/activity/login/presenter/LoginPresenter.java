package cn.gtgs.base.playpro.activity.login.presenter;


import android.widget.Toast;

import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;
import com.gt.okgo.request.PostRequest;

import java.util.Timer;
import java.util.TimerTask;

import cn.gtgs.base.playpro.activity.login.view.LoginDelegate;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.http.SimpleResponse;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class LoginPresenter implements ILoginPresenter {
    LoginDelegate delegate;
    ILoginListener listener;
    int secondcount = 60;

    public LoginPresenter(LoginDelegate delegate, ILoginListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public void login() {
        String code = delegate.getCode();

//        if (sms_hash == null)
//            Toast.makeText(delegate.getActivity(), "请先获取验证码", Toast.LENGTH_SHORT).show();
//        else
        if (code.equals(""))
            Toast.makeText(delegate.getActivity(), "输入您的验证码", Toast.LENGTH_SHORT).show();
        else {
            HttpParams params = HttpMethods.getInstance().getHttpParams();
            params.put("mbPhone", delegate.getPhone());
            params.put("smsAuthCode", code);
            final PostRequest request = OkGo.post(Config.POST_LOGIN).params(params);
            HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {
//                    if (response.code() == 200) {
//                        HttpBase<UserInfo> u = Parsing.getInstance().ResponseToObject(response, UserInfo.class);
//                        if (null != listener) {
//                            listener.LoginSuccess(u.data);
//                        }
//                    }
                            listener.LoginSuccess(null);


                }
            });

        }

    }

    @Override
    public void getCode() {
        String phone = delegate.getPhone();
        if (phone.equals(""))
            Toast.makeText(delegate.getActivity(), "输入您的手机号", Toast.LENGTH_SHORT).show();
        else {
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

                }

                @Override
                public void onNext(Response response) {

                    SimpleResponse base = Parsing.getInstance().ResponseToSimPle(response, SimpleResponse.class);
                    if (base.code == 1) {
                        timecount();
                    } else {
                        Toast.makeText(delegate.getActivity(), base.msg, Toast.LENGTH_SHORT).show();
                        delegate.getSmsView().setClickable(true);
                    }


//                        F.e(response.body().toString());
//                    SMSHash smsHash = JSON.parseObject(response.body().toString(), SMSHash.class);
//                    if (smsHash.error != null) {
//                        Toast.makeText(delegate.getActivity(), "请稍后重试", Toast.LENGTH_SHORT).show();
//                        delegate.getSmsView().setClickable(true);
//                    } else {
//                        sms_hash = smsHash.sms_hash;
//                        Log.e("dadw", sms_hash);
//                        timecount();
//                    }
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
