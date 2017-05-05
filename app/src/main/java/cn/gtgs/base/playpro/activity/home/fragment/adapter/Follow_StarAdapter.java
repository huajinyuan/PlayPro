package cn.gtgs.base.playpro.activity.home.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.utils.PixelUtil;

/**
 * Created by gtgs on 2016/9/2.
 */
public class Follow_StarAdapter extends RecyclerView.Adapter<Follow_StarAdapter.AnchorHotViewHolder> {
    private ArrayList<Follow> mData;
    private Context mContext;
    private int mWith = 0;

    public Follow_StarAdapter(ArrayList<Follow> data, Context context) {
        this.mData = data;
        this.mContext = context;
        this.mWith = PixelUtil.getWidth(mContext);
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
//        holder.tvName.setText(follow.huanxin_username);
//        Glide.with(mContext).load(anchorItem.avatar).into(holder.pic);
//        holder.tvName.setText(anchorItem.name);
//        holder.tvSiteName.setText(anchorItem.place);
//        holder.tvCount.setText(anchorItem.online_count);
//        ViewGroup.LayoutParams params = holder.pic.getLayoutParams();
//        params.height = mWith / 2;
//        holder.pic.setLayoutParams(params);
//        final String path = anchorItem.avatar;
//        if (anchorItem.live.equals("1")) {
//            Glide.with(mContext).load(anchorItem.stream.snapshot).listener(new RequestListener<String, GlideDrawable>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                    holder.pic.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(mContext).load(path).into(holder.pic);
//                        }
//                    });
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    return false;
//                }
//            }).into(holder.pic);
//        } else {
//            Glide.with(mContext).load(anchorItem.avatar).into(holder.pic);
//        }
//        holder.pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(mContext, LiveActivity.class);
////                intent.putExtra("anchoritem", mData.get(position));
////                mContext.startActivity(intent);
//            }
//        });

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

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_tyrants_name);
        }
    }
}
