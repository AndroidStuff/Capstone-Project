package mx.com.labuena.tortillas.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class SpannableTextUtils {

    public static final String SPAN_SEPARATOR = " | ";

    public static void addTextWithSpannableString(TextView textView, String text1, String text2,
                                                  final int linkColor,
                                                  final ClickableLinkCallback onLink1Clicked,
                                                  final ClickableLinkCallback onLink2Clicked) {

        String finalText = StringUtils.join(text1, SPAN_SEPARATOR, text2);

        if (textView != null) {
            SpannableString spannableString = new SpannableString(finalText);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    onLink1Clicked.onClick();
                }

                @Override
                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(linkColor);
                }
            };

            spannableString.setSpan(clickableSpan, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString spannableString2 = new SpannableString(finalText);
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    onLink2Clicked.onClick();
                }

                @Override
                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(linkColor);
                }
            };

            spannableString2.setSpan(clickableSpan2, text1.length() + SPAN_SEPARATOR.length(),
                    finalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(finalText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(Color.TRANSPARENT);

        }
    }

    public static void addTextWithSpannableString(TextView textView, String text, int spanStart, int spanEnd,
                                                  final int linkColor, final ClickableLinkCallback onLinkClicked) {

        if (textView != null) {
            SpannableString spannableString = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    onLinkClicked.onClick();
                }

                @Override
                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(linkColor);
                }
            };

            spannableString.setSpan(clickableSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(spannableString);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(Color.TRANSPARENT);

        }
    }

    public interface ClickableLinkCallback {
        void onClick();
    }
}
