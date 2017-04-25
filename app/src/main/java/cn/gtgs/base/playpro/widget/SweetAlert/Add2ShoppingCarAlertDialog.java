package cn.gtgs.base.playpro.widget.SweetAlert;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.gtgs.base.OTO.R;
import cn.gtgs.base.OTO.activity.home.model.Attribute;
import cn.gtgs.base.OTO.activity.home.model.ProductGoods;
import cn.gtgs.base.OTO.activity.home.model.Variations;
import cn.gtgs.base.OTO.utils.StringUtils;
import cn.gtgs.base.OTO.widget.AmountView;
import cn.gtgs.base.OTO.widget.AttributeView;

public class Add2ShoppingCarAlertDialog extends Dialog implements View.OnClickListener {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private AnimationSet mErrorXInAnim;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;
    private ProductGoods mGoods;
    private LinearLayout mLayoutAttr;
    HashMap<String, AttributeView> AttributeViews = new HashMap<>();
    private ImageView mImgIcon;
    TextView mTvName;
    TextView mTvPrice;
    AmountView mAmount;

    public static interface OnSweetClickListener {
        public void onClick(Add2ShoppingCarAlertDialog sweetAlertDialog);
    }

    public Add2ShoppingCarAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public Add2ShoppingCarAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog_1);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (; idx < childAnims.size(); idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            Add2ShoppingCarAlertDialog.super.cancel();
                        } else {
                            Add2ShoppingCarAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add2shoppingcar_dialog);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mLayoutAttr = (LinearLayout) findViewById(R.id.lin_shoppingcar_dialog_content);
        mImgIcon = (ImageView) findViewById(R.id.img_layout_buy_icon);
        mTvName = (TextView) findViewById(R.id.tv_layout_buy_name);
        mTvPrice = (TextView) findViewById(R.id.tv_layout_buy_price);
        mAmount = (AmountView) findViewById(R.id.amount_layout_buy_count);
        if (null != mGoods) {
            mTvName.setText(mGoods.getName());
            mTvPrice.setText("$ " + mGoods.getPrice());
            for (Attribute attribute : mGoods.getAttributes()) {
                setAttribute(attribute.getName(), attribute.getOptions(), null);
            }
        }
        findViewById(R.id.btn_buy_submit).setOnClickListener(this);
        findViewById(R.id.img_cancel).setOnClickListener(this);
    }

    public void initGoods(ProductGoods goods) {
        this.mGoods = goods;

    }

    public void setAttribute(String title, ArrayList<String> labs, String selected) {
        AttributeView attribute = new AttributeView(this.getContext());
        attribute.init(title, labs, selected);
        mLayoutAttr.addView(attribute);
        AttributeViews.put(title, attribute);
    }

    public String getChooseAttribute() {
        String s = "";
        for (int i = 0; i < mGoods.getAttributes().size(); i++) {
            Attribute attribute = getAttributeByPosition(i + "");
            if (null != attribute) {
                AttributeView attributeView = AttributeViews.get(attribute.getName());
                if (StringUtils.isEmpty(attributeView.getSelected())) {
                    return null;
                } else {
                    if (i == 0) {
                        s += attributeView.getSelected();
                    } else {
                        s = s + "," + attributeView.getSelected();
                    }
                }
            } else {
                return null;
            }
        }

        for (Variations variations : mGoods.getVariations()) {
            if (variations.getAttributes().equals(s)) {
                return variations.getId();
            }
        }
        return null;
    }

    private Attribute getAttributeByPosition(String position) {
        for (Attribute attribute : mGoods.getAttributes()) {
            if (attribute.getPosition().equals(position)) {
                return attribute;
            }
        }
        return null;
    }

    public int getChooseCount() {
        return mAmount.getAmount();
    }


    public Add2ShoppingCarAlertDialog setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public Add2ShoppingCarAlertDialog setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancel_button:
                if (mCancelClickListener != null) {
                    mCancelClickListener.onClick(Add2ShoppingCarAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }
                break;
            case R.id.confirm_button:
                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClick(Add2ShoppingCarAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }
                break;
            case R.id.btn_buy_submit:
                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClick(Add2ShoppingCarAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }
                break;
            case R.id.img_cancel:
                if (mCancelClickListener != null) {
                    mCancelClickListener.onClick(Add2ShoppingCarAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }
                break;
        }
    }

}