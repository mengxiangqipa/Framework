/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileParserUtils {

    public static final String ee_1 = "[:1]";
    public static final String ee_2 = "[:2]";
    public static final String ee_3 = "[:3]";
    public static final String ee_4 = "[:4]";
    public static final String ee_5 = "[:5]";
    public static final String ee_6 = "[:6]";
    public static final String ee_7 = "[:7]";
    public static final String ee_8 = "[:8]";
    public static final String ee_9 = "[:9]";
    public static final String ee_10 = "[:10]";
    public static final String ee_11 = "[:11]";
    public static final String ee_12 = "[:12]";
    public static final String ee_13 = "[:13]";
    public static final String ee_14 = "[:14]";
    public static final String ee_15 = "[:15]";
    public static final String ee_16 = "[:16]";
    public static final String ee_17 = "[:17]";
    public static final String ee_18 = "[:18]";
    public static final String ee_19 = "[:19]";
    public static final String ee_20 = "[:20]";
    public static final String ee_21 = "[:21]";
    public static final String ee_22 = "[:22]";
    public static final String ee_23 = "[:23]";
    public static final String ee_24 = "[:24]";
    public static final String ee_25 = "[:25]";
    public static final String ee_26 = "[:26]";
    public static final String ee_27 = "[:27]";
    public static final String ee_28 = "[:28]";
    public static final String ee_29 = "[:29]";
    public static final String ee_30 = "[:30]";
    public static final String ee_31 = "[:31]";
    public static final String ee_32 = "[:32]";
    public static final String ee_33 = "[:33]";
    public static final String ee_34 = "[:34]";
    public static final String ee_35 = "[:35]";
    public static final String ee_36 = "[:36]";
    public static final String ee_37 = "[:37]";
    public static final String ee_38 = "[:38]";
    public static final String ee_39 = "[:39]";
    public static final String ee_40 = "[:40]";
    public static final String ee_41 = "[:41]";
    public static final String ee_42 = "[:42]";
    public static final String ee_43 = "[:43]";
    public static final String ee_44 = "[:44]";
    public static final String ee_45 = "[:45]";
    public static final String ee_46 = "[:46]";
    public static final String ee_47 = "[:47]";
    public static final String ee_48 = "[:48]";
    public static final String ee_49 = "[:49]";
    public static final String ee_50 = "[:50]";
    /**
     * 表情string
     */
    public static final String[] mood = new String[]{ee_1, ee_2, ee_3, ee_4,
            ee_5, ee_6, ee_7, ee_8, ee_9, ee_10, ee_11, ee_12, ee_13, ee_14,
            ee_15, ee_16, ee_17, ee_18, ee_19, ee_20, ee_21, ee_22, ee_23,
            ee_24, ee_25, ee_26, ee_27, ee_28, ee_29, ee_30, ee_31, ee_32,
            ee_33, ee_34, ee_35, ee_36, ee_37, ee_38, ee_39, ee_40, ee_41,
            ee_42, ee_43, ee_44, ee_45, ee_46, ee_47, ee_48, ee_49, ee_50};
    /**
     * 表情资源文件
     */
    public static final int[] resDrawable = new int[50];
    // ////////////////
    private static final Factory spannableFactory = Factory.getInstance();
    private static final Map<Pattern, Integer> mapEmotions = new HashMap<Pattern, Integer>();
    //	{
//		R.drawable.bg_mood1,
//			R.drawable.bg_mood2, R.drawable.bg_mood3, R.drawable.bg_mood4,
//			R.drawable.bg_mood5, R.drawable.bg_mood6, R.drawable.bg_mood7,
//			R.drawable.bg_mood8, R.drawable.bg_mood9, R.drawable.bg_mood10,
//			R.drawable.bg_mood11, R.drawable.bg_mood12, R.drawable.bg_mood13,
//			R.drawable.bg_mood14, R.drawable.bg_mood15, R.drawable.bg_mood16,
//			R.drawable.bg_mood17, R.drawable.bg_mood18, R.drawable.bg_mood19,
//			R.drawable.bg_mood20, R.drawable.bg_mood21, R.drawable.bg_mood22,
//			R.drawable.bg_mood23, R.drawable.bg_mood24, R.drawable.bg_mood25,
//			R.drawable.bg_mood26, R.drawable.bg_mood27, R.drawable.bg_mood28,
//			R.drawable.bg_mood29, R.drawable.bg_mood30, R.drawable.bg_mood31,
//			R.drawable.bg_mood32, R.drawable.bg_mood33, R.drawable.bg_mood34,
//			R.drawable.bg_mood35, R.drawable.bg_mood36, R.drawable.bg_mood37,
//			R.drawable.bg_mood38, R.drawable.bg_mood39, R.drawable.bg_mood40,
//			R.drawable.bg_mood41, R.drawable.bg_mood42, R.drawable.bg_mood43,
//			R.drawable.bg_mood44, R.drawable.bg_mood45, R.drawable.bg_mood46,
//			R.drawable.bg_mood47, R.drawable.bg_mood48, R.drawable.bg_mood49,
//			R.drawable.bg_mood50
//	};
    private static volatile SmileParserUtils singleton;

    static {
        int len = mood.length;
        for (int i = 0; i < len; i++) {
            addPattern(mapEmotions, mood[i], resDrawable[i]);
        }
    }

    public static SmileParserUtils getInstance() {
        if (singleton == null) {
            synchronized (SmileParserUtils.class) {
                if (singleton == null) {
                    singleton = new SmileParserUtils();
                }
            }
        }
        return singleton;
    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * <pre>
     * replace existing spannable with smiles
     * @param context
     * @param outPutX x/dp
     * @param outPutY y/dp
     * @param spannable
     * @return
     *
     * <pre/>
     */
    public static boolean addSmiles(Context context, Spannable spannable,
                                    int outPutX, int outPutY) {
        boolean hasChanges = false;
        // MLog.i( "1");
        for (Entry<Pattern, Integer> entry : mapEmotions.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    final int outputX_new = ScreenUtils.getInstance().dip2px(context, outPutX);
                    final int outputY_new = ScreenUtils.getInstance().dip2px(context, outPutY);
                    Drawable drawable = context.getResources().getDrawable(
                            entry.getValue());
                    // MLog.i( "2");
                    // MLog.i( "drawable==null:"+(null==drawable));
                    drawable.setBounds(0, 0, outputX_new, outputY_new);
                    // MLog.i( "3");
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    // MLog.i( "4");
                    spannable.setSpan(imageSpan, matcher.start(),
                            matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // MLog.i( "5");
                    // spannable.setSpan(new ImageSpan(context,
                    // entry.getValue()),
                    // matcher.start(), matcher.end(),
                    // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    /**
     * <pre>
     * @param context
     * @param text CharSequence文本
     * @param outPutX x/dp
     * @param outPutY y/dp
     * @return
     *
     * <pre>
     */
    public static Spannable getSmiledText(Context context, CharSequence text,
                                          int outPutX, int outPutY) {
        try {
            // MLog.i( "getSmiledText1");
            Spannable spannable = spannableFactory.newSpannable(text);
            // MLog.i( "getSmiledText2");
            addSmiles(context, spannable, outPutX, outPutY);
            // MLog.i( "getSmiledText3");
            return spannable;
        } catch (Exception e) {
            // MLog.i( "getSmiledText_e");
            // MLog.i( "getSmiledText_e:"+e.getMessage());
            return null;
        }
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : mapEmotions.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }
}
