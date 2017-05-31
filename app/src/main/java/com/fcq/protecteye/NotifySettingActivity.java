package com.fcq.protecteye;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.View.SwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class NotifySettingActivity extends Activity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.openNotify)
    SwitchView openNotify;
    @Bind(R.id.exit)
    SwitchView exit;
    @Bind(R.id.disconnected)
    SwitchView disconnected;
    @Bind(R.id.nearly)
    SwitchView nearly;
    @Bind(R.id.timeEnd)
    SwitchView timeEnd;

    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_setting);
        ButterKnife.bind(this);
        openNotify.setState(true);
        exit.setState(true);
        disconnected.setState(true);
        timeEnd.setState(true);
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

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
