package net.naucu.englishxianshi.ui.account.updatephone;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;

/**
 * Created by Y on 2016/10/8.
 */
public class UpdatePhonePassword extends BaseActivity {

    public ImageButton imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.activity_update_password);
        imgBtnBack = (ImageButton) findViewById(R.id.bar_back);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePhonePassword.this.finish();
            }
        });
    }
}
