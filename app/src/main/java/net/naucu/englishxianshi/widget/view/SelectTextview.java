package net.naucu.englishxianshi.widget.view;

import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewDebug.ExportedProperty;
import android.widget.EditText;
import android.widget.Toast;

import net.naucu.englishxianshi.ui.DictionaryTranslationActivity;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity.TranslateType;
import net.naucu.englishxianshi.util.NetTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectTextview extends EditText{

	public SelectTextview(Context context) {
		super(context);
	}
	public SelectTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SelectTextview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	float x=0,y=0;

	@Override
    public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x=event.getX();
			y=event.getY();
			break;
		case MotionEvent.ACTION_UP:
			
			float xx,yy;
			xx=event.getX()-x;
			yy=event.getY()-y;
			if(xx*xx+yy*yy<100){
				String word=getSelectWord(SelectTextview.this.getEditableText(),
						extractWordCurOff(SelectTextview.this.getLayout(),
								(int)event.getX()-30,(int)event.getY())).trim();
				if(!word.equals("")){
					if(!NetTool.isNetworkConnected(getContext())){
						Toast.makeText(getContext(), "网络异常,请检查网络连接状态", Toast.LENGTH_SHORT).show();
					}else{
						DictionaryTranslationActivity.startActivity(getContext(), TranslateType.word, word,1);
					}
				}
			}
			break;

		default:
			break;
		}
        return super.onTouchEvent(event);
    }

    @Override
    public boolean getDefaultEditable() {
        return false;
    }
    
    @Override
    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        return super.isFocused();// return true�?定有焦点
    }
	
	public int extractWordCurOff(Layout layout, int x, int y) {
        int line;
        line = layout.getLineForVertical(getScrollY() + y-15);
        int curOff = layout.getOffsetForHorizontal(line, x);
        return curOff;
    }

    public String getSelectWord(Editable content, int curOff) {
        String word = "";
        int start = getWordLeftIndex(content, curOff);
        int end = getWordRightIndex(content, curOff);
        if (start >= 0 && end >= 0) {
            word = content.subSequence(start, end).toString().trim();
            if (!"".equals(word)) {
                // setFocusable(false);
                setFocusableInTouchMode(true);
                requestFocus();
                Selection.setSelection(content, start, end);// 设置当前具有焦点的文本字段的选择范围,当前文本必须具有焦点，否则此方法无效
            }
        }
        return word;
    }

    public int getWordLeftIndex(Editable content, int cur) {
        // --left
        String editableText = content.toString();
        if (cur >= editableText.length())
            return cur;

        int temp = 0;
        if (cur >= 20)
            temp = cur - 20;
        Pattern pattern = Pattern.compile("[^'A-Za-z]");
        Matcher m = pattern.matcher(editableText.charAt(cur) + "");
        if (m.find())
            return cur;

        String text = editableText.subSequence(temp, cur).toString();
        int i = text.length() - 1;
        for (; i >= 0; i--) {
            Matcher mm = pattern.matcher(text.charAt(i) + "");
            if (mm.find())
                break;
        }
        int start = i + 1;
        start = cur - (text.length() - start);
        return start;
    }

    public int getWordRightIndex(Editable content, int cur) {
        // --right
        String editableText = content.toString();
        if (cur >= editableText.length())
            return cur;

        int templ = editableText.length();
        if (cur <= templ - 20)
            templ = cur + 20;
        Pattern pattern = Pattern.compile("[^'A-Za-z]");
        Matcher m = pattern.matcher(editableText.charAt(cur) + "");
        if (m.find())
            return cur;

        String text1 = editableText.subSequence(cur, templ).toString();
        int i = 0;
        for (; i < text1.length(); i++) {
            Matcher mm = pattern.matcher(text1.charAt(i) + "");
            if (mm.find())
                break;
        }
        int end = i;
        end = cur + end;
        return end;
    }


}
