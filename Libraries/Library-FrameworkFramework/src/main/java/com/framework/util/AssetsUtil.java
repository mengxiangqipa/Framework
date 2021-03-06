package com.framework.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author YobertJomi
 * className AssetsUtil
 * created at  2017/8/4  11:29
 */
public class AssetsUtil {
    private static volatile AssetsUtil singleton;

    private AssetsUtil() {
    }

    public static AssetsUtil getInstance() {
        if (singleton == null) {
            synchronized (AssetsUtil.class) {
                if (singleton == null) {
                    singleton = new AssetsUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 以下为直接从assets读取 代码
     *
     * @param fileName
     * @return
     */
    public String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null)
                stringBuilder.append(line);
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
