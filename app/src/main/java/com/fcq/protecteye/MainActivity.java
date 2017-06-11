package com.fcq.protecteye;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.LinkService;
import com.fcq.protecteye.Utils.MyHandler;
import com.fcq.protecteye.Utils.OnBlurChangedImp;
import com.fcq.protecteye.Utils.SPUtils;
import com.fcq.protecteye.View.RestDialog;
import com.fcq.protecteye.data.Model;
import com.fcq.protecteye.service.ProtectService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MyHandler.OnStateChange {


    @Bind(R.id.personalCenter)
    ImageView personalCenter;
    @Bind(R.id.btConnectedImage)
    ImageView btConnectedImage;
    @Bind(R.id.btConnectedStatus)
    TextView btConnectedStatus;
    @Bind(R.id.btConnectedLay)
    LinearLayout btConnectedLay;
    @Bind(R.id.discoverImage)
    ImageView discoverImage;
    @Bind(R.id.discoverText)
    TextView discoverText;
    @Bind(R.id.discoverLay)
    LinearLayout discoverLay;
    @Bind(R.id.modelText)
    TextView modelText;
    @Bind(R.id.modelSelectLay)
    FrameLayout modelSelectLay;
    private Realm realm;
    private RealmResults<Model> models;
    private ModelSelectAdapter adapter;
    private PopupWindow popupWindow;

    private OnBlurChangedImp onBlurChangedImp;
    private ProtectService protectService;
    private boolean isBinded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        stopService(new Intent(this, ProtectService.class));
        if (isBinded) {
            unbindService(serviceConnection);
            isBinded = false;
        }
        MyHandler.getInstance().register(this, false);
    }

    private void initView() {
        realm = Realm.getDefaultInstance();
        models = realm.where(Model.class).findAll();
        adapter = new ModelSelectAdapter(this, models);
        adapter.setOnSelected(new ModelSelectAdapter.OnSelected() {
            @Override
            public void onSelected(String modelName) {
                modelText.setText(modelName);
                popupWindow.dismiss();

                SPUtils.put(MainActivity.this, SPUtils.CURRENT_MODEL, modelName);

                Model model = realm.where(Model.class).equalTo("name", modelName).findFirst();
                if (model != null) {
                    protectService.updateModel(model);
                }
            }
        });

        View menuView = View.inflate(this, R.layout.popmenu_model_select, null);
        popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) menuView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        menuView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                modelSelectLay.setBackgroundResource(R.drawable.retan_start_model2);
            }
        });

        onBlurChangedImp = new OnBlurChangedImp(getWindow().getDecorView());


        Intent intent = new Intent(this, ProtectService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        MyHandler.getInstance().register(this, true);
    }

    private void initDefaultModel() {
        String modelName = (String) SPUtils.get(this, SPUtils.CURRENT_MODEL, SPUtils.DEFAULT_STRING);
        if (modelName == null || SPUtils.DEFAULT_STRING.equals(modelName)) {
            return;
        }
        modelText.setText(modelName);
        Model model = realm.where(Model.class).equalTo("name", modelName).findFirst();
        if (model != null) {
            protectService.updateModel(model);
        }
    }

    private void showPopMenu() {
        popupWindow.showAsDropDown(modelSelectLay, 0, 0);
        modelSelectLay.setBackgroundResource(R.drawable.retan_start_model);

    }

    @OnClick({R.id.personalCenter, R.id.btConnectedLay, R.id.discoverLay, R.id.modelSelectLay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personalCenter:
                startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
                break;
            case R.id.btConnectedLay:
                startActivity(new Intent(MainActivity.this, BtActivity.class));
                break;
            case R.id.discoverLay:
                startActivity(new Intent(MainActivity.this, DiscoverActivity.class));
                break;
            case R.id.modelSelectLay:
                showPopMenu();
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ProtectService.MyBinder myBinder = (ProtectService.MyBinder) service;
            protectService = myBinder.getMyService();
            isBinded = true;
            initDefaultModel();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    @Override
    public void onChange(Message msg) {
        switch (msg.what) {
            case LinkService.Connected:
            case MyHandler.receiveMsg:
                btConnectedImage.setImageResource(R.mipmap.bt_connected);
                btConnectedStatus.setText("已连接");
                break;
            default:
                btConnectedImage.setImageResource(R.mipmap.unconnected);
                btConnectedStatus.setText("未连接");
                break;
        }
    }

}
