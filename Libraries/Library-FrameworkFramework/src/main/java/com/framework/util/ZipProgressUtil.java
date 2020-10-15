package com.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author YobertJomi
 * className ZipProgressUtil
 * created at  2020/10/15  11:37
 */
public class ZipProgressUtil {

    /***
     * 解压通用方法
     *
     * @param zipFileString
     *            文件路径
     * @param outPathString
     *            解压路径
     * @param listener
     *            加压监听
     */
    public static void unZipFile(final String zipFileString, final String outPathString,
                                 final ZipListener listener) {
        UnZipThread unZipThread = new UnZipThread(zipFileString, outPathString, listener);
        unZipThread.start();
    }

    public interface ZipListener {
        /**
         * 开始解压
         */
        void zipStart();

        /**
         * 解压进度
         *
         * @param progress 解压进度
         */
        void zipProgress(int progress);

        /**
         * 解压成功
         *
         * @param zipName 解压后的名称
         */
        void zipSuccess(String zipName);

        /**
         * 解压失败
         *
         * @param e 解压异常的原因
         */
        void zipFail(Exception e);
    }

    public final static class UnZipThread extends Thread {
        String zipFileString;
        String outPathString;
        ZipListener listener;

        public UnZipThread(String zipFileString, String outPathString,
                           ZipListener listener) {
            this.zipFileString = zipFileString;
            this.outPathString = outPathString;
            this.listener = listener;
        }

        @Override
        public void run() {
            super.run();
            try {
                listener.zipStart();
                long sumLength = 0;
                // 获取解压之后文件的大小,用来计算解压的进度
                long zipLength = getZipTrueSize(zipFileString);
                if (zipLength <= 0 && new File(zipFileString).exists()) {
                    zipLength = new File(zipFileString).length();
                }
                FileInputStream inputStream = new FileInputStream(zipFileString);
                ZipInputStream inZip = new ZipInputStream(inputStream);

                ZipEntry zipEntry;
                String szName;
                String zipName = "";
                while ((zipEntry = inZip.getNextEntry()) != null) {
                    szName = zipEntry.getName();
                    if (zipEntry.isDirectory()) {
                        szName = szName.substring(0, szName.length() - 1);
                        zipName = szName;
                        File folder = new File(outPathString + File.separator + szName);
                        boolean mkdirs = folder.mkdirs();
                        if (!mkdirs) {
                            listener.zipFail(new Exception("创建文件夹失败"));
                            return;
                        }
                    } else {
                        File file = new File(outPathString + File.separator + szName);
                        final boolean success;
                        try {
                            success = file.createNewFile();
                            if (!success) {
                                continue;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                        FileOutputStream out = new FileOutputStream(file);
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = inZip.read(buffer)) != -1) {
                            sumLength += len;
                            int progress = 0;
                            if (zipLength > 0) {
                                progress = (int) ((sumLength * 100) / zipLength);
                            }
                            updateProgress(progress, listener);
                            out.write(buffer, 0, len);
                            out.flush();
                        }
                        out.close();
                    }
                }
                listener.zipSuccess(zipName);
                inZip.close();
            } catch (Exception e) {
                listener.zipFail(e);
            }
        }

        private int lastProgress = 0;

        private void updateProgress(int progress, ZipListener listener) {
            /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 */
            if (progress > lastProgress) {
                lastProgress = progress;
                listener.zipProgress(progress);
            }
        }

        /**
         * 获取压缩包解压后的内存大小
         *
         * @param filePath 文件路径
         * @return 返回内存long类型的值
         */
        public final long getZipTrueSize(String filePath) {
            long size = 0;
            ZipFile f;
            try {
                String fileEncode = EncodeUtil.getEncode(filePath, true);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    f = new ZipFile(new File(filePath), Charset.forName(fileEncode));
                } else {
                    f = new ZipFile(filePath);
                }
                Enumeration<? extends ZipEntry> en = f.entries();
                while (en.hasMoreElements()) {
                    size += en.nextElement().getSize();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return size;
        }
    }
}
