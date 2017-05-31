package com.fcq.protecteye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.data.Model;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class ProtectModelActivity extends Activity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.delete)
    ImageView delete;
    @Bind(R.id.addModel)
    RelativeLayout addModel;
    @Bind(R.id.name)
    RelativeLayout recommendModel;
    @Bind(R.id.extremityModel)
    RelativeLayout extremityModel;
    @Bind(R.id.listView)
    ListView listView;

    private Realm realm;
    private RealmResults<Model> models;
    private ModelAdapter adapter;
    private OnBlurChangedImp onBlurChangedImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_model);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        realm = Realm.getDefaultInstance();
        models = realm.where(Model.class).equalTo("isDefault", false).findAll();
        adapter = new ModelAdapter(this, models);
        listView.setAdapter(adapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @OnClick({R.id.back, R.id.delete, R.id.addModel, R.id.name, R.id.extremityModel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                break;
            case R.id.addModel:
                startActivity(new Intent(ProtectModelActivity.this, AddCustomModel.class));
                break;
            case R.id.name:
                ModelDisplayActivity.start(ProtectModelActivity.this, false, "推荐模式");
                break;
            case R.id.extremityModel:
                ModelDisplayActivity.start(ProtectModelActivity.this, false, "极限护眼");
                break;
        }
    }
}
