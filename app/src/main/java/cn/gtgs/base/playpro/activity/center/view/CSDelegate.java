package cn.gtgs.base.playpro.activity.center.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.adapter.Adapter;
import cn.gtgs.base.playpro.activity.center.model.CSInfo;
import cn.gtgs.base.playpro.activity.center.model.Gold;
import cn.gtgs.base.playpro.base.view.AppDelegate;
import cn.gtgs.base.playpro.utils.ToastUtil;

/**
 * Created by  on 2017/4/25.
 */

public class CSDelegate extends AppDelegate implements AdapterView.OnItemClickListener {
    @BindView(R.id.listview_cs)
    ListView listview_cs;
    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;
    Adapter adapter;
    ArrayList<CSInfo> datas = new ArrayList<>();

    @Override
    public int getRootLayoutId() {

        return R.layout.activity_cs;
    }

    private ArrayList<Gold> mData = new ArrayList<>();

    public void init() {
        setmTvTitle("客服列表");
    }


    public void setmTvTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setData(ArrayList<CSInfo> mData) {
        datas.clear();
        datas.addAll(mData);
        if (null == adapter) {
            adapter = new Adapter(this.getActivity(), datas);
            listview_cs.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        listview_cs.setOnItemClickListener(this);
    }

    public ListView getListview_cs() {
        return listview_cs;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CSInfo csInfo = datas.get(position);
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(csInfo.getValue());
        ToastUtil.showToast("复制成功，请到QQ或微信联系我们的客服吧",this.getActivity());
    }
}
