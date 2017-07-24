package com.github.lguipeng.animcheckbox;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

public class MainActivity extends AppCompatActivity implements AnimCheckBox.OnCheckedChangeListener {
    private AnimCheckBox mAnimCheckBox1, mAnimCheckBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_circle);

        final CustomDialog builder = new CustomDialog.Builder(this).create();
        builder.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                builder.dismiss();
            }
        }, 1800);
    }

    @Override
    public void onChange(AnimCheckBox view, boolean checked) {
        switch (view.getId()) {
            case R.id.checkbox_1:
                Log.d("MainActivity", "checkbox_1 --> " + checked);
                break;
            case R.id.checkbox_2:
                Log.d("MainActivity", "checkbox_2 --> " + checked);
                break;
        }

    }
}
