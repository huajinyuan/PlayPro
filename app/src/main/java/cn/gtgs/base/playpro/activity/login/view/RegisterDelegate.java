package cn.gtgs.base.playpro.activity.login.view;

import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by gtgs on 2017/1/13.
 */

public class RegisterDelegate extends AppDelegate {
    @BindView(R.id.et_phone)
    EditText mEdtPhone;
    @BindView(R.id.et_code)
    EditText mEdtCode;
    @BindView(R.id.et_pwd)
    EditText mEdtPwd;
    @BindView(R.id.tv_login_sms)
    TextView mTvSms;
    @BindView(R.id.tv_topbar_title)
    TextView mTvtitle;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_register;
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

    public String getPwd() {
        return mEdtPwd.getText().toString();
    }

    public void setTitle() {
        mTvtitle.setText("注册");
    }
}
