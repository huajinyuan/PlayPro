package cn.gtgs.base.playpro.widget;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;

/**
  * @ClassName: ChatEmoticoViewPager
  * @Description: 字符表情viewpager
  * @date 2015年8月13日 上午11:15:55
  *
  */
public class ChatEmoticoViewPager extends ViewPager implements OnItemClickListener {
	private Context context;
	private List<View> pagerListView = new ArrayList<View>();
	private OnEmoticoSelectedListener onEmoticoSelectedListener;
	public ChatEmoticoViewPager(Context context) {
		super(context);
		this.context = context;
		initData();
	}

	public ChatEmoticoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initData();
	}

	private void initData() {
		for (int i = 0; i < (PApplication.emoticonList.size() - 1) / 21 + 1; i++) {
			GridView gridView = (GridView) LayoutInflater.from(context).inflate(
					R.layout.emoticon_gridview, null);
			List<String> pagerEmoticoList = new ArrayList<String>();
			List<String> pagerEmoticoKeyList = new ArrayList<String>();
			if (PApplication.emoticonList.size() < (i + 1) * 21) {
				pagerEmoticoList.addAll(PApplication.emoticonList.subList(i * 21,
						PApplication.emoticonList.size()));
				pagerEmoticoKeyList.addAll(PApplication.emoticonKeyList.subList(
						i * 21, PApplication.emoticonKeyList.size()));
			} else {
				pagerEmoticoList.addAll(PApplication.emoticonList.subList(i * 21,
						(i + 1) * 21));
				pagerEmoticoKeyList.addAll(PApplication.emoticonKeyList.subList(
						i * 21, (i + 1) * 21));
			}

			EmoticoGridViewAdapter adapter = new EmoticoGridViewAdapter(pagerEmoticoList,
					pagerEmoticoKeyList);
			gridView.setAdapter(adapter);
			gridView.setId(i);
			gridView.setOnItemClickListener(this);
			pagerListView.add(gridView);
		}
		ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(pagerListView);
		setAdapter(viewPaperAdapter);
	}

	class EmoticoGridViewAdapter extends BaseAdapter {

		List<String> pagerEmoticoList = new ArrayList<String>();
		List<String> pagerEmoticoKeyList = new ArrayList<String>();

		public EmoticoGridViewAdapter(List<String> emoticoListm,
				List<String> emoticoKeyList) {
			pagerEmoticoList = emoticoListm;
			pagerEmoticoKeyList = emoticoKeyList;
		}

		@Override
		public int getCount() {
			return pagerEmoticoList.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup viewgroup) {

			ImageView image;
			if (view == null) {
				image = new ImageView(context);
			} else {
				image = (ImageView) view;
			}
			image.setImageResource(context.getResources().getIdentifier(
					"cn.gtgs.base.playpro:drawable/" + pagerEmoticoList.get(index), null,
					null));
			return image;
		}

	}
	
	public void setOnEmoticoSelectedListener(
			OnEmoticoSelectedListener onEmoticoSelectedListener) {
		this.onEmoticoSelectedListener = onEmoticoSelectedListener;
	}

	@Override
	public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
		@SuppressLint("ResourceType") String key = PApplication.emoticonKeyList.get(gridView.getId() * 21 +  position);
		if(onEmoticoSelectedListener!=null){
			onEmoticoSelectedListener.onEmoticoSelected(key);
		}
	}
}
