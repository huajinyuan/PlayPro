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
import cn.gtgs.base.playpro.activity.home.fragment.presenter.IFollowItemListener;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.GlideCircleTransform;
import cn.gtgs.base.playpro.utils.PixelUtil;

/**
 * Created by gtgs on 2016/9/2.
 */
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.AnchorHotViewHolder> {
    private ArrayList<Follow> mData;
    private Context mContext;
    private int mWith = 0;
    public IFollowItemListener iFollowItemListener;
    public FollowAdapter(ArrayList<Follow> data, Context context) {
        this.mData = data;
        this.mContext = context;
        this.mWith = PixelUtil.getWidth(mContext);
    }
    public void setListener(IFollowItemListener listener)
    {
        this.iFollowItemListener = listener;
    }
    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tyrants_item_recycler,
                parent, false);
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {
        final Follow follow = mData.get(position);


        UserInfo anchorItem = follow.getMember();
        holder.tvPos.setVisibility(View.GONE);
        holder.img_center_num.setVisibility(View.GONE);
//        if (position == 0) {
//            holder.img_center_num.setVisibility(View.VISIBLE);
//            holder.tvPos.setVisibility(View.GONE);
//            holder.img_center_num.setImageResource(R.mipmap.icon_medal1);
////            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal1);
////            holder.tvPos.setText("");
//        } else if (position == 1) {
//            holder.img_center_num.setVisibility(View.VISIBLE);
//            holder.tvPos.setVisibility(View.GONE);
//            holder.img_center_num.setImageResource(R.mipmap.icon_medal2);
////            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal2);
////            holder.tvPos.setText("");
//        } else if (position == 2) {
//            holder.img_center_num.setVisibility(View.VISIBLE);
//            holder.tvPos.setVisibility(View.GONE);
//            holder.img_center_num.setImageResource(R.mipmap.icon_medal3);
////            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal3);
////            holder.tvPos.setText("");
//        } else {
//            holder.img_center_num.setVisibility(View.GONE);
//            holder.tvPos.setVisibility(View.VISIBLE);
////            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal_trans);
//            holder.tvPos.setText("NO." + (position + 1));
//        }
        if(null != anchorItem.getMbNickname())
        {
            holder.tvName.setText(anchorItem.getMbNickname());
        }
        else {
            holder.tvName.setText(anchorItem.getMbPhone());
        }
        holder.img_tyrants_sex.setImageResource(anchorItem.getMbSex()==0?R.mipmap.global_male:R.mipmap.global_female);
        if (null != anchorItem.getMbPhoto())
        {
            Glide.with(mContext).load(Config.BASE+anchorItem.getMbPhoto()).transform(new GlideCircleTransform(mContext)).into(holder.img_tyrants_icon);
        }

        holder.img_tyrants_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != iFollowItemListener)
                {
                    iFollowItemListener.ItemFolloClick(follow);
                }

            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != iFollowItemListener)
                {
                    iFollowItemListener.itemClick(follow);
                }

            }
        });

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
        TextView tvName;
        TextView tvPos;
        TextView tv_tyrants_sum;
        ImageView img_tyrants_icon;
        ImageView img_tyrants_sex;
        ImageView img_center_num;
        ImageView img_tyrants_follow;

        View item;

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.item = itemView;
            this.tvName = (TextView) itemView.findViewById(R.id.tv_tyrants_name);
            this.tvPos = (TextView) itemView.findViewById(R.id.tv_tyrants_count);
            this.tv_tyrants_sum = (TextView) itemView.findViewById(R.id.tv_tyrants_sum);
            this.img_tyrants_icon = (ImageView) itemView.findViewById(R.id.img_tyrants_icon);
            this.img_tyrants_sex = (ImageView) itemView.findViewById(R.id.img_tyrants_sex);
            this.img_center_num = (ImageView) itemView.findViewById(R.id.img_center_num);
            this.img_tyrants_follow = (ImageView) itemView.findViewById(R.id.img_tyrants_follow);
        }
    }
}
