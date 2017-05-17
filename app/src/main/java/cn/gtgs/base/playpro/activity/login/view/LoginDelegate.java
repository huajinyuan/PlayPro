package cn.gtgs.base.playpro.activity.login.view;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/1/13.
 */

public class LoginDelegate extends AppDelegate {
    @BindView(R.id.et_phone)
    EditText mEdtPhone;
    @BindView(R.id.et_pwd)
    EditText mEdtPwd;
    @BindView(R.id.bt_login)
    Button mBtnLogin;
    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_login;
    }


    public String getPhone() {
        return mEdtPhone.getText().toString();
    }

    public String getPwd() {
        return mEdtPwd.getText().toString();
    }

    public void setTitle() {
        mTvTitle.setText("登录");
    }

    public Button getmBtnLogin() {
        return mBtnLogin;
    }
}
