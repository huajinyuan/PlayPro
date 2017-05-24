package cn.gtgs.base.playpro.activity.center.view;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class TXDelegate extends AppDelegate {
    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    @BindView(R.id.spinner_tx)
    Spinner mSpin;
    @BindView(R.id.edt_tx_zh)
    EditText medtzh;
    @BindView(R.id.edt_tx_count)
    EditText medtcount;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_tx;
    }

    public void setmTvTitle() {
        mTvTitle.setText("提现");
    }

    public String getzh() {
        return medtzh.getText().toString();
    }

    public int getCount() {
        try {
            return Integer.valueOf(medtcount.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSpinnerValue() {
        return (String) mSpin.getSelectedItem();
    }
}
