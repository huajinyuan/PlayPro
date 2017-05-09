package cn.gtgs.base.playpro.activity.center.view;

import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by gtgs on 2017/4/25.
 */

public class GetCoinDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_get_coin;
    }

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    public void setmTvTitle(String title)
    {
        mTvTitle.setText(title);
    }
}