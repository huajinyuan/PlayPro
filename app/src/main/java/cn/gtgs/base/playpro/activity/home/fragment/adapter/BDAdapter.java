package cn.gtgs.base.playpro.activity.home.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.BDInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.GlideCircleTransform;

/**
 * Created by  on 2017/5/24.
 */

public class BDAdapter extends BaseAdapter {

    ArrayList<BDInfo> data;
    Context context;
    LayoutInflater inflater;

    public BDAdapter(Context context, ArrayList<BDInfo> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
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
        BDInfo bdInfo = data.get(position);
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.layout_bd_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.img_center_num.setVisibility(View.VISIBLE);
            holder.tvPos.setVisibility(View.GONE);
            holder.img_center_num.setImageResource(R.mipmap.icon_medal1);
        } else if (position == 1) {
            holder.img_center_num.setVisibility(View.VISIBLE);
            holder.tvPos.setVisibility(View.GONE);
            holder.img_center_num.setImageResource(R.mipmap.icon_medal2);
        } else if (position == 2) {
            holder.img_center_num.setVisibility(View.VISIBLE);
            holder.tvPos.setVisibility(View.GONE);
            holder.img_center_num.setImageResource(R.mipmap.icon_medal3);
        } else {
            holder.img_center_num.setVisibility(View.GONE);
            holder.tvPos.setVisibility(View.VISIBLE);
            holder.tvPos.setText("NO." + (position + 1));
        }
        Glide.with(context).load(Config.BASE + bdInfo.getMbPhoto()).transform(new GlideCircleTransform(context)).into(holder.img_tyrants_icon);
        holder.tvName.setText(bdInfo.getMbNickname());
        holder.tv_tyrants_sum.setText(bdInfo.getTotalGold());


        return convertView;
    }

    class ViewHolder {
        TextView tvPos;
        ImageView img_center_num;
        TextView tvName;
        TextView tv_tyrants_sum;
        ImageView img_tyrants_icon;


        public ViewHolder(View itemView) {
            this.tvPos = (TextView) itemView.findViewById(R.id.tv_tyrants_count);
            this.img_center_num = (ImageView) itemView.findViewById(R.id.img_center_num);
            this.img_tyrants_icon = (ImageView) itemView.findViewById(R.id.img_tyrants_icon);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_tyrants_name);
            this.tv_tyrants_sum = (TextView) itemView.findViewById(R.id.tv_tyrants_sum);

        }
    }
}