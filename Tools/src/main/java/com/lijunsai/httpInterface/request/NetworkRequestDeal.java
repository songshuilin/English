package com.lijunsai.httpInterface.request;

import android.text.TextUtils;
import android.util.Log;

import com.lijunsai.httpInterface.bean.ALLbookBean;
import com.lijunsai.httpInterface.bean.ALLbookDetailsBean;
import com.lijunsai.httpInterface.bean.ALLmovieBean;
import com.lijunsai.httpInterface.bean.ALLmovieDetailsBean;
import com.lijunsai.httpInterface.bean.AllMoviePath;
import com.lijunsai.httpInterface.bean.BaiduTranslation;
import com.lijunsai.httpInterface.bean.LoginBean;
import com.lijunsai.httpInterface.implementation.HttpImplementation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkRequestDeal {
    /**
     * 判断ErrCode是否存在
     *
     * @param Results
     * @return
     */
    public static boolean isErrCode(String Results) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(Results);
//        Log.i("TAG", "asdhasldjlasd =" + jsonObject.has("code"));
            return jsonObject.has("code");
        } catch (JSONException e) {
        }
//        Log.i("TAG", "ewq57a54daw74");
        return false;
    }

    public static boolean isErrCode1(String Results) {
        try {
            JSONObject jsonObject = new JSONObject(Results);
            return jsonObject.has("errCode");
        } catch (JSONException e) {
        }
        return false;
    }

    /**
     * 获取错误码
     */
    public static Integer getErrCode(String Results) {
        Integer temp = 0;
        try {
            JSONObject jsonObject = new JSONObject(Results);
            temp = jsonObject.optInt("code");
        } catch (JSONException e) {
        }
        return temp;
    }

    /**
     * 获取错误信息
     */
    public static String getErrMsg(String Results) {
        String temp = "";
        try {
            JSONObject jsonObject = new JSONObject(Results);
            temp = jsonObject.optString("msg");
        } catch (JSONException e) {
        }
        return temp;
    }

    /**
     * 获取错误信息
     */
    public static boolean hasErrMsg(String Results) {
        if (Results == null) {
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(Results);
            return jsonObject.has("msg");
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 登录请求下的数据
     *
     * @return
     */
    public static LoginBean getLogin(String Results) {
        LoginBean bean = new LoginBean();
        try {
            JSONObject object = new JSONObject(Results);
            JSONObject obj = object.getJSONObject("result");
            bean.setUid(obj.getInt("uid"));
            bean.setUsername(obj.getString("username"));
            bean.setTelephone(obj.getString("telephone"));
            bean.setGender(obj.getString("gender"));
            bean.setState(obj.getString("state"));
            bean.setPayTime(obj.getString("payTime"));
            bean.setActiveCode(obj.getString("activeCode"));
            if (!TextUtils.isEmpty(obj.getString("picUrl"))) {
                bean.setPicUrl(HttpImplementation.getHttpUrl() + obj.getString("picUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }

    public static LoginBean getLogin2(String Results) {
        LoginBean bean = new LoginBean();
        try {
            JSONObject obj = new JSONObject(Results);
            bean.setUid(obj.getInt("uid"));
            bean.setUsername(obj.getString("username"));
            bean.setTelephone(obj.getString("telephone"));
            bean.setGender(obj.getString("gender"));
            bean.setState(obj.getString("state"));
            bean.setPayTime(obj.getString("payTime"));
            bean.setActiveCode(obj.getString("activeCode"));
            if (!TextUtils.isEmpty(obj.getString("picUrl"))) {
                bean.setPicUrl(HttpImplementation.getHttpUrl() + obj.getString("picUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }

    /**
     * 获取电子书首页
     */
    public static List<ALLbookBean> getALLbook(String Results) {
        List<ALLbookBean> alLbookBeans = new ArrayList<ALLbookBean>();
        ;
        List<ALLbookDetailsBean> alLbookDetailsBeans = null;
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                JSONArray array2 = array.getJSONArray(i);
                ALLbookBean alLbookBean = new ALLbookBean();
                alLbookDetailsBeans = new ArrayList<ALLbookDetailsBean>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject jsonObject = array2.getJSONObject(j);
                    ALLbookDetailsBean alLbookDetailsBean = new ALLbookDetailsBean();
                    alLbookDetailsBean.setBookAbout(jsonObject.getString("bookAbout"));
                    alLbookDetailsBean.setBookAuthor(jsonObject.getString("bookAuthor"));
                    alLbookDetailsBean.setBookImage(HttpImplementation.getHttpUrl() + jsonObject.getString("bookImage"));
                    alLbookDetailsBean.setBookName(jsonObject.getString("bookName"));
                    alLbookDetailsBean.setBookpingfen(jsonObject.getDouble("bookpingfen"));
                    alLbookDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLbookDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLbookDetailsBean.setId(jsonObject.getString("id"));
                    alLbookDetailsBean.setPrice(jsonObject.getString("price"));
                    alLbookDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    alLbookDetailsBeans.add(alLbookDetailsBean);
                    alLbookBean.setTwoCategory(jsonObject.getString("twoCategory"));
                }
                alLbookBean.setGetbookDetailsBeans(alLbookDetailsBeans);
                alLbookBeans.add(alLbookBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return alLbookBeans;
    }

    /**
     * 获取电子书二级
     */
    public static List<ALLbookDetailsBean> getALLbookSecondary(String Results) {
        List<ALLbookDetailsBean> alLbookDetailsBeans = new ArrayList<ALLbookDetailsBean>();
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ALLbookDetailsBean alLbookDetailsBean = new ALLbookDetailsBean();
                alLbookDetailsBean.setBookAbout(jsonObject.getString("bookAbout"));
                alLbookDetailsBean.setBookAuthor(jsonObject.getString("bookAuthor"));
                alLbookDetailsBean.setBookImage(HttpImplementation.getHttpUrl() + jsonObject.getString("bookImage"));
                alLbookDetailsBean.setBookName(jsonObject.getString("bookName"));
                alLbookDetailsBean.setBookpingfen(jsonObject.getDouble("bookpingfen"));
                alLbookDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                alLbookDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                alLbookDetailsBean.setId(jsonObject.getString("id"));
                alLbookDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                alLbookDetailsBean.setPrice(jsonObject.getString("price"));
                alLbookDetailsBeans.add(alLbookDetailsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return alLbookDetailsBeans;
    }

    /**
     * 查询电子书
     */
    public static List<ALLbookDetailsBean> getSerchBook(String Results) {
        List<ALLbookDetailsBean> alLbookDetailsBeans = new ArrayList<ALLbookDetailsBean>();
        try {
            JSONObject json = new JSONObject(Results);
            JSONArray array = json.getJSONArray("book");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ALLbookDetailsBean alLbookDetailsBean = new ALLbookDetailsBean();
                alLbookDetailsBean.setBookAbout(jsonObject.getString("bookAbout"));
                alLbookDetailsBean.setBookAuthor(jsonObject.getString("bookAuthor"));
                alLbookDetailsBean.setBookImage(HttpImplementation.getHttpUrl() + jsonObject.getString("bookImage"));
                alLbookDetailsBean.setBookName(jsonObject.getString("bookName"));
                alLbookDetailsBean.setBookpingfen(jsonObject.getDouble("bookpingfen"));
                alLbookDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                alLbookDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                alLbookDetailsBean.setId(jsonObject.getString("id"));
                alLbookDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                alLbookDetailsBean.setPrice(jsonObject.getString("price"));
                alLbookDetailsBean.setNovel(false);
                alLbookDetailsBeans.add(alLbookDetailsBean);
            }
            JSONArray array2 = json.getJSONArray("novel");
            for (int i = 0; i < array2.length(); i++) {
                JSONObject jsonObject = array2.getJSONObject(i);
                ALLbookDetailsBean alLbookDetailsBean = new ALLbookDetailsBean();
                alLbookDetailsBean.setBookAbout(jsonObject.getString("novelAbout"));
                alLbookDetailsBean.setBookAuthor(jsonObject.getString("novelAuthor"));
                alLbookDetailsBean.setBookImage(HttpImplementation.getHttpUrl() + jsonObject.getString("novelImageUrl"));
                alLbookDetailsBean.setBookName(jsonObject.getString("novelName"));
                alLbookDetailsBean.setBookpingfen(jsonObject.getDouble("novelpingfen"));
                alLbookDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                alLbookDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                alLbookDetailsBean.setId(jsonObject.getString("id"));
                alLbookDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                alLbookDetailsBean.setNovel(true);
                alLbookDetailsBeans.add(alLbookDetailsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return alLbookDetailsBeans;
    }

    /**
     * 获取分类
     *
     * @param Results
     */
    public static List<String> getDirectoryAccess(String Results) {
        List<String> list = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获取欢迎页
     */
    public static String getWelcome(String Results) {
        String pic = null;
        try {
            JSONObject jsonObject = new JSONObject(Results);
            pic = HttpImplementation.getHttpUrl() + jsonObject.getString("picUrl").replaceAll("\\\\", "/");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return pic;
    }

    /**
     * 获取电子书下载地址
     */
    public static String getQueryDownload(String Results) {
        String download = null;
        try {
            JSONObject jsonObject = new JSONObject(Results);
            download = HttpImplementation.getHttpUrl() + jsonObject.getString("booksUrl").replaceAll("\\\\", "/");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return download;
    }

    /**
     * 获取电影
     */
    public static List<ALLmovieBean> getMovie(String Results) {
        List<ALLmovieBean> alLmovieBeans = new ArrayList<ALLmovieBean>();
        List<ALLmovieDetailsBean> lmovieDetailsBeans = null;
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                JSONArray array2 = array.getJSONArray(i);
                ALLmovieBean alLmovieBean = new ALLmovieBean();
                lmovieDetailsBeans = new ArrayList<ALLmovieDetailsBean>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject jsonObject = array2.getJSONObject(j);
                    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                    alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                    alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                    alLmovieDetailsBean.setMovieAbout(jsonObject.getString("movieAbout"));
                    alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("movieImageUrl"));
                    alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("movieMadeBy"));
                    alLmovieDetailsBean.setMovieName(jsonObject.getString("movieName"));
                    alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("moviepingfen"));
                    alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("movieAuthor"));
                    alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    lmovieDetailsBeans.add(alLmovieDetailsBean);
                    alLmovieBean.setTwoCategory(jsonObject.getString("twoCategory"));
                }
                alLmovieBean.setGetalLmovieDetailsBeans(lmovieDetailsBeans);
                alLmovieBeans.add(alLmovieBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return alLmovieBeans;
    }

    /**
     * 获取美剧电影
     */
    public static List<ALLmovieBean> getSlowMovie(String Results) {
        List<ALLmovieBean> alLmovieBeans = new ArrayList<ALLmovieBean>();
        List<ALLmovieDetailsBean> lmovieDetailsBeans = null;
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                JSONArray array2 = array.getJSONArray(i);
                ALLmovieBean alLmovieBean = new ALLmovieBean();
                lmovieDetailsBeans = new ArrayList<ALLmovieDetailsBean>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject jsonObject = array2.getJSONObject(j);
                    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                    alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                    alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                    alLmovieDetailsBean.setMovieAbout(jsonObject.getString("tvAbout"));
                    alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("tvImageUrl"));
                    alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("tvMadeBy"));
                    alLmovieDetailsBean.setMovieName(jsonObject.getString("tvName"));
                    alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("tvpingfen"));
                    alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("tvAuthor"));
                    alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    alLmovieDetailsBean.setTV(true);
                    lmovieDetailsBeans.add(alLmovieDetailsBean);
                    alLmovieBean.setTwoCategory(jsonObject.getString("twoCategory"));
                }
                alLmovieBean.setGetalLmovieDetailsBeans(lmovieDetailsBeans);
                alLmovieBeans.add(alLmovieBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return alLmovieBeans;
    }


    /**
     * 获取电影
     */
    public static List<ALLmovieDetailsBean> getMovieDetails(String Results) {
        List<ALLmovieDetailsBean> lmovieDetailsBeans = null;
        try {
            JSONArray array = new JSONArray(Results);
            lmovieDetailsBeans = new ArrayList<ALLmovieDetailsBean>();
            for (int j = 0; j < array.length(); j++) {
                JSONObject jsonObject = array.getJSONObject(j);
                ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                alLmovieDetailsBean.setMovieAbout(jsonObject.getString("movieAbout"));
                alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("movieImageUrl"));
                alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("movieMadeBy"));
                alLmovieDetailsBean.setMovieName(jsonObject.getString("movieName"));
                alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("moviepingfen"));
                alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("movieAuthor"));
                alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                lmovieDetailsBeans.add(alLmovieDetailsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return lmovieDetailsBeans;
    }

    /**
     * 查询电影
     */
    public static List<ALLmovieDetailsBean> getSerchMovie(String Results) {
        List<ALLmovieDetailsBean> lmovieDetailsBeans = null;
        Log.i("GM", "" + Results);
        try {
            JSONObject json = new JSONObject(Results);
            lmovieDetailsBeans = new ArrayList<ALLmovieDetailsBean>();
            JSONArray array = null;
            try {
                array = json.getJSONArray("movie");
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonObject = array.getJSONObject(j);
                    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                    alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                    alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                    alLmovieDetailsBean.setMovieAbout(jsonObject.getString("movieAbout"));
                    alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("movieImageUrl"));
                    alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("movieMadeBy"));
                    alLmovieDetailsBean.setMovieName(jsonObject.getString("movieName"));
                    alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("moviepingfen"));
                    alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("movieAuthor"));
                    alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    alLmovieDetailsBean.setTV(false);
                    lmovieDetailsBeans.add(alLmovieDetailsBean);
                }
                JSONArray array2 = json.getJSONArray("tv");
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject jsonObject = array2.getJSONObject(j);
                    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                    alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                    alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                    alLmovieDetailsBean.setMovieAbout(jsonObject.getString("tvAbout"));
                    alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("tvImageUrl"));
                    alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("tvMadeBy"));
                    alLmovieDetailsBean.setMovieName(jsonObject.getString("tvName"));
                    alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("tvpingfen"));
                    alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("tvAuthor"));
                    alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    alLmovieDetailsBean.setTV(true);
                    lmovieDetailsBeans.add(alLmovieDetailsBean);
                }
            } catch (Exception e) {
                JSONArray array2 = json.getJSONArray("tv");
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject jsonObject = array2.getJSONObject(j);
                    ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                    alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                    alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                    alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                    alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                    alLmovieDetailsBean.setMovieAbout(jsonObject.getString("tvAbout"));
                    alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("tvImageUrl"));
                    alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("tvMadeBy"));
                    alLmovieDetailsBean.setMovieName(jsonObject.getString("tvName"));
                    alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("tvpingfen"));
                    alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("tvAuthor"));
                    alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                    alLmovieDetailsBean.setTV(true);
                    lmovieDetailsBeans.add(alLmovieDetailsBean);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return lmovieDetailsBeans;
        }

    }

    /**
     * 美剧二级
     *
     * @param Results
     * @return
     */
    public static List<ALLmovieDetailsBean> getTVSlowDetails(String Results) {
        List<ALLmovieDetailsBean> lmovieDetailsBeans = null;
        try {
            JSONArray array = new JSONArray(Results);
            lmovieDetailsBeans = new ArrayList<ALLmovieDetailsBean>();
            for (int j = 0; j < array.length(); j++) {
                JSONObject jsonObject = array.getJSONObject(j);
                ALLmovieDetailsBean alLmovieDetailsBean = new ALLmovieDetailsBean();
                alLmovieDetailsBean.setCreateTime(jsonObject.getString("createTime"));
                alLmovieDetailsBean.setFirstCategory(jsonObject.getString("firstCategory"));
                alLmovieDetailsBean.setId(jsonObject.getInt("id"));
                alLmovieDetailsBean.setLevel(jsonObject.getString("level"));
                alLmovieDetailsBean.setMovieAbout(jsonObject.getString("tvAbout"));
                alLmovieDetailsBean.setMovieImageUrl(HttpImplementation.getHttpUrl() + jsonObject.getString("tvImageUrl"));
                alLmovieDetailsBean.setMovieMadeBy(jsonObject.getString("tvMadeBy"));
                alLmovieDetailsBean.setMovieName(jsonObject.getString("tvName"));
                alLmovieDetailsBean.setMoviepingfen(jsonObject.getString("tvpingfen"));
                alLmovieDetailsBean.setMovieAuthor(jsonObject.getString("tvAuthor"));
                alLmovieDetailsBean.setTwoCategory(jsonObject.getString("twoCategory"));
                alLmovieDetailsBean.setTV(true);
                lmovieDetailsBeans.add(alLmovieDetailsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return lmovieDetailsBeans;
    }

    /**
     * 获取电影字幕下载地址
     */
    public static List<AllMoviePath> getMoviesSubtitles(String Results) {
        List<AllMoviePath> allMoviePaths = new ArrayList<AllMoviePath>();
        try {
            JSONObject jsonObject = new JSONObject(Results);
            if (jsonObject.has("movieMp3Url")) {
                allMoviePaths.add(new AllMoviePath(
                        HttpImplementation.getHttpUrl() + jsonObject.getString("movieUrl").replaceAll("\\\\", "/"),
                        HttpImplementation.getHttpUrl() + jsonObject.getString("movieZiMuUrl").replaceAll("\\\\", "/"),
                        HttpImplementation.getHttpUrl() + jsonObject.getString("movieMp3Url").replaceAll("\\\\", "/")));
            } else {
                allMoviePaths.add(new AllMoviePath(
                        HttpImplementation.getHttpUrl() + jsonObject.getString("movieUrl").replaceAll("\\\\", "/"),
                        HttpImplementation.getHttpUrl() + jsonObject.getString("movieZiMuUrl").replaceAll("\\\\", "/"),
                        ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return allMoviePaths;
    }

    /**
     * 获取美剧字幕视频
     *
     * @param Results
     * @return
     */
    public static List<AllMoviePath> getMoviesTelevFlieSubtitles(String Results) {
        List<AllMoviePath> allMoviePaths = new ArrayList<AllMoviePath>();
        try {
            JSONArray array = new JSONArray(Results);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.has("tvMp3Url")) {
                    allMoviePaths.add(new AllMoviePath(
                            HttpImplementation.getHttpUrl() + jsonObject.getString("tvUrl").replaceAll("\\\\", "/"),
                            HttpImplementation.getHttpUrl() + jsonObject.getString("tvZiMuUrl").replaceAll("\\\\", "/"),
                            HttpImplementation.getHttpUrl() + jsonObject.getString("tvMp3Url").replaceAll("\\\\", "/"),
                            jsonObject.getInt("number")));
                } else {
                    allMoviePaths.add(new AllMoviePath(
                            HttpImplementation.getHttpUrl() + jsonObject.getString("tvUrl").replaceAll("\\\\", "/"),
                            HttpImplementation.getHttpUrl() + jsonObject.getString("tvZiMuUrl").replaceAll("\\\\", "/"),
                            "", jsonObject.getInt("number")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return allMoviePaths;
    }

    /**
     * 支付宝信息
     */
    public static String getPaytreasure(String Results) {
        String str = null;
        try {
            JSONObject jsonObject = new JSONObject(Results);
            str = jsonObject.getString("payOrder");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return str;
    }

    /**
     * 百度翻译
     */
    public static BaiduTranslation getBaiduTranslation(String Results) {
        BaiduTranslation baiduTranslation = null;
        try {
            JSONObject jsonObject = new JSONObject(Results);
            JSONArray array = jsonObject.getJSONArray("trans_result");
            JSONObject jsonObject2 = array.getJSONObject(0);
            baiduTranslation = new BaiduTranslation(jsonObject2.getString("src"), jsonObject2.getString("dst"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return baiduTranslation;
    }
}
