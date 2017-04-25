package cn.gtgs.base.playpro.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gtgs.base.OTO.R;

/**
 * Created by gtgs on 2017/3/8.
 */

public class OrderLineView extends LinearLayout {
    TextView mTvLab1;
    TextView mTvLab2;
    TextView mTvValue1;
    TextView mTvValue2;

    public OrderLineView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_order_item_content_line, this);
        mTvLab1 = (TextView) findViewById(R.id.tv_order_item_content_line_lab1);
        mTvLab2 = (TextView) findViewById(R.id.tv_order_item_content_line_lab2);
        mTvValue1 = (TextView) findViewById(R.id.tv_order_item_content_line_value1);
        mTvValue2 = (TextView) findViewById(R.id.tv_order_item_content_line_value2);

    }


    public void init(String lab1, String value1, String lab2, String value2) {
        if (null == lab1) {
            mTvLab1.setVisibility(GONE);
            mTvValue1.setVisibility(GONE);
        }
        if (null == lab2) {
            mTvLab2.setVisibility(GONE);
            mTvValue2.setVisibility(GONE);
        }
        if (null != lab1) {
            mTvLab1.setText(lab1);
        }
        if (null != value1) {
            mTvValue1.setText(value1);
        }
        if (null != lab2) {
            mTvLab2.setText(lab2);
        }
        if (null != value2) {
            mTvValue2.setText(value2);
        }
    }


}
