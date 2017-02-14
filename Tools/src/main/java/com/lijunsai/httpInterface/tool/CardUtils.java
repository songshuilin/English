package com.lijunsai.httpInterface.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 内存卡 工具类<br>
 */
public class CardUtils {
    //总路径


    public static String basePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + ".EnglishXS/";
    //项目图片路径
    public static String imagePath = basePath + "image/";
    //项目文件路径
    public static String filePath = basePath + "file/";
    //电子书路径
    public static String booksPath = basePath + "file/books/";
    //电影路径
    public static String videoPath = basePath + "file/video/";
    //录音路径
    public static String recordPath = basePath + "file/record/";

    //缓存文件夹
    public static String temppath = basePath + "temp/";
    //头像上传缓存
    public static String tempheadpath = basePath + "temp/UpHead/";


    public static void initBasePath(Context context) {
        String path = SharedPreTool.getSharedPreDateString(context, "savePath", null);
        if (path != null && new File(path).exists()) {
            basePath = path + "/EnglishXian/";
        }
        InItStorageLocation();
    }

    /**
     * 文件存放地址 有SD卡优先  无SD开 内存储存空间优先
     */
    public static void InItStorageLocation() {
        String[] paths = new String[]{basePath, imagePath, filePath,
                booksPath, videoPath, recordPath, temppath, tempheadpath};
        for (String path : paths) {
            File file = new File(path);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
        }
        deleteFile(tempheadpath);
    }

    /**
     * 删除某目录下的文件
     */
    public static void deleteFile(String folder) {
        File temp = new File(folder);
        if (temp != null && temp.length() != 0) {
            File[] files = temp.listFiles();
            if (files.length != 0) {
                for (File file : files) {
                    file.delete();
                    System.out.println(file.getName());
                }
            }
        }
    }
}
