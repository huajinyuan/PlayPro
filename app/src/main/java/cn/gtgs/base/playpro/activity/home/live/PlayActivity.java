package cn.gtgs.base.playpro.activity.home.live;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.home.live.model.Gift;
import cn.gtgs.base.playpro.activity.home.live.model.LoginInfo;
import cn.gtgs.base.playpro.activity.home.model.AnchorItem;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;

public class PlayActivity extends AppCompatActivity  {
    //-----------以下为环信
    String chatroomid = "261649293176209844";
    EditText et_content;
    ListView listView;
    String et_huanxin_content;
    private List<EMMessage> msgList = new ArrayList<>();
    MessageChatroomAdapter adapter;
    private EMConversation conversation;
    private boolean isJoined = false;
    String message_content, message_from;
    //--------------------
    PLVideoView vv_test;
    Context context;
    ArrayList<View> view_giftslist = new ArrayList<>();

    @BindView(R.id.tv_likes)
    TextView tv_likes;
    @BindView(R.id.iv_gift)
    ImageView iv_gift;
    @BindView(R.id.frame_live_menu)
    RelativeLayout frame_live_menu;
    @BindView(R.id.frame_live_chat)
    LinearLayout frame_live_chat;
    @BindView(R.id.frame_live_gifts)
    LinearLayout framel_live_gifts;
    @BindView(R.id.frame_live_booking)
    RelativeLayout frame_live_booking;
    @BindView(R.id.vp_gifts)
    ViewPager vp_gifts;
    @BindView(R.id.vp_emoji)
    ViewPager vp_emoji;

    @BindView(R.id.iv_live_booking_anchoricon)
    ImageView iv_booking_anchoricon;
    @BindView(R.id.tv_booking_currentname)
    TextView tv_booking_currentname;
    @BindView((R.id.tv_booking_level))
    TextView tv_booking_level;
    @BindView(R.id.iv_booking_sex)
    ImageView iv_booking_sex;
    @BindView((R.id.tv_live_booking_anchorid))
    TextView tv_live_booking_anchorid;
    @BindView((R.id.tv_live_booking_anchorplace))
    TextView tv_live_booking_anchorplace;
    @BindView(R.id.tv_live_credits)
    TextView tv_live_credits;
    @BindView(R.id.img_layout_content_icon)
    ImageView mImgIcon;
    @BindView(R.id.tv_layout_content_name)
    TextView mTvName;
    @BindView(R.id.tv_layout_content_lv)
    TextView mTvLv;
    @BindView(R.id.tv_layout_content_count)
    TextView mTvCount;
    @BindView(R.id.img_live_bg)
    ImageView mGb;
    @BindView(R.id.tv_anchor_info_dingtai_count)
    TextView mDingtaiCount;
    @BindView(R.id.tv_anchor_info_followers_count)
    TextView mGzhCount;
    @BindView(R.id.tv_anchor_info_shouli_count)
    TextView mShouliCount;
    @BindView(R.id.tv_anchor_info_fensi_count)
    TextView mFensiCount;
    Timer timer_hide = new Timer();
    AnchorItem anchorItem;
//    RequestQueue requestQueue = NoHttp.newRequestQueue();
    LoginInfo loginInfo;
    ArrayList<Gift> gifts;
    Gift gift = null;
    private final int JOINCHATROOM = 1;
    private final int BLACKACTION = 2;
    private final int REPORTACTION = 3;
    private final int GETONLINECOUNT = 4;
    private final int LEAVECHAT = 5;
    private final int FOLLOWINGANCHOR = 6;

    private final String defautPath2 = "http://playback.yequtv.cn/appleplayback2.m3u8";
//    AllGiftViewpager viewpager;
    //    private boolean mIsGetGift = false;
    private String[] mReport = {"广告欺诈", "淫秽色情", "骚扰谩骂", "反动政治", "其他内容"};
    private String star_at;
    View view_emoji1, view_emoji2;
    GridView gv_emoji1, gv_emoji2;
    Map<String, Integer> emojisIdMap = PApplication.emoticonsIdMap;
    List<String> emojisKeyList = PApplication.emoticonKeyList;
//    @BindView(R.id.periscope)
//    public PeriscopeLayout periscopeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_play);
        context = this;
        ButterKnife.bind(this);
        ACache aCache = ACache.get(context);
        loginInfo = (LoginInfo) (aCache.getAsObject("logininfo"));
        Intent intent = getIntent();
        anchorItem = (AnchorItem) (intent.getSerializableExtra("anchoritem"));
        if (null != anchorItem) {
            chatroomid = anchorItem.huanxin_chatroom_id;
            Glide.with(context).load(anchorItem.avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImgIcon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgIcon.setImageDrawable(circularBitmapDrawable);
                }
            });
            mTvName.setText(anchorItem.name);
            mTvLv.setText(anchorItem.level);
            mTvCount.setText(anchorItem.online_count);
