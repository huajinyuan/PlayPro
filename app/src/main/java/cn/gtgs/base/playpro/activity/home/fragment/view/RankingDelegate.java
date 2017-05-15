package cn.gtgs.base.playpro.activity.home.fragment.view;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class RankingDelegate extends AppDelegate {
    @BindView(R.id.btn_tyrants)
    TextView mBtnTyrants;
    @BindView(R.id.btn_star)
    TextView mBtnStar;


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_ranking;
    }
    private void clearStatus() {
        mBtnTyrants.setBackgroundResource(R.color.float_transparent);
        mBtnTyrants.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.gray_btn_bg_pressed_color));
        mBtnStar.setBackgroundResource(R.color.float_transparent);
        mBtnStar.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.gray_btn_bg_pressed_color));
    }

    public void setStatus(int i) {
        clearStatus();
        switch (i) {
            case 0:
                mBtnTyrants.setBackgroundResource(R.drawable.shape_orange_indicator_r);
                mBtnTyrants.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorBlue));
                break;
            case 1:
                mBtnStar.setBackgroundResource(R.drawable.shape_orange_indicator_r);
                mBtnStar.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorBlue));
                break;
        }
    }
}
