package cn.gtgs.base.playpro.activity.center.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.model.Gold;
import cn.gtgs.base.playpro.utils.StringUtils;

/**
 * Created by  on 2016/9/2.
 */
public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.AnchorHotViewHolder> {
    private ArrayList<Gold> mData;
    private Context mContext;

    public CoinAdapter(ArrayList<Gold> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_getcoin,
                parent, false);
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {

        Gold gold = mData.get(position);
        holder.tv_gold.setText(gold.getGold());
        holder.tv_gold_value.setText(gold.getValue());
        if (StringUtils.isNotEmpty(gold.getGifts()))
        {
            holder.tv_gold_gifts.setText("再送"+gold.getGifts()+"钻");
        }
        else{
            holder.tv_gold_gifts.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class AnchorHotViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gold;
        TextView tv_gold_gifts;
        TextView tv_gold_value;

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.tv_gold = (TextView) itemView.findViewById(R.id.tv_gold);
            this.tv_gold_value = (TextView) itemView.findViewById(R.id.tv_gold_value);
            this.tv_gold_gifts = (TextView) itemView.findViewById(R.id.tv_gold_gifts);
        }
    }
}
