package cn.gtgs.base.playpro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.gtgs.base.OTO.R;

/**
 * Created by gtgs on 2017/3/17.
 */

public class OrderContentItem extends ViewGroup {

    public OrderContentItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.lin_order_lin, this);
    }

    public OrderContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.lin_order_lin, this);
    }

    public OrderContentItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.lin_order_lin, this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void initViews(JSONObject obs) {
        JSONObject p = obs.getJSONObject("product");
        ArrayList<String> lab = (ArrayList<String>) JSON.parseArray(obs.getJSONArray("property").toJSONString(), String.class);
        if (lab.contains("name") && p.containsKey("name")) {
//            ((TextView) viewHolder.getView(R.id.tv_order_item_name)).setText(p.getString("name"));
            int maxSize = lab.size(); //5
            for (int i = 0; i < maxSize / 2; i++) {
                OrderLineView lineView;
                if (2 * (i + 1) < maxSize) {
                    lineView = new OrderLineView(this.getContext());
                    addView(lineView);
                    lineView.init(lab.get(2 * i + 1), p.getString(lab.get(2 * i + 1)), lab.get(2 * (i + 1)), p.getString(lab.get(2 * (i + 1))));
                } else if (2 * i + 1 < maxSize) {
                    lineView = new OrderLineView(this.getContext());
                    lineView.init(lab.get(2 * i + 1), p.getString(lab.get(2 * i + 1)), null, null);
                    addView(lineView);
                }
            }
        } else {
            for (int i = 0; i < lab.size() / 2; i++) {
                OrderLineView lineView = new OrderLineView(this.getContext());
                if (i * 2 + 1 <= lab.size()) {
                    if (p.containsKey(lab.get(2 * i)) && p.containsKey(lab.get(2 * i + 1))) {
                        lineView.init(lab.get(2 * i), p.getString(lab.get(2 * i)), lab.get(2 * i + 1), p.getString(lab.get(2 * i + 1)));
                    }
                } else {
                    if (p.containsKey(lab.get(2 * i))) {
                        lineView.init(lab.get(2 * i), p.getString(lab.get(2 * i)), null, null);
                    }
                }

            }
        }
//        String ImgPath = obs.getString("image");
//        Picasso.with(this.getContext()).load(ImgPath).into(((ImageView) viewHolder.getView(R.id.item_Iv)));

    }
}
