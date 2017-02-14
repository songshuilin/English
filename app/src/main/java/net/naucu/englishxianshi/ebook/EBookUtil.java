package net.naucu.englishxianshi.ebook;

import com.jiongbull.jlog.JLog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fght
 *         creat at 15-11-26
 *         电子书工具类
 *
 */
public class EBookUtil {

    private List<EBook> ebook_map;
    private static EBookUtil ebookUtil;
    private static String lastEbookPath = "";

    public EBookUtil(String ebookPath) {
        parseEBook(ebookPath);
        ebookUtil = this;
    }

    private static EBookUtil instance(String ebookPath) {
        if (ebookPath.equals(lastEbookPath)) {
            return ebookUtil == null ? new EBookUtil(ebookPath) : ebookUtil;
        } else {
            lastEbookPath = ebookPath;
            return new EBookUtil(ebookPath);
        }
    }

    public static EBookUtil instance(String ebookPath, boolean reload) {
        return reload ? new EBookUtil(ebookPath) : instance(ebookPath);
    }

    public static void release() {
        if (ebookUtil != null) {
            ebookUtil.clear();
            ebookUtil = null;
        }
        lastEbookPath = "";
    }

    private void clear() {
        if (ebook_map != null) {
            ebook_map.clear();
            ebook_map = null;
        }
    }

    public List<EBook> getEBookMap() {
        return ebook_map;
    }

    private void parseEBook(String srtPath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(srtPath);
        } catch (FileNotFoundException e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            return;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = null;
            ebook_map = new ArrayList<EBook>();
            boolean first = true;
            String ebookBodyE = "";
            String ebookBodyC = "";
            while ((line = br.readLine()) != null) {
                if (!line.equals("")) {
                    if (first) {
                        ebookBodyE = line;
                        first = false;
                    } else {
                        EBook ebook = new EBook();
                        if (line.charAt(0) > 0x4d99 || line.charAt(line.length() >> 1) > 0x4d99) {//中文
                            ebookBodyC = line;
//							ebook.setEbookBodyE(new String(ebookBodyE.getBytes(),"UTF-8"));
//							ebook.setEbookBodyC(new String(ebookBodyC.getBytes(),"UTF-8"));
                            ebook.setEbookBodyE(ebookBodyE);
                            ebook.setEbookBodyC(ebookBodyC);
                            ebookBodyE = "";
                            ebookBodyC = "";
                            first = true;
                        } else {
                            ebook.setEbookBodyE(ebookBodyE);
                            ebookBodyE = line;
                            first = false;
                        }
                        ebook_map.add(ebook);
                    }
                }
            }
            if (!ebookBodyE.equals("")) {
                EBook ebook = new EBook();
//				ebook.setEbookBodyE(new String(ebookBodyE.getBytes(),"UTF-8"));
                ebook.setEbookBodyE(ebookBodyE);
                ebook.setEbookBodyC(ebookBodyC);
                ebook_map.add(ebook);
            }
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            clear();
        } finally {
            try {
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
            }
        }
    }
}
