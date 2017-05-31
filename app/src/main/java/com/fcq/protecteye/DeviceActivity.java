package com.fcq.protecteye;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.Utils.ToastUtils;
import com.fcq.protecteye.View.SwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class DeviceActivity extends Activity {
    @Bind(R.id.detect)
    SwitchView detect;
    @Bind(R.id.device)
    TextView device;

    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        detect.setState(true);
        device.setText(Build.MODEL);

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

    @OnClick({R.id.back, R.id.changeDeice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.changeDeice:
                ToastUtils.showShort(this, "敬请期待");
                break;
        }
    }
}
