package cn.gtgs.base.playpro.activity.home.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.utils.PixelUtil;

/**
 * Created by gtgs on 2016/9/2.
 */
public class TyrantsAdapter extends RecyclerView.Adapter<TyrantsAdapter.AnchorHotViewHolder> {
    private ArrayList<UserInfo> mData;
    private Context mContext;
    private int mWith = 0;

    public TyrantsAdapter(ArrayList<UserInfo> data, Context context) {
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
        final UserInfo anchorItem = mData.get(position);
        if (position == 0) {
            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal1);
            holder.tvPos.setText("");
        } else if (position == 1) {
            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal2);
            holder.tvPos.setText("");
        } else if (position == 2) {
            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal3);
            holder.tvPos.setText("");
        } else {
            holder.tvPos.setBackgroundResource(R.mipmap.icon_medal_trans);
            holder.tvPos.setText("NO." + (position + 1));
        }
//        holder.tvName.setText(anchorItem.huanxin_username);
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
        TextView tvPos;

        public AnchorHotViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_tyrants_name);
            this.tvPos = (TextView) itemView.findViewById(R.id.tv_tyrants_count);
        }
    }
}
