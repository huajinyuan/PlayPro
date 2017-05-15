package cn.gtgs.base.playpro.activity.home.view;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class HomeDelegate extends AppDelegate {
    @BindView(R.id.btn_ranking)
    TextView mBtnRanking;
    @BindView(R.id.btn_recommented)
    TextView mBtnRecommented;
    @BindView(R.id.btn_follow)
    TextView mBtnFollow;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_home;
    }

    private void clearStatus() {
        mBtnRanking.setBackgroundResource(R.color.float_transparent);
        mBtnRanking.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.gray_btn_bg_color));
        mBtnRecommented.setBackgroundResource(R.color.float_transparent);
        mBtnRecommented.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.gray_btn_bg_color));
        mBtnFollow.setBackgroundResource(R.color.float_transparent);
        mBtnFollow.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.gray_btn_bg_color));
    }

    public void setStatus(int i) {
        clearStatus();
        switch (i) {
            case 0:
                mBtnRanking.setBackgroundResource(R.drawable.shape_orange_indicator);
                mBtnRanking.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorwhite));
                break;
            case 1:
                mBtnRecommented.setBackgroundResource(R.drawable.shape_orange_indicator);
                mBtnRecommented.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorwhite));
                break;
            case 2:
                mBtnFollow.setBackgroundResource(R.drawable.shape_orange_indicator);
                mBtnFollow.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorwhite));
                break;
        }
    }
}
