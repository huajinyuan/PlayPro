package cn.gtgs.base.playpro.activity.home.live;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gtgs.base.playpro.R;

public class Play2Activity extends AppCompatActivity {
    @BindView(R.id.listView)
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_2);
        ButterKnife.bind(this);
        mList.setAdapter(new Adapter());
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
            return LayoutInflater.from(Play2Activity.this).inflate(R.layout.layout_tyrants_item_recycler, null);
        }
    }
}
