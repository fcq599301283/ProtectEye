package com.fcq.protecteye;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.Utils.ToastUtils;
import com.fcq.protecteye.View.EditTextDialog;
import com.fcq.protecteye.View.SwitchView;
import com.fcq.protecteye.data.Model;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class AddCustomModel extends Activity {
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.save)
    TextView save;
    @Bind(R.id.modelName)
    TextView modelName;
    @Bind(R.id.modelNameLay)
    RelativeLayout modelNameLay;
    @Bind(R.id.totalTime)
    TextView totalTime;
    @Bind(R.id.totalTimeLay)
    RelativeLayout totalTimeLay;
    @Bind(R.id.divideTime)
    TextView divideTime;
    @Bind(R.id.divideTimeLay)
    RelativeLayout divideTimeLay;
    @Bind(R.id.walkBlur)
    SwitchView walkBlur;
    @Bind(R.id.walkBlurLay)
    RelativeLayout walkBlurLay;
    @Bind(R.id.directionBlur)
    SwitchView directionBlur;
    @Bind(R.id.directionLay)
    RelativeLayout directionLay;

    private Realm realm;
    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_model);
        ButterKnife.bind(this);
        walkBlur.setState(true);
        directionBlur.setState(true);
        realm = Realm.getDefaultInstance();

        onBlurChangedImp = new OnBlurChangedImp(getWindow().getDecorView());

        walkBlur.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                walkBlur.setState(true);
            }

            @Override
            public void toggleToOff() {
                walkBlur.setState(false);
            }
        });

        directionBlur.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                directionBlur.setState(true);
            }

            @Override
            public void toggleToOff() {
                directionBlur.setState(false);
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void modifyName() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "标签", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                modelName.setText(text);
            }
        });
        editTextDialog.show();
    }

    private void modifyTotalTime() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "用眼总时长", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                totalTime.setText(text + "min");
            }
        });
        editTextDialog.setIntegerModle();
        editTextDialog.show();
    }

    private void modifyDividerTime() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "用眼休息间隔时间", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                divideTime.setText(text + "min");
            }
        });
        editTextDialog.setIntegerModle();
        editTextDialog.show();
    }

    private void saveModel() {
        final Model model = new Model();
        String name = modelName.getText().toString();
        if (realm.where(Model.class).equalTo("name", name).findFirst() != null) {
            ToastUtils.showShort(this, "标签已存在，请更换标签名");
            return;
        }
        model.setName(modelName.getText().toString());

        int totalTimeCount = Integer.parseInt(totalTime.getText().toString().substring(0, totalTime.length() - 3));
        if (totalTimeCount == 0) {
            ToastUtils.showShort(this, "用眼总时长不能为0");
            return;
        }
        model.setTotalUseTime(totalTimeCount);

        int divideTimeCount = Integer.parseInt(divideTime.getText().toString().substring(0, divideTime.length() - 3));
        if (divideTimeCount == 0) {
            ToastUtils.showShort(this, "用眼休息间隔时间不能为0");
            return;
        }
        model.setDivideTime(divideTimeCount);

        model.setWalkBlur(walkBlur.getState2());
        model.setDirectionBlur(directionBlur.getState2());
        model.setDefault(false);

        Log.d("AddCustomModel", "model:" + model);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(model);
            }
        });

        finish();
    }

    @OnClick({R.id.cancel, R.id.save, R.id.modelNameLay, R.id.totalTimeLay, R.id.divideTimeLay, R.id.walkBlurLay, R.id.directionLay, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                saveModel();
                break;
            case R.id.modelNameLay:
                modifyName();
                break;
            case R.id.totalTimeLay:
                modifyTotalTime();
                break;
            case R.id.divideTimeLay:
                modifyDividerTime();
                break;
            case R.id.walkBlurLay:
                break;
            case R.id.directionLay:
                break;
            case R.id.sure:
                saveModel();
                break;
        }
    }
}
