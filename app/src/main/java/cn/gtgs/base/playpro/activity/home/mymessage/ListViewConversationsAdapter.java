package cn.gtgs.base.playpro.activity.home.mymessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.StringUtils;

/**
 * Created by zuoyun on 2016/11/7.
 */


public class ListViewConversationsAdapter extends ArrayAdapter<EMConversation> {
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;

    private boolean notiyfyByFilter;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
    Follow follow;

    public ListViewConversationsAdapter(Context context, int resource, List<EMConversation> objects) {
        super(context, resource, objects);
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
        ACache aCache = ACache.get(context);
        follow = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        }
        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            Map<String, Object> map = lastMessage.ext();
            String username = (String) map.get("user_name");
            String avatar = (String) map.get("user_avatar");
            String to_avatar = (String) map.get("to_avatar");
            String to_userName = (String) map.get("to_userName");
            if (username.equals(follow.getMember().getMbNickname())) {
                holder.name.setText(StringUtils.isNotEmpty(to_userName)?"与 " + to_userName + " 的会话":"");
                final ImageView iv_icon = holder.iv_avatar;
                Glide.with(getContext()).load(Config.BASE + to_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_icon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_icon.setImageDrawable(circularBitmapDrawable);
                    }
                });
            } else {
                holder.name.setText(StringUtils.isNotEmpty(username)?"与 " + username + " 的会话":"");
                final ImageView iv_icon = holder.iv_avatar;
                Glide.with(getContext()).load(Config.BASE + avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_icon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_icon.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            holder.message.setText(((EMTextMessageBody) lastMessage.getBody()).getMessage());
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */

        ImageView iv_avatar;

    }
}