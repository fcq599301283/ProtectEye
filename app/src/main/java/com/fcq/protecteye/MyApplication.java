package com.fcq.protecteye;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fcq.protecteye.data.Model;
import com.fcq.protecteye.service.ProtectService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.fcq.protecteye.service.ProtectService.secondPerMinute;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class MyApplication extends Application {
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        //      初始化数据库
        RealmConfiguration config = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        initDefaultModel();

        /*
        **describe:集中监视Activity生命周期
        */
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "resumed");
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    private void initDefaultModel() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Model.class).equalTo("name", "推荐模式").findFirst() == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Model model = new Model();
                    model.setName("推荐模式");
                    model.setTotalUseTime(60);
                    model.setDivideTime(20);
                    model.setWalkBlur(true);
                    model.setDirectionBlur(true);
                    model.setDefault(true);
                    realm.copyToRealmOrUpdate(model);
                }
            });
        }

        if (realm.where(Model.class).equalTo("name", "极限护眼").findFirst() == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Model model = new Model();
                    model.setName("极限护眼");
                    model.setTotalUseTime(20);
                    model.setDivideTime(20);
                    model.setWalkBlur(true);
                    model.setDirectionBlur(true);
                    model.setDefault(true);
                    realm.copyToRealmOrUpdate(model);
                }
            });
        }
    }

    public void showDialog(final Model model, final ProtectService protectService) {
        if (currentActivity == null) {
            return;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            View view = View.inflate(currentActivity, R.layout.dialog_protect, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            TextView contentText = (TextView) view.findViewById(R.id.contentText);
            final TextView leftTime = (TextView) view.findViewById(R.id.leftTime);

            new CountDownTimer(model.getDivideTime() * secondPerMinute * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    leftTime.setText("剩余时间:" + millisUntilFinished / 1000 + "秒");
                    protectService.refreshNotification("剩余休息时间:" + millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    protectService.updateModel(model);
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
