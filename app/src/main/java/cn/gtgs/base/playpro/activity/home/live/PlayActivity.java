package cn.gtgs.base.playpro.activity.home.live;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.mymessage.ChatActivity;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.widget.AllGiftViewpager;
import cn.gtgs.base.playpro.widget.ChatEmoticoViewPager;
import cn.gtgs.base.playpro.widget.MyViewPagerAdapter;
import cn.gtgs.base.playpro.widget.OnEmoticoSelectedListener;
import cn.gtgs.base.playpro.widget.ParentViewPaperAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class PlayActivity extends AppCompatActivity implements OnEmoticoSelectedListener {
    //-----------以下为环信
    String chatroomid = "261649293176209844";
    String et_huanxin_content;
    private List<EMMessage> msgList = new ArrayList<>();
    MessageChatroomAdapter adapter;
    private EMConversation conversation;
    private boolean isJoined = false;
    String message_content, message_from;
    //--------------------
    Context context;
    ArrayList<View> view_giftslist = new ArrayList<>();
    @BindView(R.id.vv_test)
    PLVideoView vv_test;
    @BindView(R.id.et_content)
    EditText et_content;
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
    @BindView(R.id.vp_gifts)
    ViewPager vp_gifts;
    @BindView(R.id.vp_emoji)
    ViewPager vp_emoji;
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
    @BindView(R.id.layout_play_bottom_dialog)
    View mLayoutBottom;
    @BindView(R.id.iv_live_booking_anchoricon)
    ImageView iv_live_booking_anchoricon;
    @BindView(R.id.iv_booking_sex)
    ImageView iv_booking_sex;
    @BindView(R.id.tv_booking_currentname)
    TextView tv_booking_currentname;
    @BindView(R.id.tv_live_booking_anchorid)
    TextView tv_live_booking_anchorid;
    @BindView(R.id.listView)
    ListView listView;

    Timer timer_hide = new Timer();
    Follow anchorItem;
    UserInfo loginInfo;
    Follow mF;
    ArrayList<Gift> gifts;
    Gift gift = null;
    private final int JOINCHATROOM = 1;
    private final int BLACKACTION = 2;
    private final int REPORTACTION = 3;
    private final int GETONLINECOUNT = 4;
    private final int LEAVECHAT = 5;
    private final int FOLLOWINGANCHOR = 6;

    private final String defautPath2 = "http://playback.yequtv.cn/appleplayback2.m3u8";
    AllGiftViewpager viewpager;
    //        private boolean mIsGetGift = false;
    private String[] mReport = {"广告欺诈", "淫秽色情", "骚扰谩骂", "反动政治", "其他内容"};
    private String star_at;
    //    @BindView(R.id.periscope)
//    public PeriscopeLayout periscopeLayout;
    private int FACE_SIZE;// 表情大小

    boolean isMember = false;

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
        gifts = new ArrayList<>();
        context = this;
        ButterKnife.bind(this);
        ACache aCache = ACache.get(context);
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        loginInfo = mF.getMember();
        Intent intent = getIntent();
        anchorItem = (Follow) (intent.getSerializableExtra("anchoritem"));
        isMember = intent.getBooleanExtra("IsMember", false);
        FACE_SIZE = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
        initviews();
        initEmoji();
        if (!isMember) {
            doPlay();
            //----------------------------------------------------------以下为环信
            //-------------------------------------------------------------------
            login();
        }

    }

    public void doPlay() {
        vv_test.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
                F.e("播放中错误状态===============================" + i);
                return false;
            }
        });
        vv_test.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                mGb.setVisibility(View.GONE);
            }
        });
        String MYURL = defautPath2;
        if (null != anchorItem.getWcPullAddress()) {
            MYURL = anchorItem.getWcPullAddress();
        }
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_MEDIACODEC, 1);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        vv_test.setAVOptions(options);
        vv_test.setVideoPath(MYURL);
        vv_test.start();
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

