package cn.gtgs.base.playpro.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.model.Gift;


public class AllGiftViewpager extends ViewPager implements OnItemClickListener {
    private Context context;
    private List<Gift> giftList = new ArrayList<Gift>();
    public int pagecount;
    private List<View> viewListpager = new ArrayList<View>();
    public View mViewSelect = null;
    public Gift mSelectGift;

    public AllGiftViewpager(Context context, AttributeSet attrs,
                            List<Gift> giftList, OnPageChangeListener listener) {
        super(context, attrs);
        this.context = context;
        this.giftList = giftList;
        initData(listener);
    }

    private void initData(OnPageChangeListener listener) {
        viewListpager.clear();
        pagecount = (giftList.size() - 1) / 10 + 1;
        for (int i = 0; i < pagecount; i++) {
            List<Gift> everypagerList = new ArrayList<Gift>();
            GridView mGridview = (GridView) LayoutInflater.from(context)
                    .inflate(R.layout.giftgive_gridview, null);
            if (giftList.size() < (i + 1) * 10) {
                everypagerList.addAll(giftList.subList(i * 10, giftList.size()));
            } else {
                everypagerList.addAll(giftList.subList(i * 10, (i + 1) * 10));

            }
            GiftGiveGridViewAdapter mAdapter = new GiftGiveGridViewAdapter(
                    everypagerList);
            mGridview.setAdapter(mAdapter);
            mGridview.setOnItemClickListener(this);
            mGridview.setId(i);
            viewListpager.add(mGridview);
        }
        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(viewListpager);
        setAdapter(viewPaperAdapter);
        setOnPageChangeListener(listener);
    }


    public AllGiftViewpager(Context context, List<Gift> giftList,
                            OnPageChangeListener listener) {
        super(context);
        this.context = context;
        this.giftList = giftList;
        initData(listener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (null != mViewSelect) {
            mViewSelect.setBackgroundResource(R.color.float_transparent);
        }
        mViewSelect = view;
        view.setBackgroundResource(R.mipmap.checkyes);
        mSelectGift = holder.newGiftBean;
    }

    class GiftGiveGridViewAdapter extends BaseAdapter {
        private List<Gift> viewList = new ArrayList<Gift>();

        public GiftGiveGridViewAdapter(List<Gift> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(
                        R.layout.giftgive_gridview_item, null);
                holder.newGiftBean = viewList.get(position);
                holder.img = (ImageView) view.findViewById(R.id.giftimg);
                holder.name = (TextView) view.findViewById(R.id.giftname);
                holder.values = (TextView) view.findViewById(R.id.values);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            Gift newGiftBean = viewList.get(position);
            Glide.with(context).load(newGiftBean.picture).into(holder.img);
//            holder.name.setText(newGiftBean.name);
//            holder.values.setText(newGiftBean.credits);
            return view;
        }

    }

    class ViewHolder {
        Gift newGiftBean;
        ImageView img;
        TextView name;
        TextView values;
    }

    public Gift getmSelectGift() {
        if (null != mSelectGift) {
            return mSelectGift;
        }
        return null;
    }
}
