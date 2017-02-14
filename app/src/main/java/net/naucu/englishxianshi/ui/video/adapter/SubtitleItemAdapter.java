package net.naucu.englishxianshi.ui.video.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.lijunsai.httpInterface.tool.SharedPreTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity;
import net.naucu.englishxianshi.ui.video.PlayConfig;
import net.naucu.englishxianshi.ui.video.PlayConfig.HoType;
import net.naucu.englishxianshi.ui.video.PlayConfig.OpType;
import net.naucu.englishxianshi.ui.video.PlaySubtitleActivity;
import net.naucu.englishxianshi.ui.video.PlaySubtitleToActivityCallback;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleItemPlayListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleItemPromptListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleRolePlayListener;
import net.naucu.englishxianshi.ui.video.adapter.event.OnSubtitleSelectListener;
import net.naucu.englishxianshi.ui.video.bean.SubtitleBean;
import net.naucu.englishxianshi.ui.video.window.SubtitlePromptWindow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubtitleItemAdapter extends BaseAdapter {
    public PlaySubtitleToActivityCallback callBack;
    public static final String TAG = SubtitleItemAdapter.class.getSimpleName();
    private Context context;
    private List<SubtitleBean> subtitleBeanList;
    private int beginPosition = -1;
    private int endPosition = -1;
    private int endPosition2 = -1;
    private int rolePlayBeginType = -1;
    private Map<Integer, SubtitleBean> rolePlayBeginMap;
    private Map<Integer, SubtitleBean> rolePlayEndMap;
    public  Map<Integer, SubtitleBean> selectSubtitleMap;
    private OnSubtitleItemPlayListener onSubtitleItemPlayListener;
    private OnSubtitleItemPromptListener onSubtitleItemPromptListener;
    public  OnSubtitleSelectListener onSubtitleSelectListener;
    private OnSubtitleRolePlayListener onSubtitleRolePlayListener;
    private SubtitlePromptWindow subtitlePromptWindow;
    public int lvPosition = -1;
    private ArrayList<Integer> endPositions = new ArrayList<>();
    public PlaySubtitleActivity activity;
    public PlaySubtitleActivity.bsCallback bsCallback;
    public SubtitleItemAdapter(Context context, List<SubtitleBean> subtitleBeanList, PlaySubtitleToActivityCallback callBack, PlaySubtitleActivity activity, PlaySubtitleActivity.bsCallback bsCallback) {
        super();
        this.activity = activity;
        this.context = context;
        this.subtitleBeanList = subtitleBeanList;
        rolePlayBeginMap = new LinkedHashMap<>();
        rolePlayEndMap = new LinkedHashMap<>();
        selectSubtitleMap = new LinkedHashMap<>();
        if (SharedPreTool.getSharedPreDateInt(context, PlayConfig.MOVIE_TEXT_SIZE, -1) == -1) {
            SharedPreTool.setSharedPreDateInt(context, PlayConfig.MOVIE_TEXT_SIZE, 20);
        } else {
            PlayConfig.movietextSize = SharedPreTool.getSharedPreDateInt(context, PlayConfig.MOVIE_TEXT_SIZE, -1);
        }
        this.bsCallback = bsCallback;
        this.callBack = callBack;
    }

    /**
     * 设置选择字幕提示监听器
     *
     * @param onSubtitleItemPlayListener
     */
    public void setOnSubtitleItemPlayListener(OnSubtitleItemPlayListener onSubtitleItemPlayListener) {
        this.onSubtitleItemPlayListener = onSubtitleItemPlayListener;
    }
    /**
     * 设置选择字幕播放监听器
     *
     * @param onSubtitleItemPromptListener
     */
    public void setOnSubtitleItemPromptListener(OnSubtitleItemPromptListener onSubtitleItemPromptListener) {
        this.onSubtitleItemPromptListener = onSubtitleItemPromptListener;
    }

    /**
     * 设置角色扮演语句选择监听器
     *
     * @param onSubtitleSelectListener
     */
    public void setOnSubtitleSelectListener(OnSubtitleSelectListener onSubtitleSelectListener) {
        this.onSubtitleSelectListener = onSubtitleSelectListener;
    }

    /**
     * 设置选择字幕监听器
     *
     * @param onSubtitleRolePlayListener
     */
    public void setOnSubtitleRolePlayListener(OnSubtitleRolePlayListener onSubtitleRolePlayListener) {
        this.onSubtitleRolePlayListener = onSubtitleRolePlayListener;
    }

    @Override
    public int getCount() {
        return subtitleBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return subtitleBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubtitleHolder subtitleHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.video_subtitle_item, null);
            subtitleHolder = new SubtitleHolder(convertView, this);
            convertView.setTag(subtitleHolder);
        } else {
            subtitleHolder = (SubtitleHolder) convertView.getTag();
        }
        subtitleHolder.itemCheckBox01.setTag(position);
        initItemData(subtitleHolder, convertView, position);
        changeItemHolderStyle(subtitleHolder, convertView, position);
        switch (PlayConfig.hoType) {
            case PLAY:
                itemHolderPlayStyle(subtitleHolder, convertView, position);
                break;
            case ROLE_BEGIN:
                if (beginPosition != -1) {
                    for (int i = beginPosition; i <= subtitleBeanList.size() - 1; i++) {
                        SubtitleBean subtitleBean = subtitleBeanList.get(i);
                        subtitleBean.setRolePlay(false);
                        subtitleBean.setRecord(false);
                    }
                    beginPosition = -1;
                    endPosition = -1;
                    rolePlayBeginMap.clear();
                    endPositions.clear();
                }
                itemHolderRoleBeginStyle(subtitleHolder, convertView, position);
                break;
            case ROLE_END:
                if (flag3 == false) {
                    flag2 = true;
                }
                flag4 = false;
                itemHolderRoleEndStyle(subtitleHolder, convertView, position);
                break;
            case ROLE_RECORD:
                itemHolderRoleRecordStyle(subtitleHolder, convertView, position);
                break;
            case SELECT_SUB:
                itemHolderSelectSubStyle(subtitleHolder, convertView, position);
                break;
            case SELECT_PLAY:
                itemHolderSelectPlayStyle(subtitleHolder, convertView, position);
                break;
        }
        if (selectedPosition != null && selectedPosition.length > 0) {
            if (flag) {
                subtitleHolder.itemSubtitleChinese.setTextColor(Color.parseColor("#000000"));
                subtitleHolder.itemSubtitleEnglish.setTextColor(Color.parseColor("#000000"));
                for (int i = 0; i < selectedPosition.length; i++) {
                    if (selectedPosition[i] == position + 1) {
                        subtitleHolder.itemSubtitleEnglish.setTextColor(Color.parseColor("#ff6501"));
                        subtitleHolder.itemSubtitleChinese.setTextColor(Color.parseColor("#ff6501"));
                    }
                }
            }
        }
        if (activity.getLearnSelectSubtitleList() != null) {
            for (SubtitleBean subtitleBean : activity.getLearnSelectSubtitleList()) {
                if (subtitleBean.getItem() == subtitleBeanList.get(position).getItem()) {
                    if (subtitleBean.isSelect()) {
                        subtitleHolder.itemCheckBox02.setImageResource(R.drawable.srtbody_select_f);
                    }
                }
            }
        }

        return convertView;
    }

    /**
     * 初始化Item数据显示
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    private void initItemData(SubtitleHolder subtitleHolder, View convertView, final int position) {
        switch (PlayConfig.shType) {
            case NONE:
                subtitleHolder.itemSubtitleChinese.setVisibility(View.VISIBLE);
                subtitleHolder.itemSubtitleEnglish.setVisibility(View.VISIBLE);
                break;
            case CHINESE:
                subtitleHolder.itemSubtitleChinese.setVisibility(View.GONE);
                subtitleHolder.itemSubtitleEnglish.setVisibility(View.VISIBLE);
                break;
            case ENGLISH:
                subtitleHolder.itemSubtitleChinese.setVisibility(View.VISIBLE);
                subtitleHolder.itemSubtitleEnglish.setVisibility(View.GONE);
                break;
            default:
                subtitleHolder.itemSubtitleChinese.setVisibility(View.INVISIBLE);
                subtitleHolder.itemSubtitleEnglish.setVisibility(View.INVISIBLE);
                break;
        }
        switch (PlayConfig.movietextSize) {
            case 18:
                subtitleHolder.itemSubtitleChinese.setTextSize(12);
                subtitleHolder.itemSubtitleEnglish.setTextSize(18);
                break;
            case 22:
                subtitleHolder.itemSubtitleChinese.setTextSize(12);
                subtitleHolder.itemSubtitleEnglish.setTextSize(22);
                break;
            default:
                subtitleHolder.itemSubtitleChinese.setTextSize(12);
                subtitleHolder.itemSubtitleEnglish.setTextSize(20);
                break;
        }

        SubtitleBean subtitleBean = subtitleBeanList.get(position);
        if (subtitleBean.getChinese() != null && subtitleBean.getChinese().length() > 0) {
            subtitleHolder.itemSubtitleChinese.setText(subtitleBean.getChinese());
        } else {
            subtitleHolder.itemSubtitleChinese.setText("");
        }

        if (subtitleBean.getEnglish() != null && subtitleBean.getEnglish().length() > 0) {
            subtitleHolder.itemSubtitleEnglish.setText(subtitleBean.getEnglish());
        } else {
            subtitleHolder.itemSubtitleEnglish.setText("");
        }
    }

    /**
     * 修改Item Holder显示样式
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    private void changeItemHolderStyle(SubtitleHolder subtitleHolder, View convertView, final int position) {
        subtitleHolder.changeHolderShowStyle(subtitleBeanList.get(position), PlayConfig.hoType);
    }

    /**
     * 修改Item Holder显示样式
     *
     * @param opType
     */
    public void changeItemHolderStyle(PlayConfig.OpType opType) {
        switch (opType) {
            case ROLE_PLAY:
                if (rolePlayBeginMap.size() > 0) {
                    rolePlayBeginMap.clear();
                }
                if (rolePlayEndMap.size() > 0) {
                    rolePlayEndMap.clear();
                }
                PlayConfig.hoType = HoType.ROLE_BEGIN;
                break;
            case SUBTITLE_SELECT:
                if (selectSubtitleMap.size() > 0) {
                    selectSubtitleMap.clear();
                }
                PlayConfig.hoType = HoType.SELECT_SUB;
                break;
            default:
                PlayConfig.hoType = HoType.PLAY;
                break;
        }
        notifyDataSetChanged();
    }

    /**
     * Item Holder视频播放
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    private void itemHolderPlayStyle(SubtitleHolder subtitleHolder, View convertView, final int position) {
        subtitleHolder.itemPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(v, 1, position);
                }
            }
        });
        subtitleHolder.itemSubtitleChinese.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubtitleItemPlayListener != null && PlayConfig.opType == OpType.NONE) {
                    onSubtitleItemPlayListener.itemClickPlay(v, 2, position);
                }
            }
        });
        subtitleHolder.itemSubtitleEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubtitleItemPlayListener != null && PlayConfig.opType == OpType.NONE) {
                    onSubtitleItemPlayListener.itemClickPlay(v, 2, position);
                }
            }
        });
    }


    List<SubtitleBean> list = new ArrayList<>();
    private Map<Integer, SubtitleBean> map;

    int posi;

    public void setPosi(int position) {
        this.posi = position;
    }

    public int getPosi() {
        return posi;
    }


    /**
     * Item Holder角色扮演选择首句
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    //选择首句
    private void itemHolderRoleBeginStyle(SubtitleHolder subtitleHolder, final View convertView, final int position) {
        SubtitleHolder holder = (SubtitleHolder) convertView.getTag();
        final int position_2 = (int) holder.itemCheckBox01.getTag();
        final SubtitleBean subtitleBean = subtitleBeanList.get(position_2);
        if (subtitleBean.isRolePlay()) {
            subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_f);
        } else {
            subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
        }
        holder.itemSubtitleChinese.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(view, 1, position);
                }
            }
        });
        holder.itemSubtitleEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(view, 1, position);
                }
            }
        });
        subtitleHolder.itemCheckBox01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bsCallback.callBack();
                if (beginPosition == -1) {
                    beginPosition = (int) view.getTag();
                    int pos = (int) view.getTag();
                    if (pos != 0) {
                        SubtitleBean subtitleBean1 = subtitleBeanList.get(pos - 1);
                        String ZZ = subtitleBean1.getEnglish();
                        boolean BIAO = false;
                        if (subtitleBean1.getEnglish().length() > 0) {
                            char c = ZZ.charAt(subtitleBean1.getEnglish().length() - 1);
                            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                                BIAO = true;
                            } else {
                                BIAO = false;
                            }
                        }
                        if (subtitleBean1.getEnglish().endsWith("...") || subtitleBean1.getEnglish().endsWith(",") || BIAO == true) {
                            beginPosition = pos - 1;
                        }
                    }
                    SubtitleBean subtitleBean = subtitleBeanList.get(beginPosition);
                    subtitleBean.setRolePlay(true);
                    PlayConfig.hoType = HoType.ROLE_END;

                } else {
                    if (endPosition2 == -1) {
                        return;
                    }
                    int position = (int) view.getTag();
                    if (endPosition == -1 && position >= endPosition2) {
                        if (rolePlayEndMap.size() <= 0) {
                            return;
                        }
                        endPosition = (int) view.getTag();
                        int pos = (int) view.getTag();
                        if (pos != subtitleBeanList.size() - 1) {
                            SubtitleBean subtitleBean1 = subtitleBeanList.get(pos);
                            String ZZ = subtitleBean1.getEnglish();
                            boolean BIAO = false;
                            if (subtitleBean1.getEnglish().length() > 0) {
                                char c = ZZ.charAt(subtitleBean1.getEnglish().length() - 1);
                                if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                                    BIAO = true;
                                } else {
                                    BIAO = false;
                                }
                            }
                            if (subtitleBean1.getEnglish().endsWith("...") || subtitleBean1.getEnglish().endsWith(",") || BIAO == true) {
                                endPosition = pos + 1;
                            }
                        }
                        for (int i = beginPosition; i <= endPosition; i++) {
                            SubtitleBean subtitleBean = subtitleBeanList.get(i);
                            subtitleBean.setRolePlay(true);
                            map = new LinkedHashMap<>();
                            map.put(subtitleBeanList.get(i).getItem(), subtitleBeanList.get(i));
                            for (int key : map.keySet()) {
                                list.add(map.get(key));
                            }
                            rolePlayBeginMap.put(subtitleBeanList.get(i).getItem(), rolePlayBeginMap.get(i));
                        }
                    } else {
                        return;
                    }
                    callBack.callBack(list);

                }
                notifyDataSetChanged();
                if (onSubtitleRolePlayListener != null) {//2
                    onSubtitleRolePlayListener.subtitleRolePlayEnd(getRolePlayEndSubtitleList(), endPosition);
                }
            }
        });

        notifyDataSetChanged();
        if (onSubtitleRolePlayListener != null) {
            onSubtitleRolePlayListener.subtitleRolePlayBegin(getRolePlayBeginSubtitleList());
        }
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * 获取角色扮演首句
     *
     * @return
     */
    private List<SubtitleBean> getRolePlayBeginSubtitleList() {
        List<SubtitleBean> rolePlaySubtiteList = new ArrayList<>();

        for (Integer key : rolePlayBeginMap.keySet()) {
            rolePlaySubtiteList.add(rolePlayBeginMap.get(key));
        }
        return rolePlaySubtiteList;
    }

    /**
     * 清除角色扮演选择
     */
    public void clearRolePlayFirst() {
        if (rolePlayBeginMap != null) {
            rolePlayBeginMap.clear();
            rolePlayEndMap.clear();
            notifyDataSetChanged();
            lvPosition = -1;
        }
        PlayConfig.hoType = HoType.ROLE_BEGIN;
        if (onSubtitleRolePlayListener != null) {
            onSubtitleRolePlayListener.subtitleRolePlayBegin(null);
        }
    }

    /**
     * 清除角色扮演尾句
     */
    public void clearRolePlayTail() {
        if (rolePlayBeginMap == null || rolePlayBeginMap.size() <= 1 || rolePlayEndMap == null) {
            return;
        }
        int item = -1;
        if (rolePlayBeginType == 1) {
            int count = 0;
            for (int key : rolePlayBeginMap.keySet()) {
                count++;
                if (count == rolePlayBeginMap.size()) {
                    item = key;
                    break;
                }
            }
        } else if (rolePlayBeginType == 2) {
            for (int key : rolePlayBeginMap.keySet()) {
                item = key;
                if (item != -1) {
                    break;
                }
            }
        }
        if (item != -1) {
            if (rolePlayBeginMap.containsKey(item)) {
                rolePlayBeginMap.clear();
                rolePlayBeginMap.put(item, subtitleBeanList.get(item));
            }
            if (rolePlayEndMap.containsKey(item)) {
                rolePlayEndMap.clear();
            }
        }

        if (onSubtitleRolePlayListener != null) {
            onSubtitleRolePlayListener.subtitleRolePlayBegin(null);
        }
        PlayConfig.hoType = HoType.ROLE_END;
        notifyDataSetChanged();
    }

    /**
     * Item Holder角色扮演选择尾句
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */

    public boolean flag2 = false;
    public boolean flag3 = false;
    public boolean flag4 = false;
    public boolean flag5 = false;

    private void itemHolderRoleEndStyle(final SubtitleHolder subtitleHolder, View convertView, final int position) {
        final SubtitleBean subtitleBean = subtitleBeanList.get(position);
        subtitleHolder.itemCheckBox02.setVisibility(View.VISIBLE);

        subtitleHolder.itemSubtitleChinese.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(view, 1, position);
                }
            }
        });
        subtitleHolder.itemSubtitleEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(view, 1, position);
                }
            }
        });

        if (position >= beginPosition) {
            if (endPosition == -1) {
                if (position == beginPosition) {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_f);
                    flag5 = false;
                } else {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
                }
            } else {
                if (position >= beginPosition && position <= endPosition) {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_f);
                    if (flag4 == false) {
                        flag5 = !flag5;
                    }
                    flag4 = true;
                } else {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
                    subtitleHolder.itemCheckBox02.setVisibility(View.GONE);
                }
            }
        } else {
            subtitleHolder.itemCheckBox02.setVisibility(View.GONE);
        }

        if (subtitleBean.isRecord()) {
            subtitleHolder.itemCheckBox02.setImageResource(R.drawable.srtbody_select_f);
            subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_f);
        } else {
            subtitleHolder.itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
        }
        if (flag2) {
            if (position >= beginPosition) {
                if (position >= beginPosition && position <= lvPosition) {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_f);
                } else if (lvPosition >= 0 && flag4 == false) {
                    subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
                }
                if (position == lvPosition) {
                    flag2 = false;
                }
            }
        }
        subtitleHolder.itemCheckBox02.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lvPosition = position;
                flag2 = true;

                if (subtitleBean.isRecord()) {
                    if (position >= 1) {
                        SubtitleBean subtitleBean1 = subtitleBeanList.get(position - 1);
                        String ZZ = subtitleBean1.getEnglish().trim();
                        boolean BIAO;
                        if (subtitleBean1.getEnglish().length() > 0) {
                            char c = ZZ.charAt(subtitleBean1.getEnglish().length() - 1);
                            BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));
                            if (BIAO) {
                                char c1 = subtitleBean.getEnglish().trim().charAt(0);
                                if (c1 >= 48 && c1 <= 57 || c1 >= 97 && c1 <= 122) {
                                    SubtitleBean subtitleBean2 = subtitleBeanList.get(position - 1);
                                    rolePlayEndMap.remove(subtitleBean2.getItem());
                                    subtitleBean2.setRecord(false);
                                    for (int i = 0; i < endPositions.size(); i++) {
                                        if (position - 1 == endPositions.get(i)) {
                                            endPositions.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (position <= subtitleBeanList.size() - 1) {
                        String ZZ = subtitleBean.getEnglish().trim();
                        boolean BIAO;
                        if (ZZ.length() > 0) {
                            char c = ZZ.charAt(ZZ.length() - 1);
                            BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));
                            if (BIAO) {
                                SubtitleBean subtitleBean2 = subtitleBeanList.get(position + 1);
                                char c1 = subtitleBean2.getEnglish().trim().charAt(0);
                                if (c1 >= 48 && c1 <= 57 || c1 >= 97 && c1 <= 122) {
                                    rolePlayEndMap.remove(subtitleBean2.getItem());
                                    subtitleBean2.setRecord(false);
                                    for (int i = 0; i < endPositions.size(); i++) {
                                        if (position + 1 == endPositions.get(i)) {
                                            endPositions.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    rolePlayEndMap.remove(subtitleBean.getItem());
                    subtitleBean.setRecord(false);
                    if (flag2 == true) {
                        if (position >= beginPosition) {
                            if (position >= beginPosition && position <= lvPosition) {
                                subtitleHolder.itemCheckBox01.setImageResource(R.drawable.srtparagraph_select_n);
                            }
                            if (position == lvPosition) {
                                flag3 = true;
                                flag2 = false;
                            }
                        }
                    }
                    for (int i = 0; i < endPositions.size(); i++) {
                        if (position == endPositions.get(i)) {
                            endPositions.remove(i);
                        }
                    }

                } else {
                    /**
                     * 关联上一句
                     * */
                    if (position >= 0) {
                        SubtitleBean subtitleBean1 = subtitleBeanList.get(position - 1);
                        String ZZ = subtitleBean1.getEnglish().trim();
                        boolean BIAO;
                        if (ZZ.length() != 0) {
                            char c = ZZ.charAt(ZZ.length() - 1);
                            BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));
                            if (BIAO) {
                                char c1 = subtitleBeanList.get(position).getEnglish().trim().charAt(0);
                                if (c1 >= 48 && c1 <= 57 || c1 >= 97 && c1 <= 122) {
                                    subtitleBean1.setRecord(true);
                                    rolePlayEndMap.put(subtitleBean1.getItem(), subtitleBean1);
                                    endPositions.add(position);
                                }
                            }
                        }
                    }
                    /**
                     * 关联下一句
                     * */
                    if (position <= subtitleBeanList.size() - 1) {
                        String ZZ = subtitleBean.getEnglish().trim();
                        boolean BIAO;
                        if (ZZ.length() > 0) {
                            char c = ZZ.charAt(ZZ.length() - 1);
                            BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));

                            if (BIAO) {
                                SubtitleBean subtitleBean2 = subtitleBeanList.get(position + 1);
                                char c1 = subtitleBean2.getEnglish().trim().charAt(0);

                                if (c1 >= 48 && c1 <= 57 || c1 >= 97 && c1 <= 122) {
                                    subtitleBean2.setRecord(true);
                                    rolePlayEndMap.put(subtitleBean2.getItem(), subtitleBean2);
                                    endPositions.add(position);
                                }
                            }
                        }
                    }
                    subtitleBean.setRecord(true);
                    flag3 = false;
                    rolePlayEndMap.put(subtitleBean.getItem(), subtitleBean);
                    endPositions.add(position);
                }
                endPosition2 = getEndPosttion2();
                notifyDataSetChanged();
                if (onSubtitleRolePlayListener != null) {//3
                    onSubtitleRolePlayListener.subtitleRolePlayEnd(getRolePlayEndSubtitleList(), endPosition);
                }
            }
        });
    }

    public void callToWindow() {
        onSubtitleRolePlayListener.subtitleRolePlayEnd(getRolePlayEndSubtitleList(), -1);
    }

    private int getEndPosttion2() {
        int endPosttion2 = -1;
        if (endPositions.size() == 0) {
            return -1;
        } else {
            for (int enPosition2 : endPositions) {
                if (enPosition2 > endPosttion2) {
                    endPosttion2 = enPosition2;
                }
            }
        }
        return endPosttion2;
    }

    /**
     * 获取角色扮演尾句
     *
     * @return
     */
    private List<SubtitleBean> getRolePlayEndSubtitleList() {
        List<SubtitleBean> rolePlaySubtiteList = new ArrayList<>();

        for (int key : rolePlayEndMap.keySet()) {
            rolePlaySubtiteList.add(rolePlayEndMap.get(key));
        }

        if (rolePlaySubtiteList.size() > 1) {
            for (int i = 0; i < rolePlaySubtiteList.size() - 1; i++) {
                for (int j = 0; j < rolePlaySubtiteList.size() - i - 1; j++) {
                    if (rolePlaySubtiteList.get(j).getItem() > rolePlaySubtiteList.get(j + 1).getItem()) {
                        SubtitleBean subtitleBean = rolePlaySubtiteList.remove(j + 1);
                        rolePlaySubtiteList.add(j, subtitleBean);
                    }
                }
            }
        }
        return rolePlaySubtiteList;
    }

    /**
     * Item Holder角色扮演语句录音
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    private void itemHolderRoleRecordStyle(SubtitleHolder subtitleHolder, View convertView, final int position) {

    }

    /**
     * Item Holder选择字幕
     *
     * @param subtitleHolder
     * @param convertView
     * @param position
     */
    private ArrayList<Integer> selectPositions = new ArrayList<>();

    private void itemHolderSelectSubStyle(final SubtitleHolder subtitleHolder, View convertView, final int position) {
        subtitleHolder.itemPlay.setVisibility(View.VISIBLE);
        subtitleHolder.itemPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubtitleItemPlayListener != null) {
                    onSubtitleItemPlayListener.itemClickPlay(v, 1, position);
                }
            }
        });
        subtitleHolder.itemSubtitleChinese.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubtitleItemPromptListener != null) {
                    onSubtitleItemPromptListener.itemClickPrompt(v, position);
                }
            }
        });
        subtitleHolder.itemSubtitleEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = subtitleBeanList.get(position).getEnglish();
                subtitlePromptWindow = new SubtitlePromptWindow(activity);
                if (SharedPreTool.getSharedPreDateBoolean(activity, PlayConfig.AUTO_INPUT, true)) {
                    DictionaryTranslationActivity.startActivity(activity, DictionaryTranslationActivity.TranslateType.word, s, 2);
                } else {
                    subtitlePromptWindow.setSubtitle(s);
                    subtitlePromptWindow.showWindow();
                }
            }
        });
        final SubtitleBean subtitleBean = subtitleBeanList.get(position);

        if (activity.getLearnSelectSubtitleList() != null && activity.getLearnSelectSubtitleList().size() > 0) {
            for (int i = 0; i < activity.getLearnSelectSubtitleList().size(); i++) {
                if (subtitleBean.getItem() == activity.getLearnSelectSubtitleList().get(i).getItem()) {
                    subtitleBean.setSelect(true);
                } else {
                    subtitleBean.setSelect(false);
                }
            }
        } else if (selectSubtitleMap != null && selectSubtitleMap.size() > 0) {
            if (selectSubtitleMap.containsKey(subtitleBean.getItem())) {
                subtitleHolder.itemCheckBox02.setImageResource(R.drawable.srtbody_select_f);
                subtitleBean.setSelect(true);
            } else {
                subtitleHolder.itemCheckBox02.setImageResource(R.drawable.srtbody_select_n);
                subtitleBean.setSelect(false);
            }
        }
        subtitleHolder.itemCheckBox02.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getLearnSelectSubtitleList() != null && activity.getLearnSelectSubtitleList().size() > 0) {
                    for (int i = 0; i < activity.getLearnSelectSubtitleList().size(); i++) {
                        selectSubtitleMap.put(activity.getLearnSelectSubtitleList().get(i).getItem(), activity.getLearnSelectSubtitleList().get(i));
                    }
                    activity.getLearnSelectSubtitleList().clear();
                }
                if (selectSubtitleMap != null) {
                    if (selectSubtitleMap.containsKey(subtitleBean.getItem())) {
                        selectSubtitle(position, 1);
                        subtitleBeanList.get(position).setRecord(false);
                        subtitleBeanList.get(position).setSelect(false);
                        notifyDataSetChanged();
                    } else {
                        selectSubtitle(position, 0);
                        subtitleBeanList.get(position).setRecord(true);
                        subtitleBeanList.get(position).setSelect(true);
                        notifyDataSetChanged();
                    }
                } else {
                    notifyDataSetChanged();
                }
                onSubtitleSelectListener.callbackSubtitleSelectList(getSelectSubtitleList());
            }
        });
    }

    private String returnString(int position, String s) {
        String ZZ = subtitleBeanList.get(position - 1).getEnglish();
        boolean BIAO = false;
        if (subtitleBeanList.get(position - 1).getEnglish().length() > 0) {
            char c = ZZ.charAt(subtitleBeanList.get(position - 1).getEnglish().length() - 1);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                BIAO = true;
            } else {
                BIAO = false;
            }
        }
        if ((position - 1) >= 0 && (subtitleBeanList.get(position - 1).getEnglish().endsWith("...") || subtitleBeanList.get(position - 1).getEnglish().endsWith(",")) || BIAO == true) {
            s = subtitleBeanList.get(position - 1).getEnglish().replaceAll("\\.\\.\\.", " ") + s.replaceAll("\\.\\.\\.", " ");
        }
        s.replaceAll("\\.\\.\\.", " ");
        return s;
    }

    /**
     * 9527
     */
    private void selectSubtitle(int position, int opt) {
        SubtitleBean subtitleBean = subtitleBeanList.get(position);
        if (selectSubtitleMap.containsKey(subtitleBean.getItem())) {
            if (opt == 0) {
                return;
            }
            flagflag = 0;
            selectSubtitleMap.remove(subtitleBean.getItem());

        } else {
            if (opt == 1) {
                return;
            }
            if (SharedPreTool.getSharedPreDateBoolean(activity, PlayConfig.CHOICE_MODEL, true)) {
                flagflag = 1;
                if (subtitleBean.getEnglish().length() > 0) {
                    selectSubtitleMap.put(subtitleBean.getItem(), subtitleBean);
                } else {
                    Toast.makeText(activity, "请选择英文句子", Toast.LENGTH_SHORT).show();
                }
                a = subtitleBean.getItem();
                String ZZ = subtitleBean.getEnglish().trim();
                boolean BIAO = false;
                if (subtitleBean.getEnglish().length() > 0) {
                    char c11 = ZZ.charAt(0);
                    char c22 = ZZ.charAt(1);
                    if (c11 == '-') {
                        BIAO = (c22 >= 'a' && c22 <= 'z');
                    } else {
                        BIAO = (c11 >= 'a' && c11 <= 'z');
                    }
                }
                if (BIAO) {
                    //判断上一句
                    if ((position - 1) > 0) {
                        String ZZ2 = subtitleBeanList.get(position - 1).getEnglish();//上一句
                        boolean BIAO2 = false;
                        if (ZZ2.length() > 0) {
                            char c2 = ZZ2.charAt(ZZ2.length() - 1);
                            BIAO2 = (c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || ZZ2.endsWith("...") || ZZ2.endsWith(","));
                        }
                        if (BIAO2) {
                            selectSubtitle(position - 1, opt);
                        }
                    }
                    /**
                     * 下一句
                     * */
                    char c = ZZ.charAt(subtitleBean.getEnglish().length() - 1);
                    BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));

                    if ((position + 1) < subtitleBeanList.size() && BIAO) {
                        String ZZ3 = subtitleBeanList.get(position + 1).getEnglish();
                        boolean BIAO3 = false;
                        if (subtitleBeanList.get(position + 1).getEnglish().length() > 0) {
                            char c3 = ZZ3.charAt(0);
                            char c33 = ZZ3.charAt(1);
                            if (c3 == '-') {
                                BIAO3 = (c33 >= 'a' && c33 <= 'z');
                            } else {
                                BIAO3 = (c3 >= 'a' && c3 <= 'z');
                            }
                        }
                        if (BIAO3) {
                            selectSubtitle(position + 1, opt);
                        }
                    }
                } else {
                    if (subtitleBean.getEnglish().length() > 0) {
                        char c = ZZ.charAt(subtitleBean.getEnglish().length() - 1);
                        BIAO = (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || ZZ.endsWith("...") || ZZ.endsWith(","));
                    }
                    if (position != 0) {
                        String ZZ4 = subtitleBeanList.get(position + 1).getEnglish();
                        boolean BIAO4;
                        if (subtitleBeanList.get(position + 1).getEnglish().length() > 0) {
                            char c4 = ZZ4.charAt(0);
                            char c44 = ZZ4.charAt(1);
                            if (c4 == '-') {
                                BIAO4 = (c44 >= 'a' && c44 <= 'z');
                            } else {
                                BIAO4 = (c4 >= 'a' && c4 <= 'z');
                            }
                            if (BIAO4 && BIAO) {
                                selectSubtitle(position + 1, opt);
                            }
                        }
                    }
                }
            } else {
                selectSubtitleMap.clear();
                if (subtitleBean.getEnglish().length() > 0) {
                    selectSubtitleMap.put(subtitleBean.getItem(), subtitleBean);
                } else {
                    Toast.makeText(activity, "请选择英文句子", Toast.LENGTH_SHORT).show();
                }
                a = subtitleBean.getItem();
                String ZZ = subtitleBean.getEnglish();
                boolean BIAO = false;
                if (subtitleBean.getEnglish().length() > 0) {
                    char c = ZZ.charAt(subtitleBean.getEnglish().length() - 1);
                    if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                        BIAO = true;
                    } else {
                        BIAO = false;
                    }
                }
                if (subtitleBean.getEnglish().endsWith("...") || subtitleBean.getEnglish().endsWith(",") || BIAO == true
                        ) {
                    String ZZ2 = subtitleBeanList.get(position - 1).getEnglish();
                    boolean BIAO2 = false;
                    if (subtitleBeanList.get(position - 1).getEnglish().length() > 0) {
                        char c2 = ZZ2.charAt(subtitleBeanList.get(position - 1).getEnglish().length() - 1);
                        if (c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || c2 >= '0' && c2 <= '9') {
                            BIAO2 = true;
                        } else {
                            BIAO2 = false;
                        }
                    }
                    if ((position - 1) > 0 && (subtitleBeanList.get(position - 1).getEnglish().endsWith("...") || subtitleBeanList.get(position - 1).getEnglish().endsWith(",") || BIAO2 == true
                    )) {
                        selectSubtitle(position - 1, opt);
                    }
                    if ((position + 1) < subtitleBeanList.size()) {
                        int item = position + 1;
                        String ZZ3 = subtitleBeanList.get(item).getEnglish().trim();
                        boolean BIAO3 = false;
                        if (subtitleBeanList.get(item).getEnglish().length() > 0) {
                            char c3 = ZZ3.charAt(0);
                            if (c3 >= 'a' && c3 <= 'z' || c3 >= '0' && c3 <= '9' || isUpper(ZZ3)) {
                                BIAO3 = true;
                            } else {
                                BIAO3 = false;
                            }
                        }
                        if (subtitleBeanList.get(position).getEnglish().endsWith("...") || subtitleBeanList.get(position).getEnglish().endsWith(",") || BIAO3 == true) {
                            selectSubtitle(item, opt);
                        } else {
                            if (subtitleBeanList.get(item).getEnglish().length() > 0) {
                                selectSubtitleMap.put(subtitleBeanList.get(item).getItem(), subtitleBeanList.get(item));
                            }
                        }
                    }
                } else {
                    String ZZ4 = subtitleBeanList.get(position - 1).getEnglish();
                    boolean BIAO4 = false;
                    if ((position - 1) > 0 && subtitleBeanList.get(position - 1).getEnglish().length() > 0) {
                        char c4 = ZZ4.charAt(subtitleBeanList.get(position - 1).getEnglish().length() - 1);
                        if (c4 >= 'A' && c4 <= 'Z' || c4 >= 'a' && c4 <= 'z' || c4 >= '0' && c4 <= '9') {
                            BIAO4 = true;
                        } else {
                            BIAO4 = false;
                        }
                    }
                    if ((position - 1) >= 0 && (subtitleBeanList.get(position - 1).getEnglish().endsWith("...") || subtitleBeanList.get(position - 1).getEnglish().endsWith(",") || BIAO4 == true
                    )) {
                        selectSubtitle(position - 1, opt);
                    }

                }
            }

        }
    }

    private boolean isUpper(String s) {
        boolean b = false;
        if (s != null && !s.equals("") && s.length() > 0) {
            s = s.trim();
            char[] chars = s.toCharArray();
            StringBuilder buffer = new StringBuilder();
            for (char a : chars) {
                if (a != 32) {
                    buffer.append(a);
                }
            }
            String str = buffer.toString();
            if (str.length() > 4) {
                if ((str.charAt(0) == 46 && str.charAt(1) == 46 && str.charAt(2) == 46 && str.charAt(3) >= 'a' && str.charAt(0) <= 'z')
                        || ((str.charAt(0) == 45 && str.charAt(1) >= 'a' && str.charAt(1) <= 'z')
                        || (str.charAt(0) >= 'a' && str.charAt(0) <= 'z'))) {
                    b = true;
                } else if (str.charAt(0) >= 'a' && str.charAt(0) <= 'z') {
                    b = true;
                }
            } else {
                if (!(str.charAt(0) == '-' || str.charAt(0) >= 'A' && str.charAt(0) <= 'Z')) {
                    b = true;
                }
            }
        }
        return b;
    }

    List<SubtitleBean> selectSubtitleList2 = new ArrayList<>();
    public int[] mapArray;
    int a = 0;
    public int mapIndex = 0;
    public boolean mapFlag = false;
    public int flagflag = 0;

    /**
     * 获取字幕选择列表
     *
     * @return
     */
    public List<SubtitleBean> getSelectSubtitleList() {
        List<SubtitleBean> selectSubtitleList = null;
        if (selectSubtitleMap.size() > 0) {
            selectSubtitleList = new ArrayList<>();
            if (SharedPreTool.getSharedPreDateBoolean(activity, PlayConfig.CHOICE_MODEL, true)) {
                if (flagflag == 1) {
                    mapArray = new int[selectSubtitleMap.size()];
                    for (Integer key : selectSubtitleMap.keySet()) {
                        mapArray[mapIndex] = selectSubtitleMap.get(key).getItem();
                        mapIndex += 1;
                    }
                    mapIndex = 0;
                    if (mapArray.length > 0) {
                        for (int i = 1; i < mapArray.length; i++) {
                            for (int j = 0; j < mapArray.length - i; j++) {
                                if (mapArray[j] > mapArray[j + 1]) {
                                    int temp = mapArray[j];
                                    mapArray[j] = mapArray[j + 1];
                                    mapArray[j + 1] = temp;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < mapArray.length; i++) {
                        if ((i + 1) < mapArray.length) {
                            if (mapArray[i] + 1 != mapArray[i + 1]) {
                                mapFlag = true;
                            }
                        }
                    }
                    List delList = new ArrayList();
                    if (mapFlag == true) {
                        for (int i = 0; i < selectSubtitleList2.size(); i++) {
                            for (Integer key : selectSubtitleMap.keySet()) {
                                if (selectSubtitleMap.get(key).getItem() == selectSubtitleList2.get(i).getItem()) {
                                    delList.add(key);
                                }
                            }
                        }
                        for (int i = 0; i < delList.size(); i++) {
                            selectSubtitleMap.remove(delList.get(i));
                        }
                    }
                    mapFlag = false;
                }
            }
            for (Integer key : selectSubtitleMap.keySet()) {
                selectSubtitleList.add(selectSubtitleMap.get(key));
            }
            if (selectSubtitleList.size() > 0) {
                for (int i = 0; i < selectSubtitleList.size() - 1; i++) {
                    for (int j = 0; j < selectSubtitleList.size() - i - 1; j++) {
                        if (selectSubtitleList.get(j).getItem() > selectSubtitleList.get(j + 1).getItem()) {
                            SubtitleBean subtitleBean = selectSubtitleList.remove(j + 1);
                            selectSubtitleList.add(j, subtitleBean);
                        }
                    }
                }
            }
            selectSubtitleList2.clear();
            selectSubtitleList2.addAll(selectSubtitleList);
        }
        return selectSubtitleList;
    }
    private void itemHolderSelectPlayStyle(SubtitleHolder subtitleHolder, View convertView, final int position) {
        //subtitleHolder.itemSubtitleChinese.setcol

    }
    public void setDataList(List<SubtitleBean> dataList) {
        this.subtitleBeanList = dataList;
    }
    public List<SubtitleBean> getList() {
        List<SubtitleBean> rolePlaySubtiteList2 = new ArrayList<>();
        for (int i = beginPosition; i <= endPosition; i++) {
            if (i < 0)
                break;
            rolePlaySubtiteList2.add(subtitleBeanList.get(i));
        }
        return rolePlaySubtiteList2;
    }
    private int[] selectedPosition;
    public boolean flag;
    public void clearSelection(int[] item, boolean flag) {
        this.flag = flag;
        selectedPosition = item;
    }
}
