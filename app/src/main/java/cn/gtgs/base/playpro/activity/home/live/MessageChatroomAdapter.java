package cn.gtgs.base.playpro.activity.home.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
    private int mMaxLenth = -1;

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

    public void setmMaxLenth(int maxLenth){
        this.mMaxLenth = maxLenth;
    }
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        EMMessage message = getItem(position);
        boolean isGift = false;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message_chatroom_received, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        if (message.getFrom().equals("-999")) {
            viewHolder.tv_chatcontent_sys.setText(txtBody.getMessage());
            viewHolder.tv_chatcontent_sys.setVisibility(View.VISIBLE);
            viewHolder.tv_chatcontent_sys.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            viewHolder.tv_content.setVisibility(View.GONE);
            viewHolder.tv_level.setVisibility(View.GONE);
            viewHolder.tv_username.setVisibility(View.GONE);
        } else {
            Map<String, Object> map = message.ext();
            String content = txtBody.getMessage();
            if (map != null) {
                if (map.containsKey("JOIN_CHATROOM")) {
                    String name = "";
                    if (null != map.get("user_name")) {
                        name = (String) map.get("user_name");
                        viewHolder.tv_chatcontent_sys.setText("欢迎" + name + "加入聊天室");
                    }
                    viewHolder.tv_chatcontent_sys.setVisibility(View.VISIBLE);
                    viewHolder.tv_content.setVisibility(View.GONE);
                    viewHolder.tv_level.setVisibility(View.GONE);
                    viewHolder.tv_username.setVisibility(View.GONE);
                } else {
                    if (mMaxLenth > 0) {
                        viewHolder.tv_content.setMaxEms(mMaxLenth);
                    }
                    viewHolder.tv_chatcontent_sys.setVisibility(View.GONE);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_level.setVisibility(View.VISIBLE);
                    viewHolder.tv_username.setVisibility(View.VISIBLE);

                    if (null != map.get("user_name")) {
                        viewHolder.tv_username.setText((String) map.get("user_name"));
                    }
                    if (map.get("level") != null) {
                        viewHolder.tv_level.setVisibility(View.VISIBLE);
                        viewHolder.tv_level.setText("Lv" + map.get("level") + "");
                        int level = 0;
                        if (map.get("level") instanceof Integer) {
                            level = (int) map.get("level");
                        } else if (map.get("level") instanceof String) {
                            level = Integer.valueOf((String) map.get("level"));
                        }
                        int drawableId = 0;
                        int colorId = 0;
                        if (level <= 5) {
                            drawableId = R.drawable.shape_rec_1;
                            colorId = R.color.color_level_1;
                        } else if (level <= 10) {
                            drawableId = R.drawable.shape_rec_2;
                            colorId = R.color.color_level_2;
                        } else if (level <= 15) {
                            drawableId = R.drawable.shape_rec_3;
                            colorId = R.color.color_level_3;
                        } else if (level <= 20) {
                            drawableId = R.drawable.shape_rec_4;
                            colorId = R.color.color_level_4;
                        } else {
                            drawableId = R.drawable.shape_rec_5;
                            colorId = R.color.color_level_5;
                        }
                        viewHolder.tv_level.setBackgroundResource(drawableId);
                        viewHolder.tv_username.setTextColor(ContextCompat.getColor(context, colorId));
                    } else {
                        viewHolder.tv_level.setVisibility(View.GONE);
                    }
                    if (map.containsKey("Gift")) {
                        isGift = true;
                        Gift gift = PApplication.getInstance().getGiftObject((String) map.get("Gift"));
                        if (null != gift) {
                            content = "送了一个:【" + gift.getName() + "】";
                        }
                    }

                    SpannableStringBuilder style = new SpannableStringBuilder(content);
                    if (isGift) {
                        int bstart = content.indexOf("【");
                        style.setSpan(new ForegroundColorSpan(Color.YELLOW), bstart, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    viewHolder.tv_content.setText(style);
                    viewHolder.tv_content.setTextColor(ContextCompat.getColor(context, R.color.color_text_chat_item));
                }
            }
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView tv_username, tv_level;
        EmoticonsTextView tv_content;
        EmoticonsTextView tv_chatcontent_sys;

        public ViewHolder(View view) {
            tv_username = (TextView) view.findViewById(R.id.tv_username);
            tv_level = (TextView) view.findViewById(R.id.tv_level);
            tv_content = (EmoticonsTextView) view.findViewById(R.id.tv_chatcontent);
            tv_chatcontent_sys = (EmoticonsTextView) view.findViewById(R.id.tv_chatcontent_sys);
        }
    }
}

