package cn.gtgs.base.playpro.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gtgs.base.playpro.PApplication;

/**
 * Created by  on 2016/10/19.
 */


public class EmoticonsTextView extends TextView {

    private int faceSize = 14;

    public EmoticonsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFacePxSize(18);
    }

    public void setFaceSize(int faceSize) {
        setFacePxSize(faceSize);
    }
    private void setFacePxSize(int faceDpSize){
        this.faceSize = (int) (0.5F + this.getResources().getDisplayMetrics().density * faceDpSize);
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text), type);
        } else {
            super.setText(text, type);
        }
    }

    private CharSequence replace(CharSequence text) {
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Pattern pattern = buildPattern();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                if (PApplication.emoticonsIdMap.containsKey(matcher.group())) {
                    int id = PApplication.emoticonsIdMap.get(matcher.group());

                    Drawable drawable = this.getResources().getDrawable(id);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, faceSize, faceSize);
                        ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);

                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            return builder.append(" ");
        } catch (Exception e) {
            return text;
        }
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
}