//            mDingtaiCount.setText(anchorItem.);
        }
        initviews();
        initEmoji();

        vv_test = (PLVideoView) findViewById(R.id.vv_test);
        vv_test.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
                F.e("播放中错误状态===============================" + i);
//                vv_test.setVideoPath(defautPath);
//                vv_test.start();
//                Random r = new Random();
//                int radom = r.nextInt(3);
//                ExampleUtil.showToast(VideoPaths[radom], LiveActivity.this);
//                vv_test.setVideoPath(VideoPaths[radom]);
//                vv_test.start();

                return false;
            }
        });
        vv_test.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                mGb.setVisibility(View.GONE);
//                mIsStartPlay = true;
//                mIsPlay = true;
            }
        });
        String MYURL = defautPath2;
        if (null != anchorItem.stream && null != anchorItem.stream.play) {
            MYURL = anchorItem.stream.play;
        }

        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_MEDIACODEC, 1);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        vv_test.setAVOptions(options);
        vv_test.setVideoPath(MYURL);
//        vv_test.setVideoPath("http://playback.yequtv.cn/appleplayback2.m3u8");
        vv_test.start();


        //----------------------------------------------------------以下为环信
        //-------------------------------------------------------------------
        listView = (ListView) findViewById(R.id.listView);
        et_content = (EditText) findViewById(R.id.et_content);
        if (EMClient.getInstance().isLoggedInBefore()) {
            Log.e("main", "islogged");
            login();
        } else {
            Log.e("main", "logging");
            login();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        vv_test.setVideoPath(anchorItem.stream.play);
//        vv_test.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv_test.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        vv_test.stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //-----------------------------------以下为环信
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
//
        if (StringUtils.isNotEmpty(star_at)) {
//            String url = UriTemplate.fromTemplate(Config.URL_LEAVECHAT)
//                    .set("id", anchorItem.id)
//                    .expand();
//            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//            request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//            request.add("start_at", star_at);
//            CallServer.getRequestInstance().add(LEAVECHAT, request, joinRoomListener);
        }


    }

    //------------------------------------------------------------------------------以下为环信功能
    //------------------------------------------------------------------------------
    public void login() {
        String userName = "111";
        String password = "111";
        if (null != loginInfo) {
            userName = loginInfo.huanxin_username;
            password = loginInfo.huanxin_password;
        }
        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                JoinChatRoom(anchorItem.id);
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("main", "progress" + progress + "");
            }

            @Override
            public void onError(int code, String message) {
                Log.e("main", "登录聊天服务器失败！");
            }
        });

    }

    public void loadsomes() {
        Log.e("dsz", "start loadsomes..");
//        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++获取单聊、群聊 聊天记录
//        adapter = new MessageChatroomAdapter(msgList, LiveActivity.this);
        listView.setAdapter(adapter);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        if (msgList.size() > 0)
            listView.setSelection(listView.getCount() - 1);
        Log.e("sdad", "finish loadsomes");
    }

    public void bt_send(View v) {
        vp_emoji.setVisibility(View.GONE);
        et_huanxin_content = et_content.getText().toString().trim();
        if (et_huanxin_content.isEmpty()) {
            Log.e("main", "isempty");
        } else {
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++发送单聊、群聊信息
            EMMessage message = EMMessage.createTxtSendMessage(et_huanxin_content, chatroomid);
            //如果是群聊，设置chattype，默认是单聊
            message.setFrom(loginInfo.name);
            message.setChatType(EMMessage.ChatType.ChatRoom);
            message.setAttribute("user_name", loginInfo.name);
            message.setAttribute("level", loginInfo.level);
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);

            msgList.add(message);
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
            if (msgList.size() > 0) {
                listView.setSelection(listView.getCount() - 1);
            }

        }
        et_content.setText("");
        frame_live_chat.setVisibility(View.INVISIBLE);
        frame_live_menu.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
