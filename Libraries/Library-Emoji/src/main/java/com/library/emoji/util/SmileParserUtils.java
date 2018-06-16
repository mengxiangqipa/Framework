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
package com.library.emoji.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.library.emoji.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileParserUtils {

    /**
     * 表情资源文件
     */
    public static final int[] resDrawable1 = new int[]{
            R.drawable.bb_01, R.drawable.bb_02, R.drawable.bb_03, R.drawable.bb_04,
            R.drawable.bb_05, R.drawable.bb_06, R.drawable.bb_07, R.drawable.bb_08,
            R.drawable.bb_09, R.drawable.bb_10, R.drawable.bb_11, R.drawable.bb_12,
            R.drawable.bb_13, R.drawable.bb_14, R.drawable.bb_15, R.drawable.bb_16,
            R.drawable.bb_17, R.drawable.bb_18, R.drawable.bb_19, R.drawable.bb_20,
            R.drawable.bb_21, R.drawable.bb_22, R.drawable.bb_23, R.drawable.bb_24,
            R.drawable.bb_25, R.drawable.bb_26, R.drawable.bb_27, R.drawable.bb_28,
            R.drawable.bb_29, R.drawable.bb_30, R.drawable.bb_31, R.drawable.bb_32,
            R.drawable.bb_33, R.drawable.bb_34, R.drawable.bb_35, R.drawable.bb_36,
            R.drawable.bb_37, R.drawable.bb_38, R.drawable.bb_39, R.drawable.bb_40,
            R.drawable.bb_41, R.drawable.bb_42, R.drawable.bb_43, R.drawable.bb_44,
            R.drawable.bb_45, R.drawable.bb_46, R.drawable.bb_47, R.drawable.bb_48,
            R.drawable.bb_49, R.drawable.bb_50, R.drawable.bb_51, R.drawable.bb_52,
            R.drawable.bb_53, R.drawable.bb_54, R.drawable.bb_55, R.drawable.bb_56,
            R.drawable.bb_57, R.drawable.bb_58, R.drawable.bb_59, R.drawable.bb_60,
            R.drawable.bb_61, R.drawable.bb_62, R.drawable.bb_63, R.drawable.bb_64,
            R.drawable.bb_65, R.drawable.bb_66, R.drawable.bb_67, R.drawable.bb_68,
            R.drawable.bb_69, R.drawable.bb_70, R.drawable.bb_71, R.drawable.bb_72,
            R.drawable.bb_73, R.drawable.bb_74, R.drawable.bb_75, R.drawable.bb_76,
            R.drawable.bb_77, R.drawable.bb_78, R.drawable.bb_79, R.drawable.bb_80
    };
    public static final int[] resDrawable2 = new int[]{
            R.drawable.e00d, R.drawable.e00e, R.drawable.e00f, R.drawable.e010,
            R.drawable.e10b, R.drawable.e011, R.drawable.e11a, R.drawable.e11b,
            R.drawable.e11c, R.drawable.e14c, R.drawable.e022, R.drawable.e22e,
            R.drawable.e22f, R.drawable.e023, R.drawable.e32e, R.drawable.e40a,
            R.drawable.e40b, R.drawable.e40c, R.drawable.e40d, R.drawable.e40e,
            R.drawable.e40f, R.drawable.e41d, R.drawable.e41e, R.drawable.e41f,
            R.drawable.e056, R.drawable.e057, R.drawable.e058, R.drawable.e059,
            R.drawable.e105, R.drawable.e106, R.drawable.e107, R.drawable.e108,
            R.drawable.e111, R.drawable.e327, R.drawable.e328, R.drawable.e329,
            R.drawable.e330, R.drawable.e331, R.drawable.e332, R.drawable.e333,
            R.drawable.e334, R.drawable.e335, R.drawable.e336, R.drawable.e337,
            R.drawable.e401, R.drawable.e402, R.drawable.e403, R.drawable.e404,
            R.drawable.e405, R.drawable.e406, R.drawable.e407, R.drawable.e408,
            R.drawable.e409, R.drawable.e410, R.drawable.e411, R.drawable.e412,
            R.drawable.e413, R.drawable.e414, R.drawable.e415, R.drawable.e416,
            R.drawable.e417, R.drawable.e418, R.drawable.e420, R.drawable.e421,
            R.drawable.e422, R.drawable.e423, R.drawable.e424, R.drawable.e425,
            R.drawable.e426, R.drawable.e427, R.drawable.e428, R.drawable.e537
    };
    public static final int[] resDrawable3 = new int[]{
            R.drawable.emoji_1f3a4, R.drawable.emoji_1f3b5, R.drawable.emoji_1f3b8, R.drawable.emoji_1f3c0,
            R.drawable.emoji_1f3c6, R.drawable.emoji_1f4a3, R.drawable.emoji_1f4a4, R.drawable.emoji_1f4b0,
            R.drawable.emoji_1f6b2, R.drawable.emoji_1f37a, R.drawable.emoji_1f40d, R.drawable.emoji_1f41b,
            R.drawable.emoji_1f42d, R.drawable.emoji_1f42e, R.drawable.emoji_1f42f, R.drawable.emoji_1f45c,
            R.drawable.emoji_1f47c, R.drawable.emoji_1f48d, R.drawable.emoji_1f49d, R.drawable.emoji_1f52b,
            R.drawable.emoji_1f302, R.drawable.emoji_1f319, R.drawable.emoji_1f339, R.drawable.emoji_1f382,
            R.drawable.emoji_1f384, R.drawable.emoji_1f419, R.drawable.emoji_1f420, R.drawable.emoji_1f427,
            R.drawable.emoji_1f428, R.drawable.emoji_1f433, R.drawable.emoji_1f435, R.drawable.emoji_1f436,
            R.drawable.emoji_1f438, R.drawable.emoji_1f444, R.drawable.emoji_1f451, R.drawable.emoji_1f459,
            R.drawable.emoji_1f466, R.drawable.emoji_1f467, R.drawable.emoji_1f468, R.drawable.emoji_1f469,
            R.drawable.emoji_1f484, R.drawable.emoji_1f525, R.drawable.emoji_1f680, R.drawable.emoji_1f684,
            R.drawable.emoji_2b50, R.drawable.emoji_26a1, R.drawable.emoji_26bd, R.drawable.emoji_2600,
            R.drawable.emoji_2601, R.drawable.emoji_2614, R.drawable.emoji_2615, R.drawable.emoji_2708,
            R.drawable.emoji_2744, R.drawable.emoji_unknow
    };
    public static final int[] resDrawable4 = new int[]{
            R.drawable.f000, R.drawable.f001, R.drawable.f002, R.drawable.f003, R.drawable.f004,
            R.drawable.f005, R.drawable.f006, R.drawable.f007, R.drawable.f008, R.drawable.f009,
            R.drawable.f010, R.drawable.f011, R.drawable.f012, R.drawable.f013, R.drawable.f014,
            R.drawable.f015, R.drawable.f016, R.drawable.f017, R.drawable.f018, R.drawable.f019,
            R.drawable.f020, R.drawable.f021, R.drawable.f022, R.drawable.f023, R.drawable.f024,
            R.drawable.f025, R.drawable.f026, R.drawable.f027, R.drawable.f028, R.drawable.f029,
            R.drawable.f030, R.drawable.f031, R.drawable.f032, R.drawable.f033, R.drawable.f034,
            R.drawable.f035, R.drawable.f036, R.drawable.f038, R.drawable.f038, R.drawable.f039,
            R.drawable.f040, R.drawable.f041, R.drawable.f042, R.drawable.f043, R.drawable.f044,
            R.drawable.f045, R.drawable.f046, R.drawable.f047, R.drawable.f048, R.drawable.f049,
            R.drawable.f050, R.drawable.f051, R.drawable.f052, R.drawable.f053, R.drawable.f054,
            R.drawable.f055, R.drawable.f056, R.drawable.f057, R.drawable.f058, R.drawable.f059,
            R.drawable.f060, R.drawable.f061, R.drawable.f062, R.drawable.f063, R.drawable.f064,
            R.drawable.f065, R.drawable.f066, R.drawable.f067, R.drawable.f068, R.drawable.f069,
            R.drawable.f070, R.drawable.f071, R.drawable.f072, R.drawable.f073, R.drawable.f074,
            R.drawable.f075, R.drawable.f076, R.drawable.f077, R.drawable.f078, R.drawable.f079,
            R.drawable.f080, R.drawable.f081, R.drawable.f082, R.drawable.f083, R.drawable.f084,
            R.drawable.f085, R.drawable.f086, R.drawable.f087, R.drawable.f088, R.drawable.f089,
            R.drawable.f090, R.drawable.f091, R.drawable.f092, R.drawable.f093, R.drawable.f094,
            R.drawable.f095, R.drawable.f096, R.drawable.f097, R.drawable.f098, R.drawable.f099,
            R.drawable.f100, R.drawable.f101, R.drawable.f102, R.drawable.f103, R.drawable.f104,
            R.drawable.f105, R.drawable.f106
    };
    private static final String bb_1 = "[:bb1]";
    private static final String bb_2 = "[:bb2]";
    private static final String bb_3 = "[:bb3]";
    private static final String bb_4 = "[:bb4]";
    private static final String bb_5 = "[:bb5]";
    private static final String bb_6 = "[:bb6]";
    private static final String bb_7 = "[:bb7]";
    private static final String bb_8 = "[:bb8]";
    private static final String bb_9 = "[:bb9]";
    private static final String bb_10 = "[:bb10]";
    private static final String bb_11 = "[:bb11]";
    private static final String bb_12 = "[:bb12]";
    private static final String bb_13 = "[:bb13]";
    private static final String bb_14 = "[:bb14]";
    private static final String bb_15 = "[:bb15]";
    private static final String bb_16 = "[:bb16]";
    private static final String bb_17 = "[:bb17]";
    private static final String bb_18 = "[:bb18]";
    private static final String bb_19 = "[:bb19]";
    private static final String bb_20 = "[:bb20]";
    private static final String bb_21 = "[:bb21]";
    private static final String bb_22 = "[:bb22]";
    private static final String bb_23 = "[:bb23]";
    private static final String bb_24 = "[:bb24]";
    private static final String bb_25 = "[:bb25]";
    private static final String bb_26 = "[:bb26]";
    private static final String bb_27 = "[:bb27]";
    private static final String bb_28 = "[:bb28]";
    private static final String bb_29 = "[:bb29]";
    private static final String bb_30 = "[:bb30]";
    private static final String bb_31 = "[:bb31]";
    private static final String bb_32 = "[:bb32]";
    private static final String bb_33 = "[:bb33]";
    private static final String bb_34 = "[:bb34]";
    private static final String bb_35 = "[:bb35]";
    private static final String bb_36 = "[:bb36]";
    private static final String bb_37 = "[:bb37]";
    private static final String bb_38 = "[:bb38]";
    private static final String bb_39 = "[:bb39]";
    private static final String bb_40 = "[:bb40]";
    private static final String bb_41 = "[:bb41]";
    private static final String bb_42 = "[:bb42]";
    private static final String bb_43 = "[:bb43]";
    private static final String bb_44 = "[:bb44]";
    private static final String bb_45 = "[:bb45]";
    private static final String bb_46 = "[:bb46]";
    private static final String bb_47 = "[:bb47]";
    private static final String bb_48 = "[:bb48]";
    private static final String bb_49 = "[:bb49]";
    private static final String bb_50 = "[:bb50]";
    private static final String bb_51 = "[:bb51]";
    private static final String bb_52 = "[:bb52]";
    private static final String bb_53 = "[:bb53]";
    private static final String bb_54 = "[:bb54]";
    private static final String bb_55 = "[:bb55]";
    private static final String bb_56 = "[:bb56]";
    private static final String bb_57 = "[:bb57]";
    private static final String bb_58 = "[:bb58]";
    private static final String bb_59 = "[:bb59]";
    private static final String bb_60 = "[:bb60]";
    private static final String bb_61 = "[:bb61]";
    private static final String bb_62 = "[:bb62]";
    private static final String bb_63 = "[:bb63]";
    private static final String bb_64 = "[:bb64]";
    private static final String bb_65 = "[:bb65]";
    private static final String bb_66 = "[:bb66]";
    private static final String bb_67 = "[:bb67]";
    private static final String bb_68 = "[:bb68]";
    private static final String bb_69 = "[:bb69]";
    private static final String bb_70 = "[:bb70]";
    private static final String bb_71 = "[:bb71]";
    private static final String bb_72 = "[:bb72]";
    private static final String bb_73 = "[:bb73]";
    private static final String bb_74 = "[:bb74]";
    private static final String bb_75 = "[:bb75]";
    private static final String bb_76 = "[:bb76]";
    private static final String bb_77 = "[:bb77]";
    private static final String bb_78 = "[:bb78]";
    private static final String bb_79 = "[:bb79]";
    private static final String bb_80 = "[:bb80]";
    /**
     * 表情string
     */
    public static final String[] mood1 = new String[]{
            bb_1, bb_2, bb_3, bb_4, bb_5, bb_6, bb_7, bb_8, bb_9, bb_10,
            bb_11, bb_12, bb_13, bb_14, bb_15, bb_16, bb_17, bb_18, bb_19, bb_20,
            bb_21, bb_22, bb_23, bb_24, bb_25, bb_26, bb_27, bb_28, bb_29, bb_30,
            bb_31, bb_32, bb_33, bb_34, bb_35, bb_36, bb_37, bb_38, bb_39, bb_40,
            bb_41, bb_42, bb_43, bb_44, bb_45, bb_46, bb_47, bb_48, bb_49, bb_50,
            bb_51, bb_52, bb_53, bb_54, bb_55, bb_56, bb_57, bb_58, bb_59, bb_60,
            bb_61, bb_62, bb_63, bb_64, bb_65, bb_66, bb_67, bb_68, bb_69, bb_70,
            bb_71, bb_72, bb_73, bb_74, bb_75, bb_76, bb_77, bb_78, bb_79, bb_80};
    private static final String e00d = "[:e00d]";
    private static final String e00e = "[:e00e]";
    private static final String e00f = "[:e00f]";
    private static final String e010 = "[:e010]";
    private static final String e10b = "[:e10b]";
    private static final String e011 = "[:e011]";
    private static final String e11a = "[:e11a]";
    private static final String e11b = "[:e11b]";
    private static final String e11c = "[:e11c]";
    private static final String e14c = "[:e14c]";
    private static final String e022 = "[:e022]";
    private static final String e22e = "[:e22e]";
    private static final String e22f = "[:e22f]";
    private static final String e023 = "[:e023]";
    private static final String e32e = "[:e32e]";
    private static final String e40a = "[:e40a]";
    private static final String e40b = "[:e40b]";
    private static final String e40c = "[:e40c]";
    private static final String e40d = "[:e40d]";
    private static final String e40e = "[:e40e]";
    private static final String e40f = "[:e40f]";
    private static final String e41d = "[:e41d]";
    private static final String e41e = "[:e41e]";
    private static final String e41f = "[:e41f]";
    private static final String e056 = "[:e056]";
    private static final String e057 = "[:e057]";
    private static final String e058 = "[:e058]";
    private static final String e059 = "[:e059]";
    private static final String e105 = "[:e105]";
    private static final String e106 = "[:e106]";
    private static final String e107 = "[:e107]";
    private static final String e108 = "[:e108]";
    private static final String e111 = "[:e111]";
    private static final String e327 = "[:e327]";
    private static final String e328 = "[:e328]";
    private static final String e329 = "[:e329]";
    private static final String e330 = "[:e330]";
    private static final String e331 = "[:e331]";
    private static final String e332 = "[:e332]";
    private static final String e333 = "[:e333]";
    private static final String e334 = "[:e334]";
    private static final String e335 = "[:e335]";
    private static final String e336 = "[:e336]";
    private static final String e337 = "[:e337]";
    private static final String e401 = "[:e401]";
    private static final String e402 = "[:e402]";
    private static final String e403 = "[:e403]";
    private static final String e404 = "[:e404]";
    private static final String e405 = "[:e405]";
    private static final String e406 = "[:e406]";
    private static final String e407 = "[:e407]";
    private static final String e408 = "[:e408]";
    private static final String e409 = "[:e409]";
    private static final String e410 = "[:e410]";
    private static final String e411 = "[:e411]";
    private static final String e412 = "[:e412]";
    private static final String e413 = "[:e413]";
    private static final String e414 = "[:e414]";
    private static final String e415 = "[:e415]";
    private static final String e416 = "[:e416]";
    private static final String e417 = "[:e417]";
    private static final String e418 = "[:e418]";
    private static final String e420 = "[:e420]";
    private static final String e421 = "[:e421]";
    private static final String e422 = "[:e422]";
    private static final String e423 = "[:e423]";
    private static final String e424 = "[:e424]";
    private static final String e425 = "[:e425]";
    private static final String e426 = "[:e426]";
    private static final String e427 = "[:e427]";
    private static final String e428 = "[:e428]";
    private static final String e537 = "[:e537]";
    public static final String[] mood2 = new String[]{
            e00d, e00e, e00f, e010, e10b, e011, e11a, e11b, e11c, e14c, e022, e22e, e22f, e023,
            e32e, e40a, e40b, e40c, e40d, e40e, e40f, e41d, e41e, e41f, e056, e057, e058, e059,
            e105, e106, e107, e108, e111, e327, e328, e329, e330, e331, e332, e333, e334, e335,
            e336, e337, e401, e402, e403, e404, e405, e406, e407, e408, e409, e410, e411, e412,
            e413, e414, e415, e416, e417, e418, e420, e421, e422, e423, e424, e425, e426, e427,
            e428, e537};
    private static final String emoji_1f3a4 = "[:emoji_1f3a4]";
    private static final String emoji_1f3b5 = "[:emoji_1f3b5]";
    private static final String emoji_1f3c0 = "[:emoji_1f3c0]";
    private static final String emoji_1f3c6 = "[:emoji_1f3c6]";
    private static final String emoji_1f4a3 = "[:emoji_1f4a3]";
    private static final String emoji_1f4a4 = "[:emoji_1f4a4]";
    private static final String emoji_1f4b0 = "[:emoji_1f4b0]";
    private static final String emoji_1f6b2 = "[:emoji_1f6b2]";
    private static final String emoji_1f37a = "[:emoji_1f37a]";
    private static final String emoji_1f40d = "[:emoji_1f40d]";
    private static final String emoji_1f41b = "[:emoji_1f41b]";
    private static final String emoji_1f42d = "[:emoji_1f42d]";
    private static final String emoji_1f42e = "[:emoji_1f42e]";
    private static final String emoji_1f42f = "[:emoji_1f42f]";
    private static final String emoji_1f45c = "[:emoji_1f45c]";
    private static final String emoji_1f47c = "[:emoji_1f47c]";
    private static final String emoji_1f48d = "[:emoji_1f48d]";
    private static final String emoji_1f49d = "[:emoji_1f49d]";
    private static final String emoji_1f52b = "[:emoji_1f52b]";
    private static final String emoji_1f302 = "[:emoji_1f302]";
    private static final String emoji_1f319 = "[:emoji_1f319]";
    private static final String emoji_1f339 = "[:emoji_1f339]";
    private static final String emoji_1f382 = "[:emoji_1f382]";
    private static final String emoji_1f384 = "[:emoji_1f384]";
    private static final String emoji_1f427 = "[:emoji_1f427]";
    private static final String emoji_1f428 = "[:emoji_1f428]";
    private static final String emoji_1f433 = "[:emoji_1f433]";
    private static final String emoji_1f435 = "[:emoji_1f435]";
    private static final String emoji_1f436 = "[:emoji_1f436]";
    private static final String emoji_1f438 = "[:emoji_1f438]";
    private static final String emoji_1f444 = "[:emoji_1f444]";
    private static final String emoji_1f451 = "[:emoji_1f451]";
    private static final String emoji_1f459 = "[:emoji_1f459]";
    private static final String emoji_1f466 = "[:emoji_1f466]";
    private static final String emoji_1f467 = "[:emoji_1f467]";
    private static final String emoji_1f468 = "[:emoji_1f468]";
    private static final String emoji_1f469 = "[:emoji_1f469]";
    private static final String emoji_1f484 = "[:emoji_1f484]";
    private static final String emoji_1f525 = "[:emoji_1f525]";
    private static final String emoji_1f680 = "[:emoji_1f680]";
    private static final String emoji_1f684 = "[:emoji_1f684]";
    private static final String emoji_2b50 = "[:emoji_2b50]";
    private static final String emoji_26a1 = "[:emoji_26a1]";
    private static final String emoji_26bd = "[:emoji_26bd]";
    private static final String emoji_2600 = "[:emoji_2600]";
    private static final String emoji_2601 = "[:emoji_2601]";
    private static final String emoji_2614 = "[:emoji_2614]";
    private static final String emoji_2615 = "[:emoji_2615]";
    private static final String emoji_2708 = "[:emoji_2708]";
    private static final String emoji_2744 = "[:emoji_2744]";
    private static final String emoji_unknow = "[:emoji_unknow]";
    public static final String[] mood3 = new String[]{
            emoji_1f3a4, emoji_1f3b5, emoji_1f3c0, emoji_1f3c6, emoji_1f4a3, emoji_1f4a4,
            emoji_1f4b0, emoji_1f6b2, emoji_1f37a, emoji_1f40d, emoji_1f41b, emoji_1f42d,
            emoji_1f42e, emoji_1f42f, emoji_1f45c, emoji_1f47c, emoji_1f48d, emoji_1f49d,
            emoji_1f52b, emoji_1f302, emoji_1f319, emoji_1f339, emoji_1f382, emoji_1f384,
            emoji_1f427, emoji_1f428, emoji_1f433, emoji_1f435, emoji_1f436, emoji_1f438,
            emoji_1f444, emoji_1f451, emoji_1f459, emoji_1f466, emoji_1f467, emoji_1f468,
            emoji_1f469, emoji_1f484, emoji_1f525, emoji_1f680, emoji_1f684, emoji_2b50,
            emoji_26a1, emoji_26bd, emoji_2600, emoji_2601, emoji_2614, emoji_2615,
            emoji_2708, emoji_2744, emoji_unknow};
    private static final String f000 = "[:f000]";
    private static final String f001 = "[:f001]";
    private static final String f002 = "[:f002]";
    private static final String f003 = "[:f003]";
    private static final String f004 = "[:f004]";
    private static final String f005 = "[:f005]";
    private static final String f006 = "[:f006]";
    private static final String f007 = "[:f007]";
    private static final String f008 = "[:f008]";
    private static final String f009 = "[:f009]";
    private static final String f010 = "[:f010]";
    private static final String f011 = "[:f011]";
    private static final String f012 = "[:f012]";
    private static final String f013 = "[:f013]";
    private static final String f014 = "[:f014]";
    private static final String f015 = "[:f015]";
    private static final String f016 = "[:f016]";
    private static final String f017 = "[:f017]";
    private static final String f018 = "[:f018]";
    private static final String f019 = "[:f019]";
    private static final String f020 = "[:f020]";
    private static final String f021 = "[:f021]";
    private static final String f022 = "[:f022]";
    private static final String f023 = "[:f023]";
    private static final String f024 = "[:f024]";
    private static final String f025 = "[:f025]";
    private static final String f026 = "[:f026]";
    private static final String f027 = "[:f027]";
    private static final String f028 = "[:f028]";
    private static final String f029 = "[:f029]";
    private static final String f030 = "[:f030]";
    private static final String f031 = "[:f031]";
    private static final String f032 = "[:f032]";
    private static final String f033 = "[:f033]";
    private static final String f034 = "[:f034]";
    private static final String f035 = "[:f035]";
    private static final String f036 = "[:f036]";
    private static final String f037 = "[:f037]";
    private static final String f038 = "[:f038]";
    private static final String f039 = "[:f039]";
    private static final String f040 = "[:f040]";
    private static final String f041 = "[:f041]";
    private static final String f042 = "[:f042]";
    private static final String f043 = "[:f043]";
    private static final String f044 = "[:f044]";
    private static final String f045 = "[:f045]";
    private static final String f046 = "[:f046]";
    private static final String f047 = "[:f047]";
    private static final String f048 = "[:f048]";
    private static final String f049 = "[:f049]";
    private static final String f050 = "[:f050]";
    private static final String f051 = "[:f051]";
    private static final String f052 = "[:f052]";
    private static final String f053 = "[:f053]";
    private static final String f054 = "[:f054]";
    private static final String f055 = "[:f055]";
    private static final String f056 = "[:f056]";
    private static final String f057 = "[:f057]";
    private static final String f058 = "[:f058]";
    private static final String f059 = "[:f059]";
    private static final String f060 = "[:f060]";
    private static final String f061 = "[:f061]";
    private static final String f062 = "[:f062]";
    private static final String f063 = "[:f063]";
    private static final String f064 = "[:f064]";
    private static final String f065 = "[:f065]";
    private static final String f066 = "[:f066]";
    private static final String f067 = "[:f067]";
    private static final String f068 = "[:f068]";
    private static final String f069 = "[:f069]";
    private static final String f070 = "[:f070]";
    private static final String f071 = "[:f071]";
    private static final String f072 = "[:f072]";
    private static final String f073 = "[:f073]";
    private static final String f074 = "[:f074]";
    private static final String f075 = "[:f075]";
    private static final String f076 = "[:f076]";
    private static final String f077 = "[:f077]";
    private static final String f078 = "[:f078]";
    private static final String f079 = "[:f079]";
    private static final String f080 = "[:f080]";
    private static final String f081 = "[:f081]";
    private static final String f082 = "[:f082]";
    private static final String f083 = "[:f083]";
    private static final String f084 = "[:f084]";
    private static final String f085 = "[:f085]";
    private static final String f086 = "[:f086]";
    private static final String f087 = "[:f087]";
    private static final String f088 = "[:f088]";
    private static final String f089 = "[:f089]";
    private static final String f090 = "[:f090]";
    private static final String f091 = "[:f091]";
    private static final String f092 = "[:f092]";
    private static final String f093 = "[:f093]";
    private static final String f094 = "[:f094]";
    private static final String f095 = "[:f095]";
    private static final String f096 = "[:f096]";
    private static final String f097 = "[:f097]";
    private static final String f098 = "[:f098]";
    private static final String f099 = "[:f099]";
    private static final String f100 = "[:f100]";
    private static final String f101 = "[:f101]";
    // ////////////////
    private static final String f102 = "[:f102]";
    private static final String f103 = "[:f103]";
    private static final String f104 = "[:f104]";
    private static final String f105 = "[:f105]";
    private static final String f106 = "[:f106]";
    public static final String[] mood4 = new String[]{
            f000, f001, f002, f003, f004, f005, f006, f007, f008, f009, f010,
            f011, f012, f013, f014, f015, f016, f017, f018, f019, f020,
            f021, f022, f023, f024, f025, f026, f027, f028, f029, f030,
            f031, f032, f033, f034, f035, f036, f037, f038, f039, f040,
            f041, f042, f043, f044, f045, f046, f047, f048, f049, f050,
            f051, f052, f053, f054, f055, f056, f057, f058, f059, f060,
            f061, f062, f063, f064, f065, f066, f067, f068, f069, f070,
            f071, f072, f073, f074, f075, f076, f077, f078, f079, f080,
            f081, f082, f083, f084, f085, f086, f087, f088, f089, f090,
            f091, f092, f093, f094, f095, f096, f097, f098, f099, f100,
            f101, f102, f103, f104, f105, f106};
    private static final Factory spannableFactory = Factory.getInstance();
    private static final Map<Pattern, Integer> mapEmotions = new HashMap<Pattern, Integer>();
    private static volatile SmileParserUtils singleton;

    static {
        int len1 = mood1.length;
        for (int i = 0; i < len1; i++) {
            addPattern(mapEmotions, mood1[i], resDrawable1[i]);
        }
        int len2 = mood2.length;
        for (int i = 0; i < len2; i++) {
            addPattern(mapEmotions, mood2[i], resDrawable2[i]);
        }
        int len3 = mood3.length;
        for (int i = 0; i < len3; i++) {
            addPattern(mapEmotions, mood3[i], resDrawable3[i]);
        }
        int len4 = mood4.length;
        for (int i = 0; i < len4; i++) {
            addPattern(mapEmotions, mood4[i], resDrawable4[i]);
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
    public boolean addSmiles(Context context, Spannable spannable,
                             int outPutX, int outPutY) {
        boolean hasChanges = false;

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
                    final int outputX_new = dip2px(context, outPutX);
                    final int outputY_new = dip2px(context, outPutY);
                    Drawable drawable = context.getResources().getDrawable(
                            entry.getValue());
                    drawable.setBounds(0, 0, outputX_new, outputY_new);
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    spannable.setSpan(imageSpan, matcher.start(),
                            matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    public Spannable getSmiledText(Context context, CharSequence text,
                                   int outPutX, int outPutY) {
        try {
            Spannable spannable = spannableFactory.newSpannable(text);
            addSmiles(context, spannable, outPutX, outPutY);
            return spannable;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean containsKey(String key) {
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return px值
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
