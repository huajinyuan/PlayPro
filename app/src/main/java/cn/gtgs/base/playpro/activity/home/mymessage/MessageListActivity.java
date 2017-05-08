package cn.gtgs.base.playpro.activity.home.mymessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;

public class MessageListActivity extends AppCompatActivity {
    public static MessageListActivity instance;
    Context context;
    ACache aCache;
    Follow mF;
    UserInfo loginInfo;
    ArrayList<EMConversation> conversations = new ArrayList<>();
    ListViewConversationsAdapter adapter;

    @BindView(R.id.lv_conversitions)
    ListView lv_conversitions;
    @BindView(R.id.tv_message_tip)
    TextView mTvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);
        context = this;
        aCache = ACache.get(this);
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        loginInfo =mF.getMember();

        lv_conversitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EMConversation conversation = adapter.getItem(i);
                // 进入聊天页面
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatto", conversation.conversationId());
                startActivity(intent);
            }
        });

        login();
    }

    @OnClick(R.id.img_message_back)
    public void back() {
        this.finish();
    }

    public void login() {
        String userName = "111";
        String password = "111";
        if (null != loginInfo) {
            userName = loginInfo.getMbId()+"";
            password = MD5Util.getMD5("webcast"+loginInfo.getMbId());
        }
        F.e(userName+"  " +password);
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                refreshList();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("da", "Login EM Error");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "登录失败，可能是服务器不稳定", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void refreshList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //加载聊天列表
                conversations = loadConversationList();
                adapter = new ListViewConversationsAdapter(context, 1, conversations);
                lv_conversitions.setAdapter(adapter);
                if (conversations.isEmpty()) {
                    mTvTip.setVisibility(View.VISIBLE);
                    lv_conversitions.setVisibility(View.GONE);
                } else {
                    mTvTip.setVisibility(View.GONE);
                    lv_conversitions.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取会话列表
     */
    protected ArrayList<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(
                            new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));

                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
