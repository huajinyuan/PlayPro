package cn.gtgs.base.playpro.activity.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.model.CSInfo;

/**
 * Created by gtgs on 2017/5/16.
 */

public class Adapter extends BaseAdapter {
    ArrayList<CSInfo> datas;
    Context context;
    LayoutInflater inflater;

    public Adapter(Context context, ArrayList<CSInfo> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CSInfo cs = datas.get(position);
        TextView textView = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.layout_item_cs, null);
            textView = (TextView) convertView.findViewById(R.id.tv_item_cs);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        textView.setText(cs.getValue());
        return convertView;
    }

}