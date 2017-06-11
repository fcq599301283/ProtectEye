package com.fcq.protecteye;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/5/24
 */

public class DiscoverActivity extends Activity {
    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);
        onBlurChangedImp = new OnBlurChangedImp(getWindow().getDecorView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        BlurUtils.registerObserver(onBlurChangedImp, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BlurUtils.registerObserver(onBlurChangedImp, false);
    }

    @OnClick(R.id.leftTop)
    public void onClick() {
        finish();
    }
}