//        if (StringUtils.isNotEmpty(star_at)) {
//            String url = UriTemplate.fromTemplate(Config.URL_LEAVECHAT)
//                    .set("id", anchorItem.id)
//                    .expand();
//            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//            request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//            request.add("start_at", star_at);
//            CallServer.getRequestInstance().add(LEAVECHAT, request, joinRoomListener);
//        }


    }


    public void sendMsg() {
        vp_emoji.setVisibility(View.GONE);
        et_huanxin_content = et_content.getText().toString().trim();
        F.e("------------------------" + et_huanxin_content);
        if (et_huanxin_content.isEmpty()) {
            Log.e("main", "isempty");
        } else {
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++发送单聊、群聊信息
            EMMessage message = EMMessage.createTxtSendMessage(et_huanxin_content, chatroomid);
            //如果是群聊，设置chattype，默认是单聊
            message.setFrom("Prof. Elaina McCullough V");
            message.setChatType(EMMessage.ChatType.ChatRoom);
            message.setAttribute("user_name", "Prof. Elaina McCullough V");
            message.setAttribute("level", "lv20");
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
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PlayActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void bt_likes(View v) {
        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "likes", chatroomid);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setFrom("");
        message.setAttribute("user_name", "Prof. Elaina McCullough V");
        message.setAttribute("SPOT_KEY", "FJZY_SPOT");
        message.setAttribute("level", "lv20");
        EMClient.getInstance().chatManager().sendMessage(message);
        Map<String, Object> map = message.ext();
        showLikes(message.getFrom());
//        if (temp) {
//            new MyTimer(2000, 300).start();
//            temp = false;
//        }


    }

    private Gift mGetGift;


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
            public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {
                if (roomId.equals(chatroomid)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
                    } else {
//                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

            @Override
            public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {

            }

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {

            }

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {

            }

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {

            }

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {

            }
//            @Override
//            public void onMemberKicked(String roomId, String roomName, String participant) {
//                if (roomId.equals(chatroomid)) {
//                    String curUser = EMClient.getInstance().getCurrentUser();
//                    if (curUser.equals(participant)) {
//                        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
//                    } else {
////                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
//                    }
//                }
//            }


        };

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }


    public void getCountOnline() {
//        String url = UriTemplate.fromTemplate(Config.URL_ANCHOR_ONLINE_COUNT)
//                .set("ids", anchorItem.id)
//                .expand();
//        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        CallServer.getRequestInstance().add(GETONLINECOUNT, request, ActionListener);
    }

    void setopenemoji() {
        vp_emoji.setVisibility(vp_emoji.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
        if ((vp_emoji.getVisibility() == View.VISIBLE) && imm.isActive()) {
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

    void setchat() {
        hidelayout();
        frame_live_chat.setVisibility(View.VISIBLE);
    }

    void booking() {
//        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra("anchor", anchorItem);
//        startActivity(intent);
    }

    public void go2Info() {
//        Intent intent = new Intent(this, AnchorInfoActivity.class);
//        intent.putExtra("anchor_id", anchorItem.id);
//        startActivity(intent);
    }


    void setgifts() {
        hidelayout();
        framel_live_gifts.setVisibility(View.VISIBLE);
//        gifts = (ArrayList<Gift>) ACache.get(this).getAsObject("Lives_Gifts");

        if (gifts.isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Gift g = new Gift();
                g.id = i + "";
                int emoticonsId = getResources().getIdentifier("icon_" + i, "mipmap", getPackageName());
                g.picture = emoticonsId;
                gifts.add(g);
            }

        }
//        if (null != gifts && !gifts.isEmpty()) {
        viewpager = new AllGiftViewpager(context, gifts, new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
//                                indicatorScroll(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        view_giftslist.add(viewpager);
        vp_gifts.setAdapter(new MyViewPagerAdapter(view_giftslist));
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

    void setbookingclose() {
        mLayoutBottom.setVisibility(View.INVISIBLE);
//        m.setVisibility(View.INVISIBLE);
        frame_live_menu.setVisibility(View.VISIBLE);
    }

    void setviewClick() {
        frame_live_menu.setVisibility(View.VISIBLE);
        frame_live_chat.setVisibility(View.INVISIBLE);
        framel_live_gifts.setVisibility(View.INVISIBLE);
        mLayoutBottom.setVisibility(View.INVISIBLE);
    }

    void setsendgift() {
//        gift = viewpager.getmSelectGift();
//        if (null != gift) {
//            sendGift(gift.id);
//        } else {
//            Toast.makeText(context, "选一个礼物吧", LENGTH_SHORT).show();
//
//        }
    }

    void settochat() {
        Intent intent = new Intent(context, ChatActivity.class);
        if (!isMember) {
            intent.putExtra("chatto", anchorItem.getMbId());
        } else {
            intent.putExtra("chatto", anchorItem.getMember().getMbId() + "");
        }
        startActivity(intent);
    }

    void GetCoin() {
//        Intent intent = new Intent(LiveActivity.this, GetCoinActivity.class);
//        if (null != currentCredits) {
//            intent.putExtra("currentCoin", currentCredits.current_credits);
//
//        }
//        startActivity(intent);
    }

    public void contentAction() {
        hidelayout();
        mLayoutBottom.setVisibility(View.VISIBLE);
    }

    public void setBlackAction() {
//        Request<String> request = NoHttp.createStringRequest(Config.URL_ANCHOR_BLACKACTION, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.add("anchor_id", anchorItem.id);
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        CallServer.getRequestInstance().add(BLACKACTION, request, ActionListener);
    }

    public void actionFollow() {
//        Request<String> request = NoHttp.createStringRequest(Config.URL_ANCHOR_FOLLOWINGS, RequestMethod.POST);
//        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        request.add("anchor_id", anchorItem.id);
//        request.addHeader("Authorization", "Bearer " + loginInfo.token);
//        CallServer.getRequestInstance().add(FOLLOWINGANCHOR, request, ActionListener);
    }


    AlertDialog mTipDialog;
//    ReportListAdapter mListAdapter;

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
        mLayoutBottom.setVisibility(View.INVISIBLE);
        framel_live_gifts.setVisibility(View.INVISIBLE);
    }

    private List<ViewPager> viewpagers = new ArrayList<ViewPager>();
    private ChatEmoticoViewPager emoticoViewPager;

    public void initEmoji() {
        emoticoViewPager = new ChatEmoticoViewPager(context);
        emoticoViewPager.setOnEmoticoSelectedListener(this);
        viewpagers.add(emoticoViewPager);
        ParentViewPaperAdapter viewPaperAdapter = new ParentViewPaperAdapter(viewpagers);
        vp_emoji.setAdapter(viewPaperAdapter);
    }

    public void showLikes(String from) {
//        if (from.equals(loginInfo.name)) {
//            tv_likes.setText("你 给主播点了赞");
//
//        } else {
//            tv_likes.setText(from + " 给主播点了赞");
//
//        }
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
//        if (from.equals(loginInfo.name)) {
//            tv_likes.setText("你 给主播送了一个" + gift.name);
//        } else {
//            tv_likes.setText("用户 " + from + " 给主播送了一个" + gift.name);
//
//        }

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
        if (null != anchorItem) {
            String p = !isMember ? anchorItem.anPhoto : anchorItem.getMember().getMbPhoto();
            String photo = null != p ? Config.BASE + p : null;
            F.e("------------------------" + photo);
            String n = !isMember ? anchorItem.getAnRemark() : anchorItem.getMember().getMbNickname();
            String phone = !isMember ? anchorItem.getAnId() : anchorItem.getMember().mbPhone;
            String name = null != n ? n : phone;
            String sex = !isMember ? anchorItem.getAnSex() : anchorItem.getMember().getMbSex() + "";
            String mId = !isMember ? anchorItem.mbId : anchorItem.getMember().getMbId() + "";
            chatroomid = !isMember ? anchorItem.getChatRoomId() : "261649293176209844";
//            if (!isMember) {
//                chatroomid = anchorItem.getChatRoomId();
            Glide.with(context).load(null != photo ? photo : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImgIcon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgIcon.setImageDrawable(circularBitmapDrawable);
                }
            });
            mTvName.setText(name);
            Glide.with(context).load(null != photo ? photo : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_live_booking_anchoricon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_live_booking_anchoricon.setImageDrawable(circularBitmapDrawable);
                }
            });
            iv_booking_sex.setImageResource(sex.equals("1") ? R.mipmap.global_male : R.mipmap.global_female);
            tv_booking_currentname.setText(name);
            tv_live_booking_anchorid.setText(mId);
//            } else {
//                UserInfo info = anchorItem.getMember();
//                F.e("*********************************" + info.toString());
////                Glide.with(context).load(null != info.getMbPhone() ? Config.BASE + info.getMbPhone() : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImgIcon) {
////                    @Override
////                    protected void setResource(Bitmap resource) {
////                        RoundedBitmapDrawable circularBitmapDrawable =
////                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
////                        circularBitmapDrawable.setCircular(true);
////                        mImgIcon.setImageDrawable(circularBitmapDrawable);
////                    }
////                });
////                mTvName.setText(null != info.getMbNickname() ? info.getMbNickname() : info.getMbPhone());
////                Glide.with(context).load(null != info.getMbPhone() ? Config.BASE + info.getMbPhone() : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_live_booking_anchoricon) {
////                    @Override
////                    protected void setResource(Bitmap resource) {
////                        RoundedBitmapDrawable circularBitmapDrawable =
////                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
////                        circularBitmapDrawable.setCircular(true);
////                        iv_live_booking_anchoricon.setImageDrawable(circularBitmapDrawable);
////                    }
////                });
////                iv_booking_sex.setImageResource(info.getMbSex() == 1 ? R.mipmap.global_male : R.mipmap.global_female);
////                tv_booking_currentname.setText(null != info.getMbNickname() ? info.getMbNickname() : info.getMbPhone());
////                tv_live_booking_anchorid.setText(info.getMbId());
//
//            }

        }

        contentAction();

    }

    @OnClick({R.id.bt_live_booking_tochat, R.id.bt_send, R.id.bt_openemoji, R.id.et_content, R.id.bt_live_chat, R.id.bt_live_booking, R.id.bt_live_gifts, R.id.view_click, R.id.bt_live_sendgift, R.id.tv_live_recharge, R.id.layout_live_icon_content})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                sendMsg();
                break;
            case R.id.bt_openemoji:
                setopenemoji();
                break;
            case R.id.et_content:
                setetClick();
                break;
            case R.id.bt_live_chat:
                setchat();
                break;
