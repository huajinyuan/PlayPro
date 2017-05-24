package cn.gtgs.base.playpro.activity.center.view;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class AnchorApproveDelegate extends AppDelegate {
    @BindView(R.id.edt_approve_qq)
    public EditText mEdtQQ;
    //    @BindView(R.id.tv_approve_sex)
//    public TextView mTvSex;
//    @BindView(R.id.tv_approve_isVideo)
//    public TextView mTvVideoStatus;
    @BindView(R.id.edt_approve_introduce)
    public EditText mEdtIntroduce;
    @BindView(R.id.edt_approve_wx)
    public EditText mEdtwx;
    @BindView(R.id.img_approve_icon)
    ImageView Icon;
    @BindView(R.id.rg_approve_sex)
    RadioGroup mRadioGroupSex;
    @BindView(R.id.rg_approve_video_app)
    RadioGroup mRadioGroupVideo;

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;

    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_anchor_approve;
    }

    public String getQQ() {
        return mEdtQQ.getText().toString().trim();
    }

//    public String getSex() {
//        return mTvSex.getText().toString().trim();
//    }

//    public String getStatus() {
//        return mTvVideoStatus.getText().toString().trim();
//    }

    public String getIntroduce() {
        return mEdtIntroduce.getText().toString().trim();
    }

    public ImageView getIcon() {
        return Icon;
    }


    public String getSex()
    {

        return mRadioGroupSex.getCheckedRadioButtonId() == R.id.rb_approve_f ? "0" : "1";
    }
    public String getVideoStatus()
    {
        return mRadioGroupVideo.getCheckedRadioButtonId()==R.id.rb_approve_y?"是":"否";
    }
    public String getWx()
    {
        return mEdtwx.getText().toString();
    }
}
