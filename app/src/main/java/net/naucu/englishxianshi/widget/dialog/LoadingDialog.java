package net.naucu.englishxianshi.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.myTool.tool.DensityTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.widget.view.loading.LoadingView;

/**
 * 类名: PromptDialog.java
 * 描述: TODO 加载框
 * 作者: youyou_pc
 * 时间: 2015年11月24日  下午5:25:25
 */
public class LoadingDialog {
    private Context context;
    private Dialog dialog;
    private View view;
    public LoadingDialog(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_loadingdialog, null);
        dialog = new Dialog(context, R.style.LoadingDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DensityTool.dip2px(context, 150);
        lp.height = DensityTool.dip2px(context, 150);
        LoadingView loadingView = (LoadingView) view.findViewById(R.id.lv_loading);
        loadingView.setLayoutParams(new LinearLayout.LayoutParams(DensityTool.dip2px(context, 150) - DensityTool.dip2px(context, 60), DensityTool.dip2px(context, 150) - DensityTool.dip2px(context, 60)));
        window.setAttributes(lp);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setContent(String text) {
        ((TextView) view.findViewById(R.id.tv_content)).setText(text);
    }
}
