package cn.gtgs.base.playpro.activity.home.live;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import cn.gtgs.base.playpro.http.HttpBase;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.AppUtil;
import cn.gtgs.base.playpro.utils.DESUtil;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.MD5Util;
import cn.gtgs.base.playpro.utils.StringUtils;
import cn.gtgs.base.playpro.utils.ToastUtil;
import cn.gtgs.base.playpro.widget.AllGiftViewpager;
import cn.gtgs.base.playpro.widget.ChatEmoticoViewPager;
import cn.gtgs.base.playpro.widget.MyViewPagerAdapter;
import cn.gtgs.base.playpro.widget.OnEmoticoSelectedListener;
import cn.gtgs.base.playpro.widget.ParentViewPaperAdapter;
import cn.gtgs.base.playpro.widget.PeriscopeLayout;
import okhttp3.Response;
import rx.Subscriber;

import static android.widget.Toast.LENGTH_SHORT;

public class PlayActivity extends AppCompatActivity implements OnEmoticoSelectedListener {
    //-----------以下为环信
    String chatroomid = "0";
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
    @BindView(R.id.tv_play_toast)
    TextView mTvToast;
    @BindView(R.id.lin_play_gift_panel_bottom)
    View mGiftPanel;

    Timer timer_hide = new Timer();
    Follow anchorItem;
    UserInfo loginInfo;
    Follow mF;
    ArrayList<Gift> gifts;
    Gift gift = null;
    private final String defautPath2 = "";
    AllGiftViewpager viewpager;
    private int FACE_SIZE;// 表情大小

