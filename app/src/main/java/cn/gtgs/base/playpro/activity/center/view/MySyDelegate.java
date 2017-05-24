package cn.gtgs.base.playpro.activity.center.view;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.model.Gold;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.StringUtils;

/**
 * Created by  on 2017/4/25.
 */

public class MySyDelegate extends AppDelegate {
    ACache aCache ;
Follow mF;
@BindView(R.id.tv_sy_able_value)
TextView tvAble;
@BindView(R.id.tv_sy_gold_get)
TextView tvGole;
@BindView(R.id.tv_topbar_right)
TextView tvRight;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_my_sy;
    }

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    private ArrayList<Gold> mData = new ArrayList<>();

    public void init()
    {
        aCache =ACache.get(this.getActivity());
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        setmTvTitle("我的收益");
        tvRight.setText("提现记录");
        tvRight.setVisibility(View.VISIBLE);
        if (mF.getAnGoldAble()>0)
        {
            tvAble.setText((int)mF.getAnGoldAble()+"");
        }
        if (StringUtils.isNotEmpty(mF.getAnGold()))
        {
            tvGole.setText(mF.getAnGold()+"");
        }

    }

    public void setmTvTitle(String title)
    {
        mTvTitle.setText(title);
    }

}