//            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LiveActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        }
    }

    public void bt_likes(View v) {
        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "likes", chatroomid);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setFrom(loginInfo.name);
        message.setAttribute("user_name", loginInfo.name);
        message.setAttribute("SPOT_KEY", "FJZY_SPOT");
        message.setAttribute("level", loginInfo.level);
        EMClient.getInstance().chatManager().sendMessage(message);
        Map<String, Object> map = message.ext();
        showLikes(message.getFrom());
//        if (temp) {
//            new MyTimer(2000, 300).start();
//            temp = false;
//        }


    }

    private Gift mGetGift;
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.e("main", "收到消息");

            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getFrom();
                    message_from = username;
                }
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                message_content = txtBody.getMessage();
                Map<String, Object> map = message.ext();
                String spotType = null;
                if (map.containsKey("SPOT_KEY")) {
                    spotType = (String) map.get("SPOT_KEY");
                }
                if (null != spotType) {
                    if (spotType.equals("FJZY_SPOT")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLikes(message_from);
                            }
                        });
                    } else if (spotType.equals("GIFT")) {
                        mGetGift = new Gift();
                        mGetGift.name = (String) map.get("GiftName");
                        mGetGift.picture = (String) map.get("GiftPicture");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showGifts(message_from, mGetGift);
//                                showGifts(message_from, message_content.substring(message_content.length() - 2));
                            }
                        });
                    } else {
                        msgList.add(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                if (msgList.size() > 0) {
                                    listView.setSelection(listView.getCount() - 1);
                                    Log.e("sad", "setselection");
                                }
                            }
                        });
                    }
                } else {
                    msgList.add(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (msgList.size() > 0) {
                                listView.setSelection(listView.getCount() - 1);
                                Log.e("sad", "setselection");
                            }
                        }
                    });
                }

            }
            // 收到消息
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            // 收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            // 收到已读回执
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            // 收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            // 消息状态变动

        }
    };

    public void joinchatroom() {
        Log.e("adad", "startJoinChatRoom..");
        EMClient.getInstance().chatroomManager().joinChatRoom(chatroomid, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(chatroomid);
                if (room != null) {
//                    showChatroomToast("join room success : " + room.getName());
                    Log.e("adad", "JoinChatRoom succeed");
                    isJoined = true;
                } else {
                    Log.e("dasda", "JoinChatRoom Failed!");
                }
                addChatRoomChangeListenr();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadsomes();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Log.e("sdad", "joinchatroom OnError");
            }
        });
    }

    public void addChatRoomChangeListenr() {
        EMChatRoomChangeListener chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(chatroomid)) {
//                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                getCountOnline();

                EMMessage message = EMMessage.createTxtSendMessage(participant + " 加入了聊天室", chatroomid);
                message.setChatType(EMMessage.ChatType.ChatRoom);
                message.setFrom("动态");
                msgList.add(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if (msgList.size() > 0) {
                            listView.setSelection(listView.getCount() - 1);
                        }
                    }
                });
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                getCountOnline();

                EMMessage message = EMMessage.createTxtSendMessage(participant + "离开了聊天室", chatroomid);
                message.setChatType(EMMessage.ChatType.ChatRoom);
                message.setFrom("动态");
                msgList.add(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if (msgList.size() > 0) {
                            listView.setSelection(listView.getCount() - 1);
                        }
                    }
                });
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(chatroomid)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
                    } else {
//                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

        };

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }

    public void JoinChatRoom(String anchorId) {
//        String url = UriTemplate.fromTemplate(Config.URL_JOINCHAT)
//                .set("id", anchorId)
////                .expand();
//        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.addHeader("Authorization", "Bearer  " + loginInfo.token);
//
//        CallServer.getRequestInstance().add(JOINCHATROOM, request, joinRoomListener);
    }

    public void getCountOnline() {
//        String url = UriTemplate.fromTemplate(Config.URL_ANCHOR_ONLINE_COUNT)
//                .set("ids", anchorItem.id)
//                .expand();
//        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        CallServer.getRequestInstance().add(GETONLINECOUNT, request, ActionListener);
    }

    @OnClick(R.id.bt_openemoji)
    void setopenemoji() {
        vp_emoji.setVisibility(vp_emoji.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((vp_emoji.getVisibility() == View.VISIBLE) && imm.isActive()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick(R.id.et_content)
    void setetClick() {
        vp_emoji.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((vp_emoji.getVisibility() == View.VISIBLE) && (!imm.isActive())) {
            F.e("here");
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick(R.id.bt_live_chat)
    void setchat() {
        hidelayout();
        frame_live_chat.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_live_dingtai)
    void booking() {
//        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra("anchor", anchorItem);
//        startActivity(intent);
    }

    @OnClick(R.id.bt_live_booking)
    void setbooking() {
//        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra("anchor", anchorItem);
//        startActivity(intent);
    }

    @OnClick({R.id.iv_live_booking_anchoricon})
    public void go2Info() {
//        Intent intent = new Intent(this, AnchorInfoActivity.class);
//        intent.putExtra("anchor_id", anchorItem.id);
//        startActivity(intent);
    }


    @OnClick(R.id.bt_live_gifts)
    void setgifts() {
        hidelayout();
//        framel_live_gifts.setVisibility(View.VISIBLE);
//        gifts = (ArrayList<Gift>) ACache.get(this).getAsObject("Lives_Gifts");
//        if (null != gifts && !gifts.isEmpty()) {
//            viewpager = new AllGiftViewpager(context, gifts, new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int arg0) {
////                                indicatorScroll(arg0);
//                }
//
//                @Override
//                public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int arg0) {
//
//                }
//            });
//            view_giftslist.add(viewpager);
//            vp_gifts.setAdapter(new MyViewPagerAdapter(view_giftslist));
//        } else {
//            Request<String> request = NoHttp.createStringRequest(Config.URL_Gifts);
//            request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//            requestQueue.add(0, request, responseListener);
//        }
////        if (!mIsGetGift) {
//        Request<String> request_current_credits = NoHttp.createStringRequest(Config.URL_Current_Credits);
//        request_current_credits.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request_current_credits.addHeader("Authorization", "Bearer " + loginInfo.token);
//        requestQueue.add(1, request_current_credits, responseListener);
////        }
////        else
////        {
////
////        }


    }

    @OnClick(R.id.tv_live_booking_close)
    void setbookingclose() {
        frame_live_booking.setVisibility(View.INVISIBLE);
        frame_live_menu.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.view_click)
    void setviewClick() {
        frame_live_menu.setVisibility(View.VISIBLE);
        frame_live_chat.setVisibility(View.INVISIBLE);
        framel_live_gifts.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bt_live_sendgift)
    void setsendgift() {
//        gift = viewpager.getmSelectGift();
//        if (null != gift) {
//            sendGift(gift.id);
//        } else {
//            Toast.makeText(context, "选一个礼物吧", LENGTH_SHORT).show();
//
//        }
    }

    @OnClick(R.id.bt_live_booking_tochat)
    void settochat() {
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra("chatto", anchorItem.huanxin_username);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_live_recharge)
    void GetCoin() {
//        Intent intent = new Intent(LiveActivity.this, GetCoinActivity.class);
//        if (null != currentCredits) {
//            intent.putExtra("currentCoin", currentCredits.current_credits);
//
//        }
//        startActivity(intent);
    }

    @OnClick(R.id.layout_live_icon_content)
    public void contentAction() {
        hidelayout();
        frame_live_booking.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_live_booking_lahei)
    public void setBlackAction() {
//        Request<String> request = NoHttp.createStringRequest(Config.URL_ANCHOR_BLACKACTION, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.add("anchor_id", anchorItem.id);
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        CallServer.getRequestInstance().add(BLACKACTION, request, ActionListener);
    }

    @OnClick(R.id.lin_anchor_info_action_follow)
    public void actionFollow() {
//        Request<String> request = NoHttp.createStringRequest(Config.URL_ANCHOR_FOLLOWINGS, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.add("anchor_id", anchorItem.id);
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        CallServer.getRequestInstance().add(FOLLOWINGANCHOR, request, ActionListener);
    }


    AlertDialog mTipDialog;
//    ReportListAdapter mListAdapter;

    @OnClick(R.id.tv_live_booking_jubao)
    public void showReportDialog() {
//        if (null == mListAdapter) {
//            mListAdapter = new ReportListAdapter(mReport, this);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
//        mTipDialog = builder.create();
//        mTipDialog.setInverseBackgroundForced(true);
//        View view = LayoutInflater.from(context).inflate(
//                R.layout.item_dialog_report, null);
//        ListView listView = (ListView) view.findViewById(R.id.lv_dialog_report_tip);
//        listView.setAdapter(mListAdapter);
//        listView.setOnItemClickListener(this);
//        view.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTipDialog.dismiss();
//            }
//        });
//        mTipDialog.setCanceledOnTouchOutside(true);
//        mTipDialog.show();
//        mTipDialog.setContentView(view);
    }


    public void hidelayout() {
        frame_live_menu.setVisibility(View.INVISIBLE);
        frame_live_chat.setVisibility(View.INVISIBLE);
        frame_live_booking.setVisibility(View.INVISIBLE);
        framel_live_gifts.setVisibility(View.INVISIBLE);
    }

    public void setEtEmoji(String emojiKey) {
//        Drawable drawable = getResources().getDrawable(YequApplication.emoticonsIdMap.get(emojiKey));
//        int emojiSize = (int) (0.5F + this.getResources().getDisplayMetrics().density * 18);
//        drawable.setBounds(0, 0, emojiSize, emojiSize);
//        ImageSpan imageSpan = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
//        SpannableString spannableString = new SpannableString(emojiKey);
//        spannableString.setSpan(imageSpan, 0, emojiKey.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        et_content.getText().insert(et_content.getSelectionStart(), spannableString);
    }

    public void initEmoji() {
//        view_emoji1 = LayoutInflater.from(context).inflate(R.layout.item_viewpager_emoji, null);
//        view_emoji2 = LayoutInflater.from(context).inflate(R.layout.item_viewpager_emoji, null);
//        ArrayList<View> views = new ArrayList<>();
//        views.add(view_emoji1);
//        views.add(view_emoji2);
//        vp_emoji.setAdapter(new MyViewPagerAdapter(views));
//
//        gv_emoji1 = (GridView) view_emoji1.findViewById(R.id.gv_emoji);
//        gv_emoji2 = (GridView) view_emoji2.findViewById(R.id.gv_emoji);
//
//        ArrayList<Integer> emojiIds1 = new ArrayList<>();
//        for (int i = 0; i < 21; i++) {
//            emojiIds1.add(emojisIdMap.get(emojisKeyList.get(i)));
//        }
//        ArrayList<Integer> emojiIds2 = new ArrayList<>();
//        for (int i = 21; i < 40; i++) {
//            emojiIds2.add(emojisIdMap.get(emojisKeyList.get(i)));
//        }
//        gv_emoji1.setAdapter(new GridViewEmojiAdapter(context, emojiIds1));
//        gv_emoji2.setAdapter(new GridViewEmojiAdapter(context, emojiIds2));
//
//        gv_emoji1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                setEtEmoji(emojisKeyList.get(i));
//            }
//        });
//        gv_emoji2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                setEtEmoji(emojisKeyList.get(i + 21));
//            }
//        });
    }

    public void showLikes(String from) {
        if (from.equals(loginInfo.name)) {
            tv_likes.setText("你 给主播点了赞");

        } else {
            tv_likes.setText(from + " 给主播点了赞");

        }
        tv_likes.setVisibility(View.VISIBLE);
        timer_hide.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_likes.setText("");
                        tv_likes.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 1500);
    }

    public void sendGift(String giftid) {
//        String sendUrl = UriTemplate.fromTemplate(Config.URL_SEND_GIFT)
//                .set("anchor_id", anchorItem.id)
//                .expand();
//        Request<String> request = NoHttp.createStringRequest(sendUrl, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        request.add("item_id", giftid);
//        request.add("quantity", "1");
//        request.add("anchor_id", anchorItem.id);
//        requestQueue.add(2, request, responseListener);
    }

    public void showGifts(String from, Gift gift) {
        Glide.with(context).load(gift.picture).into(iv_gift);
        //开始动画
        ScaleAnimation animation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        animation.setDuration(800);
        iv_gift.startAnimation(animation);
        if (from.equals(loginInfo.name)) {

            tv_likes.setText("你 给主播送了一个" + gift.name);
        } else {
            tv_likes.setText("用户 " + from + " 给主播送了一个" + gift.name);

        }

        iv_gift.setVisibility(View.VISIBLE);
        tv_likes.setVisibility(View.VISIBLE);
        timer_hide.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_likes.setText("");
                        tv_likes.setVisibility(View.INVISIBLE);
                        iv_gift.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 1800);
    }

    public void showChatroomToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, str, LENGTH_SHORT).show();
            }
        });
    }

    public void initviews() {

        Glide.with(context).load(anchorItem.avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_booking_anchoricon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv_booking_anchoricon.setImageDrawable(circularBitmapDrawable);
            }
        });
