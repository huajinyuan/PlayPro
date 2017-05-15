package cn.gtgs.base.playpro.activity.center.view;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
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

/**
 * Created by  on 2017/4/25.
 */

public class EdtInfoDelegate extends AppDelegate {

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    @BindView(R.id.swp_center)
    SwipeRefreshLayout mSwp;
    @BindView(R.id.img_edt_info_icon)
    ImageView mIcon;
    @BindView(R.id.edt_edt_info_name)
    EditText mEdtName;
    @BindView(R.id.tv_topbar_right)
    TextView mTvRight;
    UserInfo info;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_edt_info;
    }

    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

    public ImageView getmIcon()
    {
        return mIcon;
    }
    public void init() {
        Follow follow = (Follow) ACache.get(this.getActivity()).getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = follow.getMember();
        Glide.with(this.getActivity()).load(null != info.getMbPhoto() ? Config.BASE + info.getMbPhoto() : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(mIcon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mIcon.setImageDrawable(circularBitmapDrawable);
            }
        });
        mEdtName.setText(info.getMbNickname());
        setmTvTitle("个人编辑");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("完成");

    }

    public String getEdtName()
    {
        return mEdtName.getText().toString().trim();
    }
    public SwipeRefreshLayout getmSwp() {
        return mSwp;
    }
}
