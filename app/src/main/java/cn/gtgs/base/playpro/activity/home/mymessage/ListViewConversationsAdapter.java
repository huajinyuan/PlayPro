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
 * Created by  on 2016/11/7.
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
            String chatto=null,chattophoto=null,chattoname=null,phone_from=null,photo_from=null,name_from=null;
            if (map.containsKey("phone_to"))
            {
                 chatto = (String) map.get("phone_to");
            }
            if (map.containsKey("photo_to"))
            {
                 chattophoto = (String) map.get("photo_to");
            }
            if (map.containsKey("name_to"))
            {
                 chattoname = (String) map.get("name_to");
            }
            if (map.containsKey("phone_from"))
            {
                 phone_from = (String) map.get("phone_from");
            }
            if (map.containsKey("photo_from"))
            {
                 photo_from = (String) map.get("photo_from");
            }
            if (map.containsKey("name_from"))
            {
                 name_from = (String) map.get("name_from");
            }

            final ImageView icon = holder.iv_avatar;
            if (lastMessage.direct() == EMMessage.Direct.RECEIVE)//接收到的消息
            {
                if (StringUtils.isNotEmpty(name_from))
                {
                    holder.name.setText(StringUtils.isNotEmpty(name_from) ? "与 " + name_from + " 的会话" : "");
                }
                if (StringUtils.isNotEmpty(photo_from)){
                    Glide.with(getContext()).load(Config.BASE + photo_from).asBitmap().centerCrop().into(new BitmapImageViewTarget(icon) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            icon.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }

            }else{//发送的消息
                if (StringUtils.isNotEmpty(chattoname))
                {
                    holder.name.setText(StringUtils.isNotEmpty(chattoname) ? "与 " + chattoname + " 的会话" : "");
                }
               if (StringUtils.isNotEmpty(chattophoto))
               {
                   Glide.with(getContext()).load(Config.BASE + chattophoto).asBitmap().centerCrop().into(new BitmapImageViewTarget(icon) {
                       @Override
                       protected void setResource(Bitmap resource) {
                           RoundedBitmapDrawable circularBitmapDrawable =
                                   RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                           circularBitmapDrawable.setCircular(true);
                           icon.setImageDrawable(circularBitmapDrawable);
                       }
                   });
               }
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