//        iv_booking_sex.setImageResource(anchorItem.gender.equals("f") ? R.drawable.icon_sex_f : R.drawable.icon_sex_m);
        tv_booking_currentname.setText(anchorItem.name);
        tv_booking_level.setText(anchorItem.level);
        tv_live_booking_anchorid.setText("ID:" + anchorItem.id);
        tv_live_booking_anchorplace.setText(anchorItem.place);
    }

//    CurrentCredits currentCredits;
//    OnResponseListener<String> responseListener = new OnResponseListener<String>() {
//        @Override
//        public void onStart(int what) {
//
//        }
//
//
//        @Override
//        public void onSucceed(int what, Response<String> response) {
//            if (response.responseCode() == 200) {
//                switch (what) {
//                    case 0:
////                        mIsGetGift = true;
//                        gifts = (ArrayList<Gift>) JSON.parseArray(response.get(), Gift.class);
//                        ACache.get(LiveActivity.this).put("Lives_Gifts", gifts, 86400);
//                        F.e(gifts.toString());
//                        viewpager = new AllGiftViewpager(context, gifts, new ViewPager.OnPageChangeListener() {
//
//                            @Override
//                            public void onPageSelected(int arg0) {
////                                indicatorScroll(arg0);
//                            }
//
//                            @Override
//                            public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//                            }
//
//                            @Override
//                            public void onPageScrollStateChanged(int arg0) {
//
//                            }
//                        });
////                        mViewpagerList.add(viewpager);
//                        view_giftslist.add(viewpager);
//                        vp_gifts.setAdapter(new MyViewPagerAdapter(view_giftslist));
//                        break;
//                    case 1:
//                        currentCredits = JSON.parseObject(response.get(), CurrentCredits.class);
//                        tv_live_credits.setText("趣味豆: " + currentCredits.current_credits);
//                        break;
//                    case 2:
//                        currentCredits = JSON.parseObject(response.get(), CurrentCredits.class);
//                        tv_live_credits.setText("趣味豆: " + currentCredits.current_credits);
//
//                        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "gift" + gift.id, chatroomid);
//                        message.setChatType(EMMessage.ChatType.ChatRoom);
//                        message.setFrom(loginInfo.name);
//                        message.setAttribute("GiftName", gift.name);
//                        message.setAttribute("user_name", loginInfo.name);
//                        message.setAttribute("GiftPicture", gift.picture);
//                        message.setAttribute("SPOT_KEY", "GIFT");
//                        message.setAttribute("level", loginInfo.level);
//                        message.setAttribute("credits", gift.credits);
//                        message.setAttribute("userID", loginInfo.id);
//                        message.setAttribute("user_avatar", loginInfo.avatar);
//                        EMClient.getInstance().chatManager().sendMessage(message);
//                        showGifts(message.getFrom(), gift);
//                        break;
//                }
//            } else if (response.responseCode() == 402)
//                Toast.makeText(context, "趣味豆不足", LENGTH_SHORT).show();
//            else
//                Toast.makeText(context, "无数据", LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onFailed(int what, Response<String> response) {
//            Toast.makeText(context, "请求失败", LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onFinish(int what) {
//
//        }
//    };
//    private HttpListener<String> joinRoomListener = new HttpListener<String>() {
//        @Override
//        public void onSucceed(int what, Response<String> response) {
//            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
//            F.e("加入聊天室==============================" + response.get());
//            if (responseCode == 200) {
//                if (RequestMethod.HEAD == response.request().getRequestMethod()) {
////                    ExampleUtil.showToast("Request is succeed", LiveActivity.this);
//                }
//                // 请求方法为HEAD时没有响应内容
//                else {
//                    switch (what) {
//                        case JOINCHATROOM:
////                            showChatroomToast(" room : " + chatroomid);
//                            try {
//                                JSONObject ob = JSON.parseObject(response.get());
//                                if (ob.containsKey("start_at")) {
//                                    YequApplication.JointRoomTime = ob.getString("start_at");
//                                    star_at = ob.getString("start_at");
//                                }
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (null != anchorItem) {
//                                            joinchatroom();
//                                        }
//                                    }
//                                });
//
//                            } catch (Exception e) {
//                            }
//                            break;
//                        case LEAVECHAT:
//                            star_at = null;
//                            YequApplication.JointRoomTime = null;
//                            break;
//                    }
//
//                }
//            } else {
//                switch (what) {
//                    case JOINCHATROOM:
//                        JSONObject ob = JSON.parseObject(response.get());
//                        if (ob.containsKey("error")) {
//                            ExampleUtil.showToast(ob.getString("error"), LiveActivity.this);
//                        } else {
//
//                            ExampleUtil.showToast("当前人数过多，加入聊天室失败", LiveActivity.this);
//                        }
//                        LiveActivity.this.finish();
//                        break;
//                }
//            }
//
//        }
//
//        @Override
//        public void onFailed(int what, Response<String> response) {
//
//        }
//    };
//    private HttpListener<String> ActionListener = new HttpListener<String>() {
//        @Override
//        public void onSucceed(int what, Response<String> response) {
//            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
//            F.e("action==============================" + response.get());
//            if (responseCode == 200) {
//                if (RequestMethod.HEAD == response.request().getRequestMethod()) {
////                    ExampleUtil.showToast("Request is succeed", LiveActivity.this);
//                }
//                // 请求方法为HEAD时没有响应内容
//                else {
//                    switch (what) {
//                        case BLACKACTION:
//                            ExampleUtil.showToast("操作成功", LiveActivity.this);
//                            break;
//                        case REPORTACTION:
//                            ExampleUtil.showToast("操作成功", LiveActivity.this);
//                            break;
//                        case GETONLINECOUNT:
//                            JSONArray array = JSON.parseArray(response.get());
//                            String count = array.getJSONObject(0).getString("count");
//                            mTvCount.setText(count);
//                            break;
//                        case FOLLOWINGANCHOR:
//                            ExampleUtil.showToast("操作成功", LiveActivity.this);
//                            break;
//
//                    }
//
//                }
//            } else {
//
//            }
//
//        }
//
//        @Override
//        public void onFailed(int what, Response<String> response) {
//
//        }
//    };
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (null != mTipDialog && mTipDialog.isShowing()) {
//            mTipDialog.dismiss();
//        }
//        Request<String> request = NoHttp.createStringRequest(Config.URL_ANCHOR_REPORTACTION, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.add("anchor_id", anchorItem.id);
//        switch (position) {
//            case 0:
//                request.add("type", "ad");
//                break;
//            case 1:
//                request.add("type", "porn");
//                break;
//            case 2:
//                request.add("type", "insult");
//                break;
//            case 3:
//                request.add("type", "politic");
//                break;
//            case 4:
//                request.add("type", "misc");
//                break;
//
//        }
//
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        CallServer.getRequestInstance().add(REPORTACTION, request, ActionListener);
//    }
//
//    private boolean temp = true;
//
//    private class MyTimer extends CountDownTimer {
//
//        private static final String TAG = "MyTimer";
//
//        //millisInFuture为你设置的此次倒计时的总时长，比如60秒就设置为60000
//        //countDownInterval为你设置的时间间隔，比如一般为1秒,根据需要自定义。
//        public MyTimer(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        //每过你规定的时间间隔做的操作
//        @Override
//        public void onTick(long millisUntilFinished) {
//            periscopeLayout.addHeart();
//            Log.d(TAG, "111");
//        }
//
//        //倒计时结束时做的操作↓↓
//        @Override
//        public void onFinish() {
//            temp = true;
//        }
//    }
}
