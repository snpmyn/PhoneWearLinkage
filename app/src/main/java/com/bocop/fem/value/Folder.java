package com.bocop.fem.value;

import android.os.Environment;

/**
 * Created on 2018/11/14.
 *
 * @author 郑少鹏
 * @desc 文件夹
 */
public class Folder {
    /**
     * 外部存储目录绝对路径
     */
    public static String EXTERNAL_STORAGE_DIRECTORY_ABSOLUTE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 崩溃
     */
    public static String CRASH = EXTERNAL_STORAGE_DIRECTORY_ABSOLUTE_PATH + "/bocop/crash/";
}