    boolean isMember = false;
    @BindView(R.id.danmakuView)
    DanmakuView mDanmakuView;
    @BindView(R.id.checkbox_danmu)
    CheckBox mCheckBox;
    @BindView(R.id.periscope)
    public PeriscopeLayout periscopeLayout;
    @BindView(R.id.img_anchor_info_follow)
    ImageView mImgFollow;
    @BindView(R.id.tv_play_level)
    TextView mTvLevel;
    @BindView(R.id.tv_play_gold)
    TextView mTvGold;
    @BindView(R.id.tv_live_booking_jubao)
    TextView mTvCancel;
    @BindView(R.id.tv_play_toast_sys)
    TextView mTvSysToast;
    @BindView(R.id.tv_play_level_1)
    TextView mTvLevel_2;
    @BindView(R.id.rel_layout_bottom_dialog)
    View rel_layout_bottom_dialog;
    ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication.getInstance().mActiviyts.add(this);
//        if (Build.VERSION.SDK_INT >= 19) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_play);
        gifts = new ArrayList<>();
        context = this;
        ButterKnife.bind(this);
        aCache = ACache.get(context);
        mF = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        loginInfo = mF.getMember();
        Intent intent = getIntent();
        anchorItem = (Follow) (intent.getSerializableExtra("anchoritem"));
        isMember = intent.getBooleanExtra("IsMember", false);
        FACE_SIZE = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
        initviews();
        initEmoji();
        if (!isMember) {
            getAnChorInfo(anchorItem.getAnId());
            initviews();
        } else {
            mTvCancel.setVisibility(View.GONE);
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
            try {
                MYURL = new DESUtil().decrypt(anchorItem.getWcPullAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if (!isMember) {
            try {
                vv_test.setVideoPath(new DESUtil().decrypt(anchorItem.getWcPullAddress()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            vv_test.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv_test.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isMember) {
            vv_test.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //-----------------------------------以下为环信
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().chatroomManager().leaveChatRoom(chatroomid);
        if (null != timer2) {
            timer2.cancel();
        }
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
            message.setFrom(loginInfo.getMbNickname());
            message.setChatType(EMMessage.ChatType.ChatRoom);
            message.setAttribute("user_name", loginInfo.getMbNickname());
            message.setAttribute("level", loginInfo.getMbLevel());//
            if (mCheckBox.isChecked()) {
                message.setAttribute("DanMu", loginInfo.getMbLevel());
                doDanmu(et_huanxin_content);
            }
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
            et_content.setText("");
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                msgList.add(message);
                if (null != adapter) {
                    adapter.notifyDataSetChanged();
                }
                if (msgList.size() > 0) {
                    listView.setSelection(listView.getCount() - 1);
                }
            }

        }
    }

    public void bt_likes(View v) {
        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "DianZan", chatroomid);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setFrom(loginInfo.getMbNickname());
        message.setAttribute("user_name", loginInfo.getMbNickname());
        message.setAttribute("DianZan", "DianZan");
        message.setAttribute("level", loginInfo.getMbLevel());
        EMClient.getInstance().chatManager().sendMessage(message);
        if (temp) {
            new MyTimer(2000, 300).start();
            temp = false;
        }


    }

    private Gift mGetGift;


    public void addChatRoomChangeListenr() {
        EMChatRoomChangeListener chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(chatroomid)) {
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                getCountOnline();

//                EMMessage message = EMMessage.createTxtSendMessage(participant + " 加入了聊天室", chatroomid);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                message.setFrom("动态");
//                msgList.add(message);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                        if (msgList.size() > 0) {
//                            listView.setSelection(listView.getCount() - 1);
//                        }
//                    }
//                });
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                getCountOnline();

//                EMMessage message = EMMessage.createTxtSendMessage(participant + "离开了聊天室", chatroomid);
//                message.setChatType(EMMessage.ChatType.ChatRoom);
//                message.setFrom("动态");
//                msgList.add(message);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                        if (msgList.size() > 0) {
//                            listView.setSelection(listView.getCount() - 1);
//                        }
//                    }
//                });
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

        };

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }


    public void getCountOnline() {
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

    boolean IsAdd = false;

    void setgifts() {
        hidelayout();
        mGiftPanel.setVisibility(View.VISIBLE);
        framel_live_gifts.setVisibility(View.VISIBLE);
        if (!IsAdd) {
            IsAdd = true;
            if (gifts.isEmpty()) {
                gifts = PApplication.getInstance().getGift();
                F.e("----------------------" + gifts.toString());
            }
            viewpager = new AllGiftViewpager(context, gifts, new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
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
        }


    }

    void setbookingclose() {
        mLayoutBottom.setVisibility(View.INVISIBLE);
        frame_live_menu.setVisibility(View.VISIBLE);
        rel_layout_bottom_dialog.setVisibility(View.GONE);
    }

    void setviewClick() {
        mGiftPanel.setVisibility(View.GONE);
        frame_live_menu.setVisibility(View.VISIBLE);
        frame_live_chat.setVisibility(View.GONE);
        rel_layout_bottom_dialog.setVisibility(View.GONE);
        framel_live_gifts.setVisibility(View.INVISIBLE);
        mLayoutBottom.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PlayActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    void setsendgift() {
        gift = viewpager.getmSelectGift();
        if (null != gift) {
            sendGift(gift.getCredits());
        } else {
            Toast.makeText(context, "选一个礼物吧", LENGTH_SHORT).show();

        }
    }

    void settochat() {
        AddFriend();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatto", anchorItem.getMember().getMbPhone());
        intent.putExtra("chattophoto", anchorItem.getMember().getMbPhoto());
        intent.putExtra("chattoname", anchorItem.getMember().getMbNickname());
        startActivity(intent);
    }

    public void AddFriend() {

        HttpParams params = new HttpParams();
        params.put("userName", loginInfo.getMbPhone());
        params.put("friendName", anchorItem.getMember().getMbPhone());
        PostRequest request = OkGo.post(Config.MEMBER_ADDFRIEND).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
                String Str = null;
                try {
                    Str = response.body().string();
                    F.e("-----------------" + Str);
                    JSONObject ob = JSON.parseObject(Str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void contentAction() {
        hidelayout();
        mLayoutBottom.setVisibility(View.VISIBLE);
        rel_layout_bottom_dialog.setVisibility(View.VISIBLE);
        ArrayList<String> gs = PApplication.getInstance().getmFList();
        if (null != anchorItem.getAnId()) {
            if (gs.contains(anchorItem.getAnId())) {
                mImgFollow.setImageResource(R.mipmap.praise_photo_button_image2);
            } else {
                mImgFollow.setImageResource(R.mipmap.praise_photo_button_image);
            }
        }
    }

    public void hidelayout() {
        frame_live_menu.setVisibility(View.INVISIBLE);
        frame_live_chat.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
        rel_layout_bottom_dialog.setVisibility(View.GONE);
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
        if (from.equals(loginInfo.getMbNickname())) {
            tv_likes.setText("你 给主播点了赞");

        } else {
            tv_likes.setText(from + " 给主播点了赞");
            if (temp) {
                new MyTimer(2000, 300).start();
                temp = false;
            }

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

    public void sendGift(final String num) {
        HttpParams params = new HttpParams();
        params.put("mbId", loginInfo.getMbId());
        params.put("anId", anchorItem.getAnId());
        params.put("num", num);
        PostRequest request = OkGo.post(Config.POST_MEMBER_SEND).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", PlayActivity.this);
            }

            @Override
            public void onNext(Response response) {
                try {
                    String Str = response.body().string();
                    JSONObject ob = JSON.parseObject(Str);
                    if (ob.containsKey("code")) {
                        int code = ob.getInteger("code");
                        if (code == 1) {
                            if (ob.containsKey("data")) {
                                int gold = ob.getIntValue("data");
                                loginInfo.setMbGold(gold);
                                tv_live_credits.setText("钻石:" + loginInfo.getMbGold());
                                mF.setMember(loginInfo);
                                aCache.put(ACacheKey.CURRENT_ACCOUNT, mF);
                            }
                            giftPost();

                        } else {
                            ToastUtil.showToast(ob.getString("msg"), context);
                        }
                    }
                } catch (Exception e) {
                    F.e(e.toString());
                }

            }
        });


    }

    public void giftPost() {
        EMMessage message = EMMessage.createTxtSendMessage("[key]" + "Gift" + gift.id, chatroomid);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setFrom(loginInfo.getMbNickname());
        message.setAttribute("Gift", gift.getId());
        message.setAttribute("level", loginInfo.getMbLevel());
        message.setAttribute("user_name", loginInfo.getMbNickname());
        message.setAttribute("user_url", loginInfo.getMbPhoto());
        EMClient.getInstance().chatManager().sendMessage(message);
        showGifts("我", loginInfo.getMbPhoto(), gift);
    }

    @BindView(R.id.tv_play_gift_count)
    TextView mTvGiftCount;
    @BindView(R.id.lin_play_gift_send)
    View linGiftSend;
    @BindView(R.id.img_play_gift_send_icon)
    ImageView img_play_gift_send_icon;
    @BindView(R.id.tv_play_gift_name)
    TextView tv_play_gift_name;

    long GETGIFTTIME = 0;
    int GiftCount = 1;
    String giftId = "";

    public void showGifts(String from, String icon, Gift gift) {

        linGiftSend.setVisibility(View.VISIBLE);
        Glide.with(this).load(null != icon ? Config.BASE + icon : R.drawable.circle_zhubo).asBitmap().centerCrop().into(new BitmapImageViewTarget(img_play_gift_send_icon) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img_play_gift_send_icon.setImageDrawable(circularBitmapDrawable);
            }
        });
        tv_play_gift_name.setText(from);
        if (System.currentTimeMillis() - GETGIFTTIME < 1000) {
            if (giftId.equals(gift.getId())) {
                GiftCount++;
            } else {
                GiftCount = 1;
            }
        } else {
            GiftCount = 1;
        }
        giftId = gift.getId();
        GETGIFTTIME = System.currentTimeMillis();
        Animation a = AnimationUtils.loadAnimation(context, R.anim.scalebig);
        iv_gift.setVisibility(View.VISIBLE);
        Glide.with(context).load(gift.picture).into(new GlideDrawableImageViewTarget(iv_gift) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                Animation b = AnimationUtils.loadAnimation(context, R.anim.scalebig);
                iv_gift.setAnimation(b);
                b.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv_gift.clearAnimation();
                        iv_gift.setVisibility(View.GONE);
                        linGiftSend.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
        mTvGiftCount.setVisibility(View.VISIBLE);
        mTvGiftCount.setText("X" + GiftCount);
        a.setFillAfter(true);
        mTvGiftCount.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvGiftCount.getVisibility() == View.VISIBLE) {
                            F.e("---------------------------------mTvGiftCount GONE");
                            mTvGiftCount.clearAnimation();
                            mTvGiftCount.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
            chatroomid = !isMember ? anchorItem.getChatRoomId() : "0";
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
            mTvLevel.setText(AppUtil.getDJ(anchorItem.getMember().getMbGold()) + "");
            mTvGold.setText(AppUtil.getDJ(anchorItem.getMember().getMbGoldPay()) + "");

        }
        mTvLv.setText("Lv" + anchorItem.getMember().getMbLevel());
        tv_live_credits.setText("钻石:" + loginInfo.getMbGold());
        contentAction();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_content.setHint("(每条弹幕收费2钻石)你想对主播说些什么？");
                } else {
                    et_content.setHint("请输入消息内容");
                }
            }
        });
    }

    @OnClick({R.id.lin_play_gift_panel_bottom, R.id.lin_anchor_info_action_follow, R.id.view_gone, R.id.frame_live_chat, R.id.tv_live_booking_jubao, R.id.bt_live_booking_tochat, R.id.bt_send, R.id.bt_openemoji, R.id.et_content, R.id.bt_live_chat, R.id.bt_live_gifts, R.id.bt_live_sendgift, R.id.layout_live_icon_content})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                if (mCheckBox.isChecked()) {
                    sendGift2("2", true);
                } else {
                    sendMsg();
                }
                break;
            case R.id.bt_openemoji:
                setopenemoji();
                break;
            case R.id.lin_play_gift_panel_bottom:
                if (!isMember) {
                    setviewClick();
                }
                break;
            case R.id.et_content:
                setetClick();
                break;
            case R.id.bt_live_chat:
                setchat();
                break;
            case R.id.tv_live_booking_jubao:
                if (!isMember) {
                    setbookingclose();
                }
                break;
            case R.id.bt_live_gifts:
                setgifts();
                break;
            case R.id.lin_anchor_info_action_follow://关注
                follow();
                break;
            case R.id.frame_live_chat:
                if (!isMember) {
                    setviewClick();
                }
                break;
            case R.id.view_gone:
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
            case R.id.layout_live_icon_content:
                contentAction();
                break;
        }
    }


    public void follow() {
        if (null != anchorItem.getAnId()) {
            HttpParams params = new HttpParams();
            params.put("mbId", loginInfo.getMbId());
            params.put("anId", anchorItem.getAnId());
            PostRequest request = OkGo.post(Config.POST_ANCHOR_MEMBER_fav).params(params);
            HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showToast("请求失败，请检查网络", PlayActivity.this);

                }

                @Override
                public void onNext(Response response) {
                    try {
                        String Str = response.body().string();
                        F.e("-----------------" + Str);
                        JSONObject ob = JSON.parseObject(Str);
                        if (ob.containsKey("code")) {
                            int i = ob.getIntValue("code");
                            if (i == 1) {
                                int a = ob.getInteger("data");
                                ArrayList<String> gs = PApplication.getInstance().getmFList();
                                if (a == 1) {
                                    gs.add(anchorItem.getAnId());
                                    mImgFollow.setImageResource(R.mipmap.praise_photo_button_image2);
                                } else {
                                    mImgFollow.setImageResource(R.mipmap.praise_photo_button_image);
                                    gs.remove(anchorItem.getAnId());
                                }
                                String str = JSON.toJSONString(gs);
                                aCache.put(ACacheKey.CURRENT_FOLLOW, str);
                            }
                        }
                    } catch (Exception e) {
                        F.e(e.toString());
                    }


                }
            });
        } else {
            ToastUtil.showToast("该入口暂不支持关注", this);
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
            userName = loginInfo.getMbPhone() + "";
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
                if (code == 200) {
                    EMClient.getInstance().logout(true);
                    login();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "登录失败，可能是服务器不稳定", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void joinchatroom() {
        Log.e("adad", "startJoinChatRoom..");
        EMClient.getInstance().chatroomManager().joinChatRoom(chatroomid, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(chatroomid);
                if (room != null) {
                    showChatroomToast("join room success : " + room.getName());
                    Log.e("adad", "JoinChatRoom succeed");
                    sendJoinMSG();
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

    public void sendJoinMSG() {
        //TODO 发送加入消息
        EMMessage message = EMMessage.createTxtSendMessage("进来逛逛", chatroomid);
        message.setFrom(loginInfo.getMbNickname());
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("JOIN_CHATROOM", chatroomid);
        message.setAttribute("user_name", loginInfo.getMbNickname());
        message.setAttribute("level", loginInfo.getMbLevel());
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
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
                if (!message.getChatType().equals(EMMessage.ChatType.ChatRoom)) {
                    return;
                }
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getFrom();
                    message_from = username;
                }
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                message_content = txtBody.getMessage();
                final Map<String, Object> map = message.ext();
                String spotType = null;
                if (map.containsKey("Gift")) {
                    message_from = (String) map.get("user_name");
                    final String icon = (String) map.get("user_url");
                    mGetGift = PApplication.getInstance().getGiftObject((String) map.get("Gift"));
//                        mGetGift.picture = (String) map.get("GiftPicture");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showGifts(message_from, icon, mGetGift);
                        }
                    });

                } else if (map.containsKey("DianZan")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLikes(message_from);
                        }
                    });
                } else if (map.containsKey("Recharge")) {
                    // TODO 收费
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Animation a = AnimationUtils.loadAnimation(context, R.anim.scalebig2);
                            mTvToast.setVisibility(View.VISIBLE);
                            mTvToast.setText("主播开启收费模式：" + (String) map.get("Recharge") + "钻石/分钟");
                            a.setFillAfter(true);
                            mTvToast.startAnimation(a);
                            a.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mTvToast.getVisibility() == View.VISIBLE) {
                                                F.e("---------------------------------mTvGiftCount GONE");
                                                mTvToast.clearAnimation();
                                                mTvToast.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            if (null != timer2) {
                                timer2.cancel();
                                timer2 = null;
                                timer2 = new MyTimer2(999999999, 60000, (String) map.get("Recharge"));
                                temp2 = true;
                            } else {
                                timer2 = new MyTimer2(999999999, 60000, (String) map.get("Recharge"));
                                temp2 = true;
                            }
                            if (temp2) {
                                timer2.start();
                                temp2 = false;
                            }
                        }
                    });


                } else if (map.containsKey("DanMu")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doDanmu(message_content);
                        }
                    });

                } else if (map.containsKey("JOIN_CHATROOM")) {
                    //TODO 新人加入聊天室消息
                    message_from = (String) map.get("user_name");
//                    int count = Integer.valueOf(anchorItem.getFaCount()) + 1;
                    anchorItem.setFaCount(getChatRoomInfoCount() + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvCount.setText(anchorItem.getFaCount() + "");
                        }
                    });
                    if (map.containsKey("level")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int level = Integer.valueOf((String) map.get("level"));
                                Animation a = AnimationUtils.loadAnimation(context, R.anim.scalebig2);
                                a.setFillAfter(true);
                                mTvLevel_2.setVisibility(View.VISIBLE);
                                mTvLevel_2.setText(message_from + "加入聊天室");
                                if (level >= 15) {
                                    mTvToast.setVisibility(View.VISIBLE);
                                    mTvToast.setText(message_from + "加入聊天室");
                                    mTvToast.startAnimation(a);
                                }
                                mTvLevel_2.startAnimation(a);
                                a.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mTvToast.getVisibility() == View.VISIBLE) {
                                                    mTvToast.clearAnimation();
                                                    mTvToast.setVisibility(View.GONE);

                                                }
                                                if (mTvLevel_2.getVisibility() == View.VISIBLE) {
                                                    mTvLevel_2.clearAnimation();
                                                    mTvLevel_2.setVisibility(View.GONE);
                                                }

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
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

    public void doDanmu(String message) {

//        SpannableStringBuilder builder = new SpannableStringBuilder(message);
        Pattern pattern = buildPattern();
        Matcher matcher = pattern.matcher(message);
        SpannableString spannableString = new SpannableString(message);
        while (matcher.find()) {
            if (PApplication.emoticonsIdMap.containsKey(matcher.group())) {
                int id = PApplication.emoticonsIdMap.get(matcher.group());

                Drawable drawable = this.getResources().getDrawable(id);
                if (drawable != null) {
                    drawable.setBounds(0, 0, FACE_SIZE, FACE_SIZE);
                    ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
                    spannableString.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(span, matcher.start(), matcher.end(),
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

//        IDanmakuItem item = new DanmakuItem(this, new SpannableString(message), mDanmakuView.getWidth(),0,R.color.colorBlue,0,1);
//        DanmakuItem dm = new DanmakuItem(this, message, mDanmakuView.getWidth());
//        dm.setTextColor(Color.parseColor("#49C3B8"));

//        ImageSpan imageSpan = new ImageSpan(this, R.drawable.em);
//        SpannableString spannableString = new SpannableString(i + msg);
//        spannableString.setSpan(imageSpan, spannableString.length() - 2, spannableString.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        IDanmakuItem item = new DanmakuItem(this, spannableString, mDanmakuView.getWidth(), 0, R.color.colorBlue, 0, 1.5f);
        mDanmakuView.addItem(item);
        mDanmakuView.setVisibility(View.VISIBLE);
        mDanmakuView.show();
    }

    public MyTimer2 timer2;
    private boolean temp = true;

    private class MyTimer extends CountDownTimer {

        private static final String TAG = "MyTimer";

        //millisInFuture为你设置的此次倒计时的总时长，比如60秒就设置为60000
        //countDownInterval为你设置的时间间隔，比如一般为1秒,根据需要自定义。
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
            periscopeLayout.addHeart();
            Log.d(TAG, "111");
        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            temp = true;
        }
    }

    private boolean temp2 = true;

    public class MyTimer2 extends CountDownTimer {

        String sum;
        private static final String TAG = "MyTimer";

        //millisInFuture为你设置的此次倒计时的总时长，比如60秒就设置为60000
        //countDownInterval为你设置的时间间隔，比如一般为1秒,根据需要自定义。
        public MyTimer2(long millisInFuture, long countDownInterval, String sum) {
            super(millisInFuture, countDownInterval);
            this.sum = sum;
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
//            periscopeLayout.addHeart();
            sendGift2(sum, false);
        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            temp2 = true;
        }
    }


    public void sendGift2(final String num, final boolean isDanmu) {
        HttpParams params = new HttpParams();
        params.put("mbId", loginInfo.getMbId());
        params.put("anId", anchorItem.getAnId());
        params.put("num", num);
        PostRequest request = OkGo.post(Config.POST_MEMBER_SEND).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", PlayActivity.this);
            }

            @Override
            public void onNext(Response response) {
                try {
                    String Str = response.body().string();
                    JSONObject ob = JSON.parseObject(Str);
                    if (ob.containsKey("code")) {
                        int code = ob.getInteger("code");
                        if (code == 1) {
                            if (ob.containsKey("data")) {
                                int gold = ob.getIntValue("data");
                                loginInfo.setMbGold(gold);
                                tv_live_credits.setText("钻石:" + loginInfo.getMbGold());
                                mF.setMember(loginInfo);
                                aCache.put(ACacheKey.CURRENT_ACCOUNT, mF);
                            }
                            if (isDanmu) {
                                sendMsg();
                            }
                        } else {
                            if (isDanmu) {
                                ToastUtil.showToast(ob.getString("msg") + "，充值后再发弹幕吧", context);
                            } else {
                                ToastUtil.showToast(ob.getString("msg") + ",请充值后再进入观看", context);
                                PlayActivity.this.finish();
                            }

                        }
                    }
                } catch (Exception e) {
                    F.e(e.toString());
                }

            }
        });
    }


    public void getAnChorInfo(String anId) {
        HttpParams params = new HttpParams();
        params.put("anId", anId);
        PostRequest request = OkGo.post(Config.POST_ANCHOR_GET).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast("请求失败，请检查网络", PlayActivity.this);
            }

            @Override
            public void onNext(Response response) {
                HttpBase<Follow> bf = Parsing.getInstance().ResponseToObject(response, Follow.class);
                anchorItem = bf.getData();
                chatroomid = anchorItem.getChatRoomId();
                if (StringUtils.isNotEmpty(anchorItem.getSysMsg())) {
                    mTvSysToast.setVisibility(View.VISIBLE);
                    mTvSysToast.setText("系统消息：" + anchorItem.getSysMsg());
                    Animation a = AnimationUtils.loadAnimation(context, R.anim.scalebig2);
                    mTvSysToast.startAnimation(a);
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mTvSysToast.getVisibility() == View.VISIBLE) {
                                        F.e("---------------------------------mTvGiftCount GONE");
                                        mTvSysToast.clearAnimation();
                                        mTvSysToast.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                if (StringUtils.isNotEmpty(anchorItem.getWordLimit())) {
                    int maxlenth = Integer.valueOf(anchorItem.getWordLimit());
                    et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlenth)});
                }
                if (null != bf.getData().getFaCount()) {
                    mTvCount.setText(bf.getData().getFaCount());
                }
                if (anchorItem.getLiveStatus().equals("3") && anchorItem.getAnPrice() != 0) {
                    if (null != timer2) {
                        timer2.cancel();
                        timer2 = null;
                        timer2 = new MyTimer2(999999999, 60000, anchorItem.getAnPrice() + "");
                        temp2 = true;
                    } else {
                        timer2 = new MyTimer2(999999999, 60000, anchorItem.getAnPrice() + "");
                        temp2 = true;
                    }
                    if (temp2) {
                        timer2.start();
                        temp2 = false;
                    }
                }

                doPlay();
                //----------------------------------------------------------以下为环信
                //-------------------------------------------------------------------
                login();

            }
        });
    }


    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(
                PApplication.emoticonKeyList.size() * 3);
        patternString.append('(');
        for (int i = 0; i < PApplication.emoticonKeyList.size(); i++) {
            String s = PApplication.emoticonKeyList.get(i);
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    public int getChatRoomInfoCount() {
        try {
            EMChatRoom chatRoom = EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(chatroomid);
            return chatRoom.getMemberCount();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
