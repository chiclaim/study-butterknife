package com.chiclaim.butterknife;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.chiclaim.butterknife.annotation.BindView;

/**
 * Created by chiclaim on 2016/09/23
 */

public class BActivity extends Activity {


    @BindView(R.id.btn_b)
    Button button;
    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_layout);
    }
}
