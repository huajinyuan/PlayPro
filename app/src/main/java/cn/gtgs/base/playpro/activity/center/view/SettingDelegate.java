package cn.gtgs.base.playpro.activity.center.view;

import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class SettingDelegate extends AppDelegate {
    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_setting;
    }
    public void setmTvTitle(String title)
    {
        mTvTitle.setText(title);
    }
}
