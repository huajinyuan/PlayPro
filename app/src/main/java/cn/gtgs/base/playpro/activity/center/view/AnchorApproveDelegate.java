package cn.gtgs.base.playpro.activity.center.view;

import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by gtgs on 2017/4/25.
 */

public class AnchorApproveDelegate extends AppDelegate {
    @BindView(R.id.edt_approve_qq)
    public EditText mEdtQQ;
    @BindView(R.id.tv_approve_sex)
    public TextView mTvSex;
    @BindView(R.id.tv_approve_isVideo)
    public TextView mTvVideoStatus;
    @BindView(R.id.edt_approve_introduce)
    public EditText mEdtIntroduce;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_anchor_approve;
    }
    public String getQQ()
    {
        return mEdtQQ.getText().toString().trim();
    }
    public String getSex()
    {
        return mTvSex.getText().toString().trim();
    }
    public String getStatus()
    {
        return mTvVideoStatus.getText().toString().trim();
    }
    public String getIntroduce()
    {
        return mEdtIntroduce.getText().toString().trim();
    }
}
