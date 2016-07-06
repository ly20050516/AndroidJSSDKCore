/*
 *    Copyright 2016 ifarseer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.farseer.tool;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.*;


/**
 * Assets工具类
 *
 * @author zhaosc
 * @version 1.0.0
 * @since 16/6/19
 */
public class AssetsTool {

    private static final String TAG = AssetsTool.class.getSimpleName();

    public static String getText(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String text = "";
            while ((line = bufReader.readLine()) != null)
                text += line;
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拷贝Asset下文件
     *
     * @param dir      文件所在目录,dir==null表示根目录
     * @param fileName
     * @param outDir
     * @return
     */
    public static boolean copyAssetFile(Context context, String dir, String fileName, String outDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
            AssetManager am = context.getAssets();
            if (dir != null) {
                String[] fileNames = am.list(dir);
                if (fileNames == null) {
                    for (String string : fileNames) {
                        if (string.equals(fileName)) {
                            in = am.open(dir + "/" + fileName);
                            break;
                        }
                    }
                }
            } else {
                in = am.open(fileName);
            }
            File outFile = new File(outDir + fileName);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            LogTool.error(TAG, e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                LogTool.error(TAG, e.getMessage());
            }
        }
        return false;
    }

    /**
     * 拷贝Asset下目录
     *
     * @param assetDir
     * @param outDir
     * @return
     */
    public static boolean copyAssetFile(Context context, String assetDir, String outDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
            AssetManager am = context.getAssets();
            String[] files = am.list(assetDir);
            File outDirFile = new File(outDir);
            if (!outDirFile.exists()) {
                outDirFile.mkdir();
            }
            for (int i = 0; i < files.length; i++) {
                in = am.open(assetDir + "/" + files[i]);
                File outFile = new File(outDir + files[i]);
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            LogTool.error(TAG, e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                LogTool.error(TAG, e.getMessage());
            }
        }
        return false;
    }
}