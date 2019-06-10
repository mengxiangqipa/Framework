package com.framework.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Ping网络是否通畅
 *
 * @author YobertJomi
 * className PingUtil
 * created at  2018/1/17  15:34
 */
public class PingUtil {
    private static volatile PingUtil singleton;

    private PingUtil() {
    }

    public static PingUtil getInstance() {
        if (singleton == null) {
            synchronized (PingUtil.class) {
                if (singleton == null) {
                    singleton = new PingUtil();
                }
            }
        }
        return singleton;
    }

    public String Ping(String domainNameOrUrl) {
        String resault = "";
        Process p;
        try {
            // ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 100
            // 以秒为单位指定超时间隔，是指超时时间为100秒
            // p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
            p = Runtime.getRuntime().exec("ping -c 3 -w 3 " + domainNameOrUrl);
            int status = p.waitFor();

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());

            if (status == 0) {
                resault = "success";
            } else {
                resault = "faild";
            }
            resault = (resault
                    + "  平均网速："
                    + (TrafficStatsUtil.getInstance().getNetSpeed() / (90000 / 1000)) + "kb/s");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resault;
    }

    public class NetPing extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String s = "";
            s = Ping("www.baidu.com");
//            LogCollectUtil.writeFileToSDSimple("ping网络结果--百度--（超时限制5s）" + s);
            Log.e("yy", "ping网络结果--百度--：" + s);
            return s;
        }
    }
}
