package cn.gtgs.base.playpro.activity.login.view;

import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by gtgs on 2017/1/13.
 */

public class LoginDelegate extends AppDelegate {
    @BindView(R.id.et_phone)
    EditText mEdtPhone;
    @BindView(R.id.et_code)
    EditText mEdtCode;
    @BindView(R.id.tv_login_sms)
    TextView mTvSms;
    @BindView(R.id.bt_login)
    TextView mBtnLogin;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_login;
    }

    public TextView getSmsView() {
        return mTvSms;
    }

    public String getPhone() {
        return mEdtPhone.getText().toString();
    }

    public String getCode() {
        return mEdtCode.getText().toString();
    }

}