//            case R.id.btn_live_dingtai:
//                booking();
//                break;
            case R.id.bt_live_booking:
                booking();
                break;
//            case R.id.iv_live_booking_anchoricon:
//                go2Info();
//                break;
            case R.id.bt_live_gifts:
                setgifts();
                break;
//            case R.id.tv_live_booking_close:
//                setbookingclose();
//                break;
            case R.id.view_click:
                if (!isMember) {
                    setviewClick();
                }
                break;
            case R.id.bt_live_sendgift:
                setsendgift();
                break;
            case R.id.bt_live_booking_tochat:
                settochat();
                break;
            case R.id.tv_live_recharge:
                GetCoin();
                break;
            case R.id.layout_live_icon_content:
                contentAction();
                break;
        }
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

    public void login() {

        String userName = "111";
        String password = "111";
        if (null != loginInfo) {
            userName = loginInfo.getMbId() + "";
            password = MD5Util.getMD5("webcast" + loginInfo.getMbId());
        }


        EMClient.getInstance().login(userName, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                joinchatroom();
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

//    public void JoinChatRoom(String anchorId) {
//        String url = UriTemplate.fromTemplate(Config.URL_JOINCHAT)
//                .set("id", anchorId)
//                .expand();
////        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
////        request.add("key", "z45CasVgh8K3q6300g0d95VkK197291A");
////        request.addHeader("Authorization", "Bearer  " + loginInfo.token);
////
////        CallServer.getRequestInstance().add(JOINCHATROOM, request, joinRoomListener);
//
//        HttpParams params = new HttpParams();
//        params.put("key", "z45CasVgh8K3q6300g0d95VkK197291A");
//        PostRequest request = OkGo.post(url).params(params);
//        HttpMethods.getInstance().doPost(request, true).subscribe(new Subscriber<Response>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Response response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (null != anchorItem) {
//                            joinchatroom();
//                        }
//                    }
//                });
//            }
//        });
//
//    }

    public void joinchatroom() {
        Log.e("adad", "startJoinChatRoom..");
        EMClient.getInstance().chatroomManager().joinChatRoom(chatroomid, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(chatroomid);
                if (room != null) {
                    showChatroomToast("join room success : " + room.getName());
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

    public void loadsomes() {
        Log.e("dsz", "start loadsomes..");
//        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++获取单聊、群聊 聊天记录
        adapter = new MessageChatroomAdapter(msgList, PlayActivity.this);
        listView.setAdapter(adapter);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        if (msgList.size() > 0)
            listView.setSelection(listView.getCount() - 1);
        Log.e("sdad", "finish loadsomes");
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            Log.e("onMessageReceived", "收到消息" + list.toString());
            for (EMMessage message : list) {
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
//                        mGetGift.picture = (String) map.get("GiftPicture");
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
        public void onCmdMessageReceived(List<EMMessage> list) {
            Log.e("onCmdMessageReceived", "收到消息" + list.toString());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

}
