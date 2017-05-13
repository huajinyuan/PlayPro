package cn.gtgs.base.playpro.activity.center.view;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.AppUtil;

/**
 * Created by gtgs on 2017/4/25.
 */

public class CenterDelegate extends AppDelegate {

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    @BindView(R.id.img_center_icon)
    ImageView mIcon;
    @BindView(R.id.tv_center_name)
    TextView mTvName;
    @BindView(R.id.img_center_sex)
    ImageView mImgSex;
    @BindView(R.id.img_topbar_right)
    ImageView mImgRight;
    @BindView(R.id.tv_center_id)
    TextView mTvId;
    @BindView(R.id.tv_center_gole)
    TextView mGole;
    @BindView(R.id.tv_center_level)
    TextView tvLe;
    @BindView(R.id.tv_center_g)
    TextView tvg;
    @BindView(R.id.swp_center)
    SwipeRefreshLayout mSwp;
    @BindView(R.id.tv_center_isauditAnchor)
    TextView mTvisanditAnchor;
    UserInfo info;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_center;
    }

    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

    public void init() {
        Follow follow = (Follow) ACache.get(this.getActivity()).getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
        mTvName.setText(null != info.getMbNickname() ? info.getMbNickname() : info.getMbPhone());
        mTvId.setText(info.getMbId() + "");
        mImgRight.setVisibility(View.VISIBLE);
        Glide.with(this.getActivity()).load(null != info.getMbPhoto() ? Config.BASE + info.getMbPhoto() : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(mIcon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mIcon.setImageDrawable(circularBitmapDrawable);
            }
        });
        tvLe.setText(AppUtil.getDJ(info.getMbGold()) + "");
        tvg.setText(AppUtil.getDJ(info.getMbGoldPay()) + "");
        mGole.setText("今日收益" + follow.getDayGold() + "钻石");
        mImgSex.setImageResource(info.getMbSex() == 1 ? R.mipmap.global_male : R.mipmap.global_female);
        if (info.getAuditAnchor()==1)
        {
            mTvisanditAnchor.setText("已认证");
        }else
        {
            mTvisanditAnchor.setText("未认证");
        }
    }

    public SwipeRefreshLayout getmSwp() {
        return mSwp;
    }
}
