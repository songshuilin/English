package net.naucu.englishxianshi.ui.video.window.event;

/**
 * Window状态监听器
 *
 * @author Yi
 */
public interface OnWindowStateListener {

    /**
     * Window打开
     */
    void windowOpen();

    /**
     * Window关闭 内部关闭
     */
    void windowInternalClose();

    /**
     * Window关闭 外部关闭
     */
    void windowExternalClose();
}
