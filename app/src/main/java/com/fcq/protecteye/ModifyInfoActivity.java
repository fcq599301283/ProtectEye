package com.fcq.protecteye;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.View.EditTextDialog;
import com.fcq.protecteye.View.RadioButtonDialog;
import com.fcq.protecteye.data.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class ModifyInfoActivity extends Activity {
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.nameText)
    TextView nameText;
    @Bind(R.id.nameLay)
    RelativeLayout nameLay;
    @Bind(R.id.ageText)
    TextView ageText;
    @Bind(R.id.ageLay)
    RelativeLayout ageLay;
    @Bind(R.id.glassesText)
    TextView glassesText;
    @Bind(R.id.glassesLay)
    RelativeLayout glassesLay;
    @Bind(R.id.leftEyeText)
    TextView leftEyeText;
    @Bind(R.id.leftEyeLay)
    RelativeLayout leftEyeLay;
    @Bind(R.id.rightEyeText)
    TextView rightEyeText;
    @Bind(R.id.rightEyeLay)
    RelativeLayout rightEyeLay;
    @Bind(R.id.astigmatismText)
    TextView astigmatismText;
    @Bind(R.id.astigmatismLay)
    RelativeLayout astigmatismLay;

    private Realm realm;
    private UserInfo userInfo;
    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        ButterKnife.bind(this);
        initView();
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

    private void initView() {
        realm = Realm.getDefaultInstance();
        onBlurChangedImp = new OnBlurChangedImp(getWindow().getDecorView());
        userInfo = realm.where(UserInfo.class).findFirst();
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getName())) {
                nameText.setText(userInfo.getName());
            }
            ageText.setText(String.valueOf(userInfo.getAge() + "岁"));
            leftEyeText.setText(String.valueOf(userInfo.getLeftEye()));
            rightEyeText.setText(String.valueOf(userInfo.getRightEye()));
            if (userInfo.isWearGlasses()) {
                glassesText.setText("是");
            } else {
                glassesText.setText("否");
            }
            if (userInfo.isAstigmatism()) {
                astigmatismText.setText("是");
            } else {
                astigmatismText.setText("否");
            }
        }
    }

    private void ModifyName() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "名字", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                nameText.setText(text);
            }
        });
        editTextDialog.show();
    }

    private void ModifyAge() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "年龄", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                ageText.setText(text + "岁");
            }
        });
        editTextDialog.setIntegerModle();
        editTextDialog.show();
    }

    private void ModifyGlasses() {
        String text = glassesText.getText().toString();
        boolean is;
        is = !TextUtils.isEmpty(text) && "是".equals(text);
        RadioButtonDialog radioButtonDialog = new RadioButtonDialog(this, "是否佩戴眼镜", is, new RadioButtonDialog.OnClickSure() {
            @Override
            public void onClick(boolean is) {
                glassesText.setText(is ? "是" : "否");
            }
        });
        radioButtonDialog.show();
    }

    private void ModifyLeftEye() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "裸眼视力(左眼)", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                leftEyeText.setText(text);
            }
        });
        editTextDialog.setDecimalModle();
        editTextDialog.show();
    }

    private void ModifyRightEye() {
        EditTextDialog editTextDialog = new EditTextDialog(this, "裸眼视力(右眼)", new EditTextDialog.OnClickSure() {
            @Override
            public void onClick(String text) {
                rightEyeText.setText(text);
            }
        });
        editTextDialog.setDecimalModle();
        editTextDialog.show();
    }

    private void ModifyAstigmatism() {
        String text = astigmatismText.getText().toString();
        boolean is;
        is = !TextUtils.isEmpty(text) && "是".equals(text);
        RadioButtonDialog radioButtonDialog = new RadioButtonDialog(this, "是否散光", is, new RadioButtonDialog.OnClickSure() {
            @Override
            public void onClick(boolean is) {
                astigmatismText.setText(is ? "是" : "否");
            }
        });
        radioButtonDialog.show();
    }

    private void saveInfo() {
        final UserInfo userInfo = new UserInfo();
        if (!TextUtils.isEmpty(nameText.getText())) {
            userInfo.setName(nameText.getText().toString());
        }
        if (!TextUtils.isEmpty(ageText.getText())) {
            String age = ageText.getText().toString().substring(0, ageText.getText().length() - 1);
            userInfo.setAge(Integer.parseInt(age));
        }
        if (!TextUtils.isEmpty(glassesText.getText())) {
            userInfo.setWearGlasses("是".equals(glassesText.getText().toString()));
        }
        if (!TextUtils.isEmpty(leftEyeText.getText())) {
            userInfo.setLeftEye(Float.parseFloat(leftEyeText.getText().toString()));
        }
        if (!TextUtils.isEmpty(rightEyeText.getText())) {
            userInfo.setRightEye(Float.parseFloat(rightEyeText.getText().toString()));
        }
        if (!TextUtils.isEmpty(astigmatismText.getText())) {
            userInfo.setAstigmatism("是".equals(astigmatismText.getText().toString()));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(UserInfo.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(userInfo);
            }
        });
        realm.close();

        finish();
    }

    @OnClick({R.id.cancel, R.id.nameLay, R.id.ageLay, R.id.glassesLay, R.id.leftEyeLay, R.id.rightEyeLay, R.id.astigmatismLay, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.nameLay:
                ModifyName();
                break;
            case R.id.ageLay:
                ModifyAge();
                break;
            case R.id.glassesLay:
                ModifyGlasses();
                break;
            case R.id.leftEyeLay:
                ModifyLeftEye();
                break;
            case R.id.rightEyeLay:
                ModifyRightEye();
                break;
            case R.id.astigmatismLay:
                ModifyAstigmatism();
                break;
            case R.id.sure:
                saveInfo();
                break;
        }
    }
}
