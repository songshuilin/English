package net.naucu.englishxianshi.ui.video;

import android.graphics.Color;

public abstract class PlayConfig {

    // 字幕全屏状态
    public static boolean isFullStatus = false;
    // 输入模式
    public static final String AUTO_INPUT = "autoInput";
    // 选择模式
    public static final String CHOICE_MODEL = "choiceModel";
    // 电影字体大小
    public static final String MOVIE_TEXT_SIZE = "movietextSize";
    // 图书字体大小
    public static final String BOOK_TEXT_SIZE = "booktextSize";
    // 录音开始画面暂停
    public static final String BEGIN_PLAY_PAUSE = "isBeginStop";
    // 录音过程是否限制时间
    public static final String RECORD_LIMIT_TIME = "isTimeLimit";
    // 每句录音完毕是否暂停试听
    public static final String COMPLETE_PAUES_PLAY = "isEndStop";

    // 视频播放 已显示字幕
    public static final int PLAY_HAVE_COLOR = Color.parseColor("#CCCCCC");
    // 视频播放 当前显示字幕
    public static final int PLAY_CURRENT_COLOR = 0xffdd7777;
    // 视频播放待显示字幕
    public static final int PLAY_NOT_COLOR = Color.parseColor("#000000");
    // 选择字幕播放
    public static final int SELECT_SUBTITLE_COLOR = Color.parseColor("#CB8335");
    // 角色扮演当前显示字幕
    public static final int ROLE_PLAY_CURRENT_COLOR = Color.parseColor("#B0E2FF");
    // 角色扮演未显示字幕
    public static final int ROLE_PLAY_NOT_COLOR = Color.parseColor("#FFFFFF");

    // 字幕操作类型
    public static OpType opType = OpType.NONE;
    // 字幕中英文显示
    public static ShType shType = ShType.NONE;
    // Holder样式
    public static HoType hoType = HoType.PLAY;
    // 角色扮演标识
    public static RoType roType = RoType.START;

    // 字幕字体大小
    public static int movietextSize = 22;
    // 图书字体大小
    public static int booktextSize = 14;
    // 录音开始画面暂停
    public static boolean isBeginPlayPause = false;
    // 录音过程是否限制时间
    public static boolean isRecordLimitTime = false;
    // 每句录音完毕是否暂停试听
    public static boolean isCompletePausePlay = false;

    /**
     * 字幕操作类型
     *
     * @author Yi
     */
    public enum OpType {
        NONE, // 无操作
        ANSWER_READ, // 复读机
        ROLE_PLAY, // 角色扮演
        FULL, // 全屏
        SUBTITLE_SELECT, // 选择字幕
        MORE_SETTING // 更多设置
    }

    /**
     * 字幕中英文显示
     *
     * @author Yi
     */
    public enum ShType {
        NONE, // 全部显示
        ALL, // 全部隐藏
        CHINESE, // 中文隐藏
        ENGLISH // 英文隐藏
    }

    /**
     * ViewHolder样式
     *
     * @author Yi
     */
    public enum HoType {
        PLAY, // 视频播放
        ROLE_BEGIN, // 选择首句
        ROLE_END, // 选择尾句
        ROLE_RECORD, // 选择语句录音
        SELECT_SUB, // 选择字幕
        SELECT_PLAY // 选择字幕播放
    }

    /**
     * 角色扮演标识
     *
     * @author garmi
     */
    public enum RoType {
        START, //
        PREARE_RECORD, // 准备录音
        COUNT_DOWN, // 倒计时
        TAPE_RECORD, // 录音开始
        SENTENCE_END, // 本句录音结束
        COMPLETE_RECORD// 录音完成
    }
}
