package cn.gtgs.base.playpro.activity.home.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;
import java.util.Map;

import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.model.Gift;
import cn.gtgs.base.playpro.widget.EmoticonsTextView;

/**
 * Created by  on 2016/8/3.
 */
public class MessageChatroomAdapter extends BaseAdapter {
    private List<EMMessage> msgs;
    private Context context;
    private LayoutInflater inflater;
    public MessageChatroomAdapter(List<EMMessage> msgs, Context context_) {
        this.msgs = msgs;
        this.context = context_;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        return message.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        EMMessage message = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message_chatroom_received, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        viewHolder.tv_username.setText(message.getFrom());


        Map<String, Object> map = message.ext();
        String content= txtBody.getMessage();
        if (map != null) {
            if (null!=map.get("user_name"))
            {
                viewHolder.tv_username.setText((String)map.get("user_name"));
            }
            if (map.get("level") != null) {
                viewHolder.tv_level.setVisibility(View.VISIBLE);
                viewHolder.tv_level.setText("lv" + map.get("level") + "");
            } else {
                viewHolder.tv_level.setVisibility(View.GONE);
            }
            if(map.containsKey("Gift"))
            {
                Gift gift = PApplication.getInstance().getGiftObject((String) map.get("Gift"));
                if (null!= gift)
                {
                        content = "送了一个:【"+gift.getName()+"】";
                }
            }
        }
        viewHolder.tv_content.setText(content);
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_username, tv_level;
        EmoticonsTextView tv_content;

        public ViewHolder(View view) {
            tv_username = (TextView) view.findViewById(R.id.tv_username);
            tv_level = (TextView) view.findViewById(R.id.tv_level);
            tv_content = (EmoticonsTextView) view.findViewById(R.id.tv_chatcontent);
        }
    }
}

