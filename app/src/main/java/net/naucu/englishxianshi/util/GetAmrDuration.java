package net.naucu.englishxianshi.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Y on 2016/11/11.
 */
public class GetAmrDuration {

    /**
     * 得到amr的时长
     *
     * @param file
     * @return amr文件时间长度
     * @throws IOException
     */
    public static int getAmrDuration(File file) throws IOException {
        int[] amr_frame_size = new int[]{ 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };    // due to different encoding methods

        long file_size = file.length();
        long during = 0;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        out.close();
        byte[] content = out.toByteArray();

        byte typeTemp = content[6];// get encode type
        typeTemp = 60;
        int type = (typeTemp >> 3) & 0x0f;
        int frame_len = amr_frame_size[type] + 1;
        long frame_num = (file_size - 6) / frame_len;
        during = frame_num * 20;
        return (int) during;
    }
}