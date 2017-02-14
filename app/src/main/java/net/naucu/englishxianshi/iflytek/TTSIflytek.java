package net.naucu.englishxianshi.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.util.SharedPreTool;

/**
 * 类名: TtsIflytek.java
 * 描述: TODO 讯飞语音合成
 * 作者: youyou_pc
 * 时间: 2015年11月28日 上午9:32:07
 */
public class TTSIflytek {
    public static final String TAG = TTSIflytek.class.getSimpleName();

    private Context context;
    private SpeechSynthesizer speechSynthesizer;// 语音合成对象
    private ImageView _read;
    // 1 翻译  2 电影 电子书
    private int type;

    public void set_read(ImageView _read, int type) {
        this._read = _read;
        this.type = type;
    }

    public TTSIflytek(Context context) {
        this.context = context;
        initTts();
        setParam();
    }

    private void initTts() {
        // 初始化合成对象
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(context, initListener);

    }

    public SpeechSynthesizer getSpeechSynthesizer() {
        return speechSynthesizer;
    }

    /**
     * 初始对象事件
     */
    private InitListener initListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(context, "初始化失败" + code, Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(context,"初始化成功",2000).show();
            }
        }
    };

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        speechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        String temp = SharedPreTool.getSharedPreDateString(context, "ReadCharactersShared", "henry");
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
                SharedPreTool.getSharedPreDateString(context, "ReadCharactersShared", temp));

        // 设置合成语速
        speechSynthesizer.setParameter(SpeechConstant.SPEED,
                SharedPreTool.getSharedPreDateInt(context, "EadLanguageShared", 50) + "");

        //设置合成音调 2016-5-16 Dong
        speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");

//        // 设置合成音调
//        speechSynthesizer.setParameter(SpeechConstant.PITCH, "80");
//        // 设置合成音量
//        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "200");
        // 设置播放器音频流类型
        speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH,
                Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 关闭发音合成
     */
    public void CancelTts() {
        speechSynthesizer.stopSpeaking();
    }

    /**
     * 暂停发音
     */
    public void PauseTts() {
        speechSynthesizer.pauseSpeaking();
    }

    /**
     * 继续发音
     */
    public void ResumeTts() {
        speechSynthesizer.resumeSpeaking();
    }

    /**
     * 设置发音文本
     */
    public void setTextflytek(String text) {
        int code = speechSynthesizer.startSpeaking(text, synthesizerListener);
    }

    /**
     * 发音页onDestroy调用
     */
    public void onDestroyFlytek() {
        speechSynthesizer.stopSpeaking();
        // 退出时释放连接
        speechSynthesizer.destroy();
    }

    /**
     * 发音页onResume调用
     */
    public void onResumeFlytek() {
        FlowerCollector.onResume(context);
    }

    /**
     * 发音页onResume调用
     */
    public void onPauseFlytek() {
        FlowerCollector.onPause(context);
    }

    /**
     * 合成回调监听。
     */
    public SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            JLog.e("SynthesizerListener SpeakBegin");
            if (type == 1) {
                _read.setImageResource(R.drawable.bianse);
            }
            if (type == 2) {
                _read.setImageResource(R.drawable.video_read_f);
            }
        }

        @Override
        public void onSpeakPaused() {
            JLog.e("SynthesizerListener SpeakPaused");
        }

        @Override
        public void onSpeakResumed() {
            JLog.e("SynthesizerListener SpeakResumed");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            JLog.e("SynthesizerListener BufferProgress percent=" + percent + ",beginPos=" + beginPos + ",endPos=" + endPos + ",info=" + info);
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            //  JLog.e("SynthesizerListener SpeakProgress percent=" + percent + ",beginPos=" + beginPos + ",endPos=" + endPos);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (type == 1) {
                if (_read != null) {
                    _read.setImageResource(R.drawable.yu);
                    Log.i("GM", "修改");
                }
            }
            if (type == 2) {
                if (_read != null) {
                    _read.setImageResource(R.drawable.video_read_n);
                    Log.i("GM", "修改");
                }
            }
//            if (type == 3) {
//                if (_read != null) {
//                    _read.setImageResource(R.drawable.video_read_n);
//                    Log.i("GM", "修改");
//                }
//            }
            if (error != null) {
                JLog.e("SynthesizerListener Completed code=" + error.getErrorCode() + ",error=" + error.getErrorDescription());


            } else {
                JLog.e("SynthesizerListener Completed");
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
}
