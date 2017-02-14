package net.naucu.englishxianshi.ui.video.window.event;

import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechError;

public class SimpleEvaluatorListener implements EvaluatorListener {
    public static final String TAG = SimpleEvaluatorListener.class.getSimpleName();

    @Override
    public void onBeginOfSpeech() {
        Log.e(TAG, "onBeginOfSpeech");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "onBeginOfSpeech");
    }

    @Override
    public void onError(SpeechError error) {
        Log.e(TAG, "onError： ErrorCode:" + error.getErrorCode() + ",ErrorMsg:" + error.getMessage());
    }

    @Override
    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        Log.e(TAG, "onEvent");
    }

    @Override
    public void onResult(EvaluatorResult result, boolean arg1) {
        Log.e(TAG, "onResult: Result:" + result.getResultString());
    }

    @Override
    public void onVolumeChanged(int arg0, byte[] arg1) {
        Log.e(TAG, "onVolumeChanged");
    }

}
