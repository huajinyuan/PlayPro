package cn.gtgs.base.playpro.activity.home.mymessage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import cn.gtgs.base.playpro.widget.ChatEmoticoViewPager;
import cn.gtgs.base.playpro.widget.OnEmoticoSelectedListener;
import cn.gtgs.base.playpro.widget.ParentViewPaperAdapter;

public class ChatActivity extends AppCompatActivity implements OnEmoticoSelectedListener {
    Context context;
    @BindView(R.id.frame_chat)
    LinearLayout frame_chat;
    @BindView(R.id.lv_chat)
    ListView lv_chat;
    @BindView(R.id.vp_emoji)
    ViewPager vp_emoji;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_topbar_title)
    TextView mTvTitle;

    Follow mF;
    UserInfo loginInfo;
    String message_from, message_content;
    String chatto,chattophoto,chattoname;
    ArrayList<EMMessage> msgList = new ArrayList<>();
    MessageAdapter adapter;
    String et_huanxin_content;
    private int FACE_SIZE;//
    // 表情大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication.getInstance().mActiviyts.add(this);
        setContentView(R.layout.activity_chat);
        context = this;
        ButterKnife.bind(this);
        ACache aCache = ACache.get(this);
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        loginInfo = mF.getMember();
        chatto = getIntent().getStringExtra("chatto");
        chattophoto = getIntent().getStringExtra("chattophoto");
        chattoname = getIntent().getStringExtra("chattoname");
        if (StringUtils.isNotEmpty(chattoname)) {
            mTvTitle.setText(chattoname);
        }
        FACE_SIZE = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
        login();
        initviews();
    }

    void setopenEmoji() {
        vp_emoji.setVisibility(vp_emoji.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((vp_emoji.getVisibility() == View.VISIBLE) && imm.isActive()) {
            F.e("here");
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    void setetClick() {
        vp_emoji.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((vp_emoji.getVisibility() == View.VISIBLE) && (!imm.isActive())) {
            F.e("here");
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick({R.id.img_topbar_back, R.id.et_content, R.id.bt_openemoji})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.et_content:
                setetClick();
                break;
            case R.id.bt_openemoji:
                setopenEmoji();
                break;
        }
    }

    private List<ViewPager> viewpagers = new ArrayList<ViewPager>();
    private ChatEmoticoViewPager emoticoViewPager;

    void initviews() {
        emoticoViewPager = new ChatEmoticoViewPager(context);
        emoticoViewPager.setOnEmoticoSelectedListener(this);
        viewpagers.add(emoticoViewPager);
        ParentViewPaperAdapter viewPaperAdapter = new ParentViewPaperAdapter(viewpagers);
        vp_emoji.setAdapter(viewPaperAdapter);
    }

    public void login() {

        String userName = "111";
        String password = "111";
        if (null != loginInfo) {
            userName = loginInfo.getMbPhone() + "";
            password = MD5Util.getMD5("webcast" + loginInfo.getMbId());
        }
        F.e(userName + "  " + password);
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                //加载聊天记录
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(StringUtils.isNotEmpty(chatto))
                        {
                            loadsomes();
                        }

                    }
                });
            }

            @Override
            public void onError(int code, final String s) {
                if (code == 200) {
                    EMClient.getInstance().logout(true);
                    login();
                } else {
                    Log.e("da", "Login EM Error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void loadsomes() {
        Log.e("dsz", "start loadsomes..");
        Log.e("dsaa", "chatto:" + chatto);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++获取单聊、群聊 聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(chatto, EMConversation.EMConversationType.Chat, true);
        conversation.markAllMessagesAsRead();
        if (MessageListActivity.instance != null)
            MessageListActivity.instance.refreshList();

        msgList.addAll(conversation.getAllMessages());
        adapter = new MessageAdapter(msgList, context);
        lv_chat.setAdapter(adapter);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        if (msgList.size() > 0)
            lv_chat.setSelection(lv_chat.getCount() - 1);
        Log.e("sdad", "finish loadsomes");
    }

    @OnClick(R.id.bt_send)
    void setsend() {
        et_huanxin_content = et_content.getText().toString().trim();
        if (et_huanxin_content.isEmpty()) {
            Log.e("main", "isempty");
        } else {
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++发送单聊、群聊信息
            EMMessage message = EMMessage.createTxtSendMessage(et_huanxin_content, chatto);
            message.setChatType(EMMessage.ChatType.Chat);
            message.setAttribute("phone_from", loginInfo.getMbPhone());
            message.setAttribute("photo_from", loginInfo.getMbPhoto());
            message.setAttribute("name_from", loginInfo.getMbNickname());
            if (StringUtils.isEmpty(chatto))
            {
                ToastUtil.showToast("对方账号未知，消息未发送",this);
                return;
            }
            if (StringUtils.isEmpty(chattophoto))
            {
                ToastUtil.showToast("对方头像未知，消息未发送",this);
                return;
            }
            if (StringUtils.isEmpty(chattoname))
            {
                ToastUtil.showToast("对方昵称未知，消息未发送",this);
                return;
            }
            message.setAttribute("phone_to", chatto);
            message.setAttribute("photo_to", chattophoto);
            message.setAttribute("name_to", chattoname);


            EMClient.getInstance().chatManager().sendMessage(message);

            msgList.add(message);
            adapter.notifyDataSetChanged();
            if (MessageListActivity.instance != null)
                MessageListActivity.instance.refreshList();
            if (msgList.size() > 0) {
                lv_chat.setSelection(lv_chat.getCount() - 1);
            }
            EMClient.getInstance().chatManager().importMessages(msgList);
        }
        et_content.setText("");
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.e("main", "收到消息");
            if (MessageListActivity.instance != null)
                MessageListActivity.instance.refreshList();

            for (EMMessage message : messages) {
                if (message.getUserName().equals(chatto)) {
                    //--------------------------------------------->
                    message_from = message.getUserName();
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    message_content = txtBody.getMessage();

                    msgList.add(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (msgList.size() > 0) {
                                lv_chat.setSelection(lv_chat.getCount() - 1);
                                Log.e("sad", "setselection");
                            }
                        }
                    });

                    //---------------------------------------------->
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            // 收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

//        @Override
//        public void onMessageReadAckReceived(List<EMMessage> messages) {
//            // 收到已读回执
//        }
//
//        @Override
//        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//            // 收到已送达回执
//        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            // 消息状态变动

        }
    };

//    public void setEtEmoji(String emojiKey) {
//        Drawable drawable = getResources().getDrawable(YequApplication.emoticonsIdMap.get(emojiKey));
//        int emojiSize = (int) (0.5F + this.getResources().getDisplayMetrics().density * 18);
//        drawable.setBounds(0, 0, emojiSize, emojiSize);
//        ImageSpan imageSpan = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
//        SpannableString spannableString = new SpannableString(emojiKey);
//        spannableString.setSpan(imageSpan, 0, emojiKey.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        et_content.getText().insert(et_content.getSelectionStart(), spannableString);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onEmoticoSelected(String key) {
        F.e("-------------------------------------------------" + key);
        if ("DELETE".equals(key)) {
            int selection = et_content.getSelectionStart();
            String text = et_content.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    et_content.getText().delete(start, end);
                    return;
                }
                et_content.getText().delete(selection - 1, selection);
            }
            return;
        } else {
            try {
                SpannableString ss = new SpannableString(key);

                int id = PApplication.emoticonsIdMap.get(key);
                Drawable drawable = this.getResources().getDrawable(id);
                if (drawable != null) {
                    drawable.setBounds(0, 0, FACE_SIZE, FACE_SIZE);
                    ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
                    ss.setSpan(span, 0, key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 追加到editText
                    et_content.append(ss);
                }
            } catch (Exception e) {
            }
        }
    }
}
