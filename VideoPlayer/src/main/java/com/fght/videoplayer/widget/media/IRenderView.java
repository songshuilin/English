/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author: y.haiyang@qq.com
 */

package com.fght.videoplayer.widget.media;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IRenderView {
    static final int AR_ASPECT_FIT_PARENT = 0; // without clip
    static final int AR_ASPECT_FILL_PARENT = 1; // may clip
    static final int AR_ASPECT_WRAP_CONTENT = 2;
    static final int AR_MATCH_PARENT = 3;
    static final int AR_16_9_FIT_PARENT = 4;
    static final int AR_4_3_FIT_PARENT = 5;

    View getView();

    boolean shouldWaitForResize();

    void setVideoSize(int videoWidth, int videoHeight);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    void setVideoRotation(int degree);

    void setAspectRatio(int aspectRatio);

    void addRenderCallback(IRenderCallback callback);

    void removeRenderCallback(IRenderCallback callback);

    interface ISurfaceHolder {
        void bindToMediaPlayer(IMediaPlayer mp);

        IRenderView getRenderView();

        SurfaceHolder getSurfaceHolder();

        Surface openSurface();

        SurfaceTexture getSurfaceTexture();
    }

    public interface IRenderCallback {
        /**
         * @param holder
         * @param width
         *            could be 0
         * @param height
         *            could be 0
         */
        void onSurfaceCreated(ISurfaceHolder holder, int width, int height);

        /**
         * @param holder
         * @param format
         *            could be 0
         * @param width
         * @param height
         */
        void onSurfaceChanged(ISurfaceHolder holder, int format, int width, int height);

        void onSurfaceDestroyed(ISurfaceHolder holder);
    }
}
