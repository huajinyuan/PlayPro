package cn.gtgs.base.playpro.activity.center.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.model.TxInfo;
import cn.gtgs.base.playpro.base.view.AppDelegate;

/**
 * Created by  on 2017/4/25.
 */

public class CZListDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_czlist;
    }

    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    @BindView(R.id.listview_cz_content)
    ListView mCZContent;
    @BindView(R.id.tv_czlist_ing)
    TextView mTvIng;
    @BindView(R.id.tv_czlist_end)
    TextView mTvEnd;
    private ArrayList<TxInfo> mData = new ArrayList<>();
    boolean isOff = false;
    CZListAdapter adapter;

    public void init() {
        setmTvTitle("提现记录");
        mTvIng.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorBlue));
        mTvEnd.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.color_black_0D));
    }

    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setData(boolean isoff, ArrayList<TxInfo> data) {
        isOff = isoff;
        if (!isoff) {
            mTvIng.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorBlue));
            mTvEnd.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.color_black_0D));
        } else {
            mTvIng.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.color_black_0D));
            mTvEnd.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorBlue));
        }
        mData.clear();
        mData.addAll(data);
        if (null == adapter) {
            adapter = new CZListAdapter(mData, this.getActivity(), isOff);
            mCZContent.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    class CZListAdapter extends BaseAdapter {
        ArrayList<TxInfo> data;
        Context context;
        LayoutInflater inflater;
        boolean isOff;

        public CZListAdapter(ArrayList<TxInfo> data, Context context, boolean isOff) {
            this.data = data;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.isOff = isOff;
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
            Holder holder = null;
            TxInfo txInfo = data.get(position);
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.layout_czlist_content_item, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tvValue.setText("+" + txInfo.getTradeAmount());
            if (isOff) {
                holder.tvStatus.setText("已处理");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(txInfo.getUpdateTime());
                holder.tvTime.setText(dateString);
            } else {
                holder.tvStatus.setText("待处理");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(txInfo.getCreateTime());
                holder.tvTime.setText(dateString);
            }

            return convertView;
        }
    }

    class Holder {
        TextView tvTime;
        TextView tvStatus;
        TextView tvValue;

        public Holder(View view) {
            this.tvStatus = (TextView) view.findViewById(R.id.tv_item_stuts);
            this.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
            this.tvValue = (TextView) view.findViewById(R.id.tv_item_value);
        }
    }

}
