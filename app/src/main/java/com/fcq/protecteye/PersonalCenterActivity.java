package com.fcq.protecteye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.data.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class PersonalCenterActivity extends Activity {
    @Bind(R.id.leftTop)
    ImageView leftTop;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.modify)
    ImageView modify;
    @Bind(R.id.age)
    TextView age;
    @Bind(R.id.leftEye)
    TextView leftEye;
    @Bind(R.id.rightEye)
    TextView rightEye;
    @Bind(R.id.personalInfoLay)
    CardView personalInfoLay;
    @Bind(R.id.modelLay)
    RelativeLayout modelLay;
    @Bind(R.id.data)
    RelativeLayout data;
    @Bind(R.id.notify)
    RelativeLayout notify;
    @Bind(R.id.device)
    RelativeLayout device;
    @Bind(R.id.glasses)
    ImageView glasses;
    @Bind(R.id.restTimeLay)
    RelativeLayout restTimeLay;

    private Realm realm;
    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        UserInfo userInfo = realm.where(UserInfo.class).findFirst();
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getName())) {
                name.setText(userInfo.getName());
            }
            age.setText(String.valueOf(userInfo.getAge() + "岁"));
            leftEye.setText("左眼 " + String.valueOf(userInfo.getLeftEye()));
            rightEye.setText("右眼 " + String.valueOf(userInfo.getRightEye()));
            if (userInfo.isWearGlasses()) {
                glasses.setVisibility(View.VISIBLE);
            } else {
                glasses.setVisibility(View.INVISIBLE);
            }
        }

        onBlurChangedImp = new OnBlurChangedImp(getWindow().getDecorView());
        BlurUtils.registerObserver(onBlurChangedImp, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BlurUtils.registerObserver(onBlurChangedImp, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @OnClick({R.id.leftTop, R.id.modify, R.id.modelLay, R.id.data, R.id.notify, R.id.device, R.id.restTimeLay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftTop:
                break;
            case R.id.modify:
                startActivity(new Intent(PersonalCenterActivity.this, ModifyInfoActivity.class));
                break;
            case R.id.modelLay:
                startActivity(new Intent(PersonalCenterActivity.this, ProtectModelActivity.class));
                break;
            case R.id.data:
                break;
            case R.id.notify:
                startActivity(new Intent(PersonalCenterActivity.this, NotifySettingActivity.class));
                break;
            case R.id.device:
                startActivity(new Intent(PersonalCenterActivity.this, DeviceActivity.class));
                break;
            case R.id.restTimeLay:
                break;
        }
    }
}
