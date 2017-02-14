package com.lijunsai.httpInterface.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

    private MD5() {
    }

    public static String toHexString(byte bytes[]) {
        if (bytes == null)
            return "";
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        byte arr$[] = bytes;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            byte b = arr$[i$];
            hex.append(hexDigits[b >> 4 & 15]);
            hex.append(hexDigits[b & 15]);
        }

        return hex.toString();
    }

    public static String md5(File file)
            throws IOException {
        FileChannel ch;
        byte encodeBytes[];
        MessageDigest messagedigest = null;
        FileInputStream in = null;
        ch = null;
        encodeBytes = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            java.nio.MappedByteBuffer byteBuffer = ch.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0L, file.length());
            messagedigest.update(byteBuffer);
            encodeBytes = messagedigest.digest();
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (ch != null) {
                ch.close();
                ch = null;
            }
        }
        return toHexString(encodeBytes);
    }

    public static String md5(String string) {
        byte encodeBytes[] = null;
        try {
            encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException neverHappened) {
            throw new RuntimeException(neverHappened);
        } catch (UnsupportedEncodingException neverHappened) {
            throw new RuntimeException(neverHappened);
        }
        return toHexString(encodeBytes);
    }

    private static final char hexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };

}