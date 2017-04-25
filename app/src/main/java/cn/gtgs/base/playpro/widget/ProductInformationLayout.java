package cn.gtgs.base.playpro.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gtgs.base.OTO.R;


public class ProductInformationLayout extends LinearLayout {

    TextView mTvLab;
    TextView mTvInformation;

    public ProductInformationLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.product_information_item, this);
        mTvLab = (TextView) findViewById(R.id.tv_product_information_lab);
        mTvInformation = (TextView) findViewById(R.id.tv_product_information_value);
    }

    public void init(String lab, String value) {
        mTvLab.setText(lab + ":");
        mTvInformation.setText(value);
    }
}