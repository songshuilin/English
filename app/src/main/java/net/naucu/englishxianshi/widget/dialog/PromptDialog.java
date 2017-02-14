package net.naucu.englishxianshi.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.naucu.englishxianshi.R;

/**
 * 类名: PromptDialog.java
 * 描述: TODO 自定义对话框
 * 作者: youyou_pc
 * 时间: 2015年11月24日  下午5:25:25
 */
public class PromptDialog implements OnClickListener {
    private Context context;
    private Dialog dialog;
    private View view;
    private Button bu_cancel;
    private Button bt_determine;
    private onPromptClickListener clickListener;
    private OnDismissListener onDismissListener;
    public PromptDialog(Context context) {
        this.context = context;
        initDialog();
        initView();
        settitle(context.getString(R.string.tx_prompt));
    }

    private void initView() {
        bu_cancel = (Button) view.findViewById(R.id.bu_cancel);
        bt_determine = (Button) view.findViewById(R.id.bt_determine);
        bu_cancel.setOnClickListener(this);
        bt_determine.setOnClickListener(this);
    }

    private void initDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_promptdialog, null);
        dialog = new Dialog(context, R.style.PromptDialog);
        dialog.setContentView(view);
        // 设置窗口位置
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.7); // 高度设置为屏幕的0.7
        window.setAttributes(lp);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new Dialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(PromptDialog.this.dialog);
                }
            }

        });
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void settitle(String text) {
        ((TextView) view.findViewById(R.id.tv_title)).setText(text);
    }

    public void setContent(String text) {
        ((TextView) view.findViewById(R.id.tv_content)).setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_cancel:
                if (v != null) {
                    if (clickListener != null) {
                        clickListener.onCancel(v);
                    }
                }
                break;
            case R.id.bt_determine:
                if (v != null) {
                    if (clickListener != null) {
                        clickListener.onDetermine(v);
                    }
                }
                break;
        }
    }

    public interface onPromptClickListener {
        void onCancel(View v);

        void onDetermine(View v);
    }

    public interface OnDismissListener {

        void onDismiss(Dialog dialog);
    }

    public void setClickListener(onPromptClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
