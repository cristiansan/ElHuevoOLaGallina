package app.eggorchicken.eggorchicken;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

/**
 * Created by XXX on 3/13/2015.
 */
public class Common {
    public static void SetFontTextView(Context context, TextView mtextView1, String Text) {
        SpannableString s = new SpannableString(Text);
        s.setSpan(new TypefaceSpan(context, "passionone_bold.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mtextView1.setText(s);
        // mtextView1.setTextColor(Color.parseColor("#40cea9"));
    }

    public static void SetFontTextView(Context context, TextView mtextView1) {
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/passionone_bold.ttf");
        mtextView1.setTypeface(type);
    }
}
