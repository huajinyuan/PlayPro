package cn.gtgs.base.playpro.activity.home.mymessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;
import java.util.Map;

import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.widget.EmoticonsTextView;

/**
 * Created by zuoyun on 2016/8/3.
 */
public class MessageAdapter extends BaseAdapter {
    private List<EMMessage> msgs;
    private Context context;
    private LayoutInflater inflater;

    public MessageAdapter(List<EMMessage> msgs, Context context_) {
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
        EMMessage message = getItem(position);
        int viewType = getItemViewType(position);
        if (convertView == null) {
            if (viewType == 0) {
                convertView = inflater.inflate(R.layout.item_message_received, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_message_sent, parent, false);
            }
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tv_content = (EmoticonsTextView) convertView.findViewById(R.id.tv_chatcontent);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
            convertView.setTag(holder);
        }

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Map<String, Object> map = message.ext();
        String username = (String) map.get("user_name");
        String avatar = (String) map.get("user_avatar");
        holder.tv_content.setText(txtBody.getMessage());
        holder.tv_username.setText(username != null ? username : message.getFrom());
        if (avatar != null) {
            final ImageView iv_icon = holder.iv_avatar;
            Glide.with(context).load(null != avatar ? Config.BASE + avatar : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_icon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_icon.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_username;
        EmoticonsTextView tv_content;
        ImageView iv_avatar;
    }
}

