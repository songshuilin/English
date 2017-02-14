package net.naucu.englishxianshi.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.iflytek.bean.ISEIflytekBean;
import net.naucu.englishxianshi.widget.dialog.PromptDialog;
import net.naucu.englishxianshi.widget.dialog.PromptDialog.onPromptClickListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 类名: ISEIflytek.java
 * 描述: TODO 讯飞语音评测
 * 作者: youyou_pc
 * 时间: 2015年11月28日 上午11:23:11
 */
@SuppressWarnings("unused")
public class ISEIflytek {
    public static final String TAG = ISEIflytek.class.getSimpleName();
    private Context context;
    private SpeechEvaluator speechEvaluator;// 语音评测对象

    public ISEIflytek(Context context) {
        this.context = context;
        initISE();
        setParams();
    }

    private void initISE() {
        // 初始化语音评测对象
        speechEvaluator = SpeechEvaluator.createEvaluator(context, new InitListener() {
            @Override
            public void onInit(int code) {
                JLog.e("ISEIflytek init code=" + code);
//				if(code!=ErrorCode.SUCCESS){
//					Toast.makeText(context,"初始化失败"+code,2000).show();
//				}else{Toast.makeText(context,"初始化成功",2000).show();
//				}
            }
        });
    }

    /**
     * 键入要朗读文字
     */
    public void setTextInput(String text) {
        if (speechEvaluator == null) {
            return;
        }
        speechEvaluator.startEvaluating(text, null, evaluatorListener);
    }

    /**
     * 键入要朗读文字
     */
    public void setTextInput(String text, String savepath) {
        if (speechEvaluator == null) {
            return;
        }
        speechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, savepath);
        speechEvaluator.startEvaluating(text, null, evaluatorListener);
    }

    /**
     * 键入要朗读文字
     */
    public void setTextInput(String text, String savepath, EvaluatorListener evaluatorListener) {
        if (speechEvaluator == null || evaluatorListener == null) {
            return;
        }
            speechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, savepath);
        try {
            speechEvaluator.startEvaluating(text, null, evaluatorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (speechEvaluator != null && speechEvaluator.isEvaluating()) {
            speechEvaluator.stopEvaluating();
        }
    }

    // 评测监听接口
    private EvaluatorListener evaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            JLog.e("onResult isLast=" + isLast + ",result=" + result.getResultString());
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                if (!TextUtils.isEmpty(builder)) {
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(new ByteArrayInputStream(builder.toString().getBytes()), "utf-8");
                        int event = parser.getEventType();
                        while (event != XmlPullParser.END_DOCUMENT) {
                            switch (event) {
                                case XmlPullParser.START_TAG:
                                    if (parser.getName() != null && parser.getName().equals("read_sentence")) {
                                        if (parser.getAttributeCount() == 5) {
                                            String content = parser.getAttributeValue(1);
                                            String end_pos = parser.getAttributeValue(2);
                                            String time_len = parser.getAttributeValue(3);
                                            String total_score = parser.getAttributeValue(4);
                                            ISEIflytekBean iflytekBean = new ISEIflytekBean(content, end_pos, time_len, total_score);
                                            final PromptDialog dialog = new PromptDialog(context);
                                            dialog.setContent(iflytekBean.toString());
                                            dialog.show();
                                            dialog.setClickListener(new onPromptClickListener() {

                                                @Override
                                                public void onDetermine(View v) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onCancel(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    }
//										   <read_chapter beg_pos="0" content="The quick brown fox jumps over the lazy dog." end_pos="532" except_info="28676" is_rejected="true" total_score="0.219109" word_count="9">
                                    if (parser.getName() != null && parser.getName().equals("read_chapter")) {
                                        if (parser.getAttributeCount() == 7) {
                                            String content = parser.getAttributeValue(1);
                                            String end_pos = parser.getAttributeValue(2);
                                            String time_len = 0 + "";
                                            String total_score = parser.getAttributeValue(5);
                                            ISEIflytekBean iflytekBean = new ISEIflytekBean(content, end_pos, time_len, total_score);
                                            final PromptDialog dialog = new PromptDialog(context);
                                            dialog.setContent(iflytekBean.toString());
                                            dialog.show();
                                            dialog.setClickListener(new onPromptClickListener() {

                                                @Override
                                                public void onDetermine(View v) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onCancel(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    }
                                    break;
                                case XmlPullParser.END_TAG:

                                    break;
                            }
                            event = parser.next();
                        }
                    } catch (XmlPullParserException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    } catch (IOException e) {
                        JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    }


                }
            }
        }

        @Override
        public void onError(SpeechError error) {
            if (error != null) {
                JLog.e("EvaluatorListener Error code=" + error.getErrorCode() + ",msg=" + error.getErrorDescription());
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            JLog.e("EvaluatorListener VolumeChange volume=" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

    };

    private void setParams() {
        // SharedPreferences pref = getSharedPreferences(PREFER_NAME,
        // MODE_PRIVATE);
        // // 设置评测语言
        // language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
        // // 设置需要评测的类型
        // category = pref.getString(SpeechConstant.ISE_CATEGORY,
        // "read_sentence");
        // // 设置结果等级（中文仅支持complete）
        // result_level = pref.getString(SpeechConstant.RESULT_LEVEL,
        // "complete");
        // // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        // String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "5000");
        // // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        // String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "1800");
        // // 语音输入超时时间，即用户最多可以连续说多长时间；
        // String speech_timeout =
        // pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
        //
//		speechEvaluator.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechEvaluator.setParameter(SpeechConstant.LANGUAGE, "en_us");
        speechEvaluator.setParameter(SpeechConstant.ISE_CATEGORY, "read_sentence");
        speechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        /*
         * 2016.5.17- dong
		 * speechEvaluator.setParameter(SpeechConstant.VAD_BOS, "3500");
		speechEvaluator.setParameter(SpeechConstant.VAD_EOS, "1200");*/

        speechEvaluator.setParameter(SpeechConstant.VAD_BOS, "5000");
        speechEvaluator.setParameter(SpeechConstant.VAD_EOS, "3000");

        speechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
        speechEvaluator.setParameter(SpeechConstant.RESULT_LEVEL, "complete");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechEvaluator.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");

    }

}
