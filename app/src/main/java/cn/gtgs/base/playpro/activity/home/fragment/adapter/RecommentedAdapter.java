package cn.gtgs.base.playpro.activity.home.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IRecommentedItemListener;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.AppUtil;
import cn.gtgs.base.playpro.utils.PixelUtil;
import cn.gtgs.base.playpro.utils.StringUtils;

/**
 * Created by  on 2016/9/2.
 */
public class RecommentedAdapter extends RecyclerView.Adapter<RecommentedAdapter.AnchorHotViewHolder> {
    private ArrayList<Follow> mData;
    private Context mContext;
    private int mWith = 0;
    private IRecommentedItemListener listener;

    public RecommentedAdapter(ArrayList<Follow> data, Context context) {
        this.mData = data;
        this.mContext = context;
        this.mWith = PixelUtil.getWidth(mContext);
    }

    public void setListener(IRecommentedItemListener listener) {
        this.listener = listener;
    }

    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommented_item_recycler,
                parent, false);
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {
//        if (mData.size()>0)
//        {
//            final Follow anchorItem = mData.get(position);
//            Glide.with(mContext).load(null != anchorItem.getAnPhoto() ? Config.BASE + anchorItem.getAnPhoto() : R.drawable.circle_zhubo).into(holder.pic);
//
//        }
        ViewGroup.LayoutParams params = holder.pic.getLayoutParams();
        params.height = mWith / 2;
        holder.pic.setLayoutParams(params);
        final Follow anchorItem = mData.get(position);
        Glide.with(mContext).load(null != anchorItem.getAnPhoto() ? Config.BASE + anchorItem.getAnPhoto() : R.drawable.circle_zhubo).into(holder.pic);

        holder.tvName.setText(anchorItem.getMember().getMbNickname());
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.itemCliclk(anchorItem);
                }

            }
        });
        holder.tvStatus.setText("暂无直播");
        holder.tvStatus.setBackgroundResource(R.drawable.timestampe_bg);
        if (anchorItem.getLiveStatus().equals("2")) {
            holder.tvStatus.setText("直播");
            holder.tvStatus.setBackgroundResource(R.drawable.timestampe_bg);
        } else if (anchorItem.getLiveStatus().equals("3")) {
            holder.tvStatus.setText("开车");
            holder.tvStatus.setBackgroundResource(R.drawable.background_green);
        }
if (StringUtils.isNotEmpty(anchorItem.getAnGold())){
 int level = AppUtil.getDJ(Integer.valueOf(anchorItem.getAnGold()));
 if (level<5){
     holder.tvName.setBackgroundResource(R.drawable.shape_rec_1);
 }else if (level<10){
     holder.tvName.setBackgroundResource(R.drawable.shape_rec_2);
 }else if (level<15){
     holder.tvName.setBackgroundResource(R.drawable.shape_rec_3);
 }else if (level<20){
     holder.tvName.setBackgroundResource(R.drawable.shape_rec_4);
 }else {
     holder.tvName.setBackgroundResource(R.drawable.shape_rec_5);
 }
}






    }

    @Override
    public int getItemCount() {
        return mData.size();
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class AnchorHotViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView tvName;
        TextView tvCount;
        TextView tvStatus;

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.pic = (ImageView) itemView.findViewById(R.id.img_layout_hotanchor_item_pic);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_layout_hotanchor_item_name);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            this.tvCount = (TextView) itemView.findViewById(R.id.tv_layout_hotanchor_item_count);
        }
    }
}
