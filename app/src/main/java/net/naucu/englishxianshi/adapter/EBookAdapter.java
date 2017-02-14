package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ebook.EBook;
import net.naucu.englishxianshi.ui.EBookActivity;
import net.naucu.englishxianshi.ui.video.EBooksCallBack;

import java.util.List;

/**
 * @author fght 电子书适配器
 */
public class EBookAdapter extends BaseAdapter {
    private Context context;
    private List<EBook> ebook_map;
    private boolean showChinese = true;
    private boolean showEnglish = true;
    private int textSize = 14;
    private int currentPosition;
    public static boolean bool = false;
    private EBookActivity eBookActivity;
    private EBooksCallBack callBack;
    public String discolorationText;
    public String word;
    private Typeface faceCn;
    private Typeface faceEn;

    public EBookAdapter(Context context, List<EBook> ebook_map, EBookActivity eBookActivity, EBooksCallBack callBack) {
        this.eBookActivity = eBookActivity;
        this.context = context;
        this.ebook_map = ebook_map;
        this.callBack = callBack;
        faceCn = Typeface.createFromAsset(context.getAssets(), "fonts/default.ttf");
        faceEn = Typeface.createFromAsset(context.getAssets(), "fonts/defaultEng.ttf");
    }

    public void showChinese(boolean showChinese) {
        this.showChinese = showChinese;
        notifyDataSetChanged();
    }

    public void showEnglish(boolean showEnglish) {
        this.showEnglish = showEnglish;
        notifyDataSetChanged();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        notifyDataSetChanged();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCount() {
        return ebook_map == null ? 0 : ebook_map.size();
    }

    @Override
    public Object getItem(int position) {
        return ebook_map.get(position).getEbookBodyE();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int index = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final EBook ebook = ebook_map.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ebook, parent, false);
            holder = new ViewHolder();
            holder.chinesedET = (TextView) convertView.findViewById(R.id.ewe_text_chinese);
            holder.englishldET = (TextView) convertView.findViewById(R.id.ewe_text_english);
            holder.chinesedET.setTypeface(faceCn);
            holder.englishldET.setTypeface(faceEn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.englishldET.setText("");
        holder.chinesedET.setText("");
        if (ebook != null) {
            if (showEnglish) {
                String english = ebook.getEbookBodyE();
                if (english.toString().indexOf("<!---->") != -1) {
                    english = english.replaceAll("<!---->", "");
                }
                holder.englishldET.setText("\u3000\u3000" + english, TextView.BufferType.SPANNABLE);
            }
            if (showChinese && ebook.getEbookBodyC() != null && !ebook.getEbookBodyC().equals("")) {
                String chinese = ebook.getEbookBodyC();
                if (chinese.toString().indexOf("<!---->") != -1) {
                    chinese = chinese.replaceAll("<!---->", "");
                }
                holder.chinesedET.setText("\u3000\u3000" + chinese);
            }
            if (textSize == 14) {
                holder.chinesedET.setTextSize(12);
                holder.englishldET.setTextSize(14);
            } else if (textSize == 18) {
                holder.chinesedET.setTextSize(12);
                holder.englishldET.setTextSize(16);
            } else {
                holder.chinesedET.setTextSize(12);
                holder.englishldET.setTextSize(18);
            }
            if (position == currentPosition) {
                if (discolorationText != null) {
                    int startIndex = holder.englishldET.getText().toString().indexOf(discolorationText);
                    int endIndex = startIndex + discolorationText.length();
                    if (startIndex >= 0 && endIndex >= 0) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(holder.englishldET.getText().toString());
                        builder.setSpan(new ForegroundColorSpan(0xff2290dd), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (startIndex != 0) {
                            builder.setSpan(new ForegroundColorSpan(0xff000000), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (endIndex != holder.englishldET.getText().length()) {
                            builder.setSpan(new ForegroundColorSpan(0xff000000), endIndex, holder.englishldET.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        holder.englishldET.setText(builder);
                    }
                    eBookActivity.setS(discolorationText);
                }
                TextPaint Paint = holder.englishldET.getPaint();
                Paint.setFakeBoldText(true);
                if (textSize == 14) {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(15);
                } else if (textSize == 18) {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(17);
                } else {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(19);
                }

            } else {
                holder.chinesedET.setTextColor(0xff000000);
                holder.englishldET.setTextColor(0xff000000);
                TextPaint Paint = holder.englishldET.getPaint();
                Paint.setFakeBoldText(false);
                if (textSize == 14) {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(14);
                } else if (textSize == 18) {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(16);
                } else {
                    holder.chinesedET.setTextSize(12);
                    holder.englishldET.setTextSize(18);
                }
            }
            holder.englishldET.setTag(position);
            holder.chinesedET.setTag(position);
            holder.englishldET.setFocusable(false);
            holder.chinesedET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "不在有效范围内", Toast.LENGTH_SHORT).show();
                    callBack.callBack(null, 0);
                }
            });

            holder.englishldET.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int pos = (int) v.getTag();
                    if (pos != currentPosition) {
                        discolorationText = null;
                        index = 0;
                        return false;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Layout layout = (Layout) holder.englishldET.getLayout();
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            if (layout != null) {
                                int line = layout.getLineForVertical(y);
                                int characterOffset = layout.getOffsetForHorizontal(line, x);//点中的字符坐标
                                String str = holder.englishldET.getText().toString().substring(characterOffset - 1, characterOffset);//点中的字符
                                String strBefore = reverseStr(holder.englishldET.getText().toString().substring(0, characterOffset));//str之前的字符
                                String strAfter = holder.englishldET.getText().toString().substring(characterOffset, holder.englishldET.getText().length());//str之后的字符
                                StringBuffer sbBefore = new StringBuffer();
                                StringBuffer sbAfter = new StringBuffer();
                                char item = str.charAt(0);
                                if (item < 32 || item > 126) {
                                    return false;
                                } else {
                                    for (int i = 0; i < strBefore.length(); i++) {
                                        char item2 = strBefore.charAt(i);
                                        if (item2 == 46 || item2 == 33 || item2 == 63 || item2 == '…') {
                                            if ((i - 1) > -1) {
                                                char item3 = strBefore.charAt(i - 1);
                                                if (item3 == 32) {
                                                    break;
                                                } else {
                                                    sbBefore.append(item2);
                                                }
                                            }
                                        } else {
                                            sbBefore.append(item2);
                                        }
                                    }
                                    for (int j = 0; j < strAfter.length(); j++) {
                                        char item2 = strAfter.charAt(j);
                                        sbAfter.append(item2);
                                        if (item2 == 46 || item2 == 33 || item2 == 63 || item2 == '…') {
                                            if (j + 1 < strAfter.length()) {
                                                char item3 = strAfter.charAt(j + 1);
                                                if (item3 == 32) {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                                StringBuffer strAll = new StringBuffer();
                                strAll.append(reverseStr(sbBefore.toString()));
                                strAll.append(sbAfter.toString());
                                if (discolorationText != null) {
                                    if (!discolorationText.equals(strAll.toString())) {
                                        index = 0;
                                    } else {
                                        index = 1;
                                    }
                                    discolorationText = "";
                                }
                                discolorationText(strAll.toString());
                                word = strAll.toString();
                                callBack.callBack(strAll.toString(), index);
                                index = 1;
                                break;
                            }
                            break;
                    }
                    return false;

                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView chinesedET;
        TextView englishldET;
    }

    public void discolorationText(String discolorationText) {
        this.discolorationText = discolorationText;
    }

    public String reverseStr(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }


}

