package cn.gtgs.base.playpro.activity.login.presenter;


import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.util.Timer;
import java.util.TimerTask;

import cn.gtgs.base.playpro.activity.login.model.SMSHash;
import cn.gtgs.base.playpro.activity.login.view.LoginDelegate;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class LoginPresenter implements ILoginPresenter {
    LoginDelegate delegate;
    ILoginListener listene;
    String sms_hash;
    int secondcount = 60;
    public LoginPresenter(LoginDelegate delegate, ILoginListener listene) {
        this.delegate = delegate;
        this.listene = listene;
    }

    @Override
    public void login() {
      String  code = delegate.getCode();

        if(sms_hash==null)
            Toast.makeText(delegate.getActivity(), "请先获取验证码", Toast.LENGTH_SHORT).show();
        else if(code.equals(""))
            Toast.makeText(delegate.getActivity(), "输入您的验证码", Toast.LENGTH_SHORT).show();
        else{
            HttpParams params = HttpMethods.getInstance().getHttpParams();
            params.put("type", "phone");
            params.put("id", delegate.getPhone());
            params.put("sms_hash", sms_hash);
            params.put("sms_code", code);
            PostRequest request = OkGo.post(Config.URL_LOGIN).params(params);



//            Request<String> request = NoHttp.createStringRequest(Config.URL_LOGIN, RequestMethod.POST);
//            request.add("type", "phone");
//            request.add("id", phone);
//            request.add("sms_hash", sms_hash);
//            request.add("sms_code", code);
//            requestQueue.add(1,request,responseListener);
        }
    }

    @Override
    public void getCode() {
        String phone = delegate.getPhone();
        if (phone.equals(""))
            Toast.makeText(delegate.getActivity(), "输入您的手机号", Toast.LENGTH_SHORT).show();
        else {
            delegate.getSmsView().setClickable(false);
            HttpParams params = HttpMethods.getInstance().getHttpParams();
            PostRequest request = OkGo.post(Config.URL_SMS).params(params);
            params.put("phone",phone);
            HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response response) {

//                        F.e(response.body().toString());
                    SMSHash smsHash = JSON.parseObject(response.body().toString(), SMSHash.class);
                    if (smsHash.error != null) {
                        Toast.makeText(delegate.getActivity(), "请稍后重试", Toast.LENGTH_SHORT).show();
                        delegate.getSmsView().setClickable(true);
                    } else {
                        sms_hash = smsHash.sms_hash;
                        Log.e("dadw", sms_hash);
                        timecount();
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
