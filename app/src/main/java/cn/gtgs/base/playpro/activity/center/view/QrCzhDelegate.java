package cn.gtgs.base.playpro.activity.center.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.model.Gold;
import cn.gtgs.base.playpro.activity.center.model.PicInfo;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.http.Config;

/**
 * Created by  on 2017/4/25.
 */

public class QrCzhDelegate extends AppDelegate {

    @BindView(R.id.img_qr_czh_qr)
    ImageView mImgQr;
    @BindView(R.id.img_qr_czh_qr2)
    ImageView mImgQr2;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_qr_czh;
    }

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    private ArrayList<Gold> mData = new ArrayList<>();

    public void init() {
        setmTvTitle("加好友充值");

    }

    public void setmImgQr(ArrayList<PicInfo> pics) {
        if (pics.size() > 0) {
            Glide.with(this.getActivity()).load(Config.BASE + pics.get(0).getAdImage()).into(mImgQr);
        }
        if (pics.size() > 1) {

            Glide.with(this.getActivity()).load(Config.BASE + pics.get(1).getAdImage()).into(mImgQr2);
        }
    }

    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

}
