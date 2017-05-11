package cn.gtgs.base.playpro.activity.home.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.PlayActivity;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.PixelUtil;

/**
 * Created by gtgs on 2016/9/2.
 */
public class RecommentedAdapter extends RecyclerView.Adapter<RecommentedAdapter.AnchorHotViewHolder> {
    private ArrayList<Follow> mData;
    private Context mContext;
    private int mWith = 0;

    public RecommentedAdapter(ArrayList<Follow> data, Context context) {
        this.mData = data;
        this.mContext = context;
        this.mWith = PixelUtil.getWidth(mContext);
    }

    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommented_item_recycler,
                parent, false);
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.pic.getLayoutParams();
        params.height = mWith / 2;
        holder.pic.setLayoutParams(params);


        final Follow anchorItem = mData.get(position);
        Glide.with(mContext).load(null != anchorItem.getAnPhoto() ? Config.BASE + anchorItem.getAnPhoto() : R.drawable.circle_zhubo).into(holder.pic);
        holder.tvName.setText(anchorItem.getAnRemark());
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("anchoritem", mData.get(position));
                intent.putExtra("IsMember", false);
                mContext.startActivity(intent);
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
        ImageView pic;
        TextView tvName;
        TextView tvCount;

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.pic = (ImageView) itemView.findViewById(R.id.img_layout_hotanchor_item_pic);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_layout_hotanchor_item_name);
            this.tvCount = (TextView) itemView.findViewById(R.id.tv_layout_hotanchor_item_count);
        }
    }
}
