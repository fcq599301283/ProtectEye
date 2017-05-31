package com.fcq.protecteye.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fcq.protecteye.MainActivity;
import com.fcq.protecteye.MyApplication;
import com.fcq.protecteye.R;
import com.fcq.protecteye.Utils.BlurUtils;
import com.fcq.protecteye.Utils.CalculateLevel;
import com.fcq.protecteye.Utils.SPUtils;
import com.fcq.protecteye.Utils.ToastUtils;
import com.fcq.protecteye.data.Model;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class ProtectService extends Service {

    private Model model = new Model();
    private CountDownTimer countDownTimer;
    public static final int secondPerMinute = 10;

    private SensorManager mSensorManager;
    private Sensor directionSensor, moveSensor;
    private float mGravity;

    private CalculateLevel levelX, levelY, levelZ;

    private SensorEventListener directionDetect, moveDetect;

    private final IBinder iBinder = new MyBinder();

    private MyApplication application;

    private Notification.Builder builder;
    private Notification notification;
    public static final int NotificationId = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        directionSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        moveSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGravity = SensorManager.STANDARD_GRAVITY;

        float minValue = 0.2f;
        float maxValue = 0.6f;
        levelX = new CalculateLevel(minValue, maxValue);
        levelY = new CalculateLevel(minValue, maxValue);
        levelZ = new CalculateLevel(minValue, maxValue);

        addObserver(true);

        application = (MyApplication) getApplication();

        String currentModel = (String) SPUtils.get(this, SPUtils.CURRENT_MODEL, "推荐模式");
        if (currentModel == null) {
            currentModel = "推荐模式";
        }
        Realm realm = Realm.getDefaultInstance();
        Model model = realm.where(Model.class).equalTo("name", currentModel).findFirst();
        if (model == null) {
            ToastUtils.showShort(this, "没有找到默认的模式");
        } else {
            updateModel(model);
        }
        realm.close();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NotificationId, intent, 0);

        builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle(getString(R.string.app_name))
                .setContentText("正在保护")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.logo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NotificationId, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    public void refreshNotification(String text) {
        builder.setContentText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startForeground(NotificationId, builder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addObserver(false);
    }

    public void updateModel(Model m) {
        model.setName(m.getName());
        model.setTotalUseTime(m.getTotalUseTime());
        model.setDivideTime(m.getDivideTime());
        model.setWalkBlur(m.isWalkBlur());
        model.setDirectionBlur(m.isDirectionBlur());
        startTimeCount();
        if (!model.isWalkBlur()) {
            BlurUtils.setMoveLevel(0);
        }
        if (!model.isDirectionBlur()) {
            BlurUtils.setDirectionLevel(0);
        }

    }

    private void startTimeCount() {
        if (model == null) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.d("ProtectService", "model.getTotalUseTime():" + model.getTotalUseTime());

        countDownTimer = new CountDownTimer(model.getTotalUseTime() * secondPerMinute * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("ProtectService", "millisUntilFinished:" + millisUntilFinished);
                refreshNotification(millisUntilFinished / 1000 + "秒后休息");
            }

            @Override
            public void onFinish() {
                application.showDialog(model, ProtectService.this);
            }
        };
        countDownTimer.start();
    }

    private void addObserver(boolean observer) {
        if (directionDetect == null) {
            directionDetect = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    if (!model.isDirectionBlur()) {
                        return;
                    }

                    float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
                    float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
                    float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis
                    if (zValue < 0) {
                        int level = (int) (Math.abs(200 * (zValue / mGravity)));
                        Log.d("AccelerometerActivity", "level:" + level);
                        if (level >= 0) {
                            if (level > 100) {
                                level = 100;
                            }
                            float radius = level * 1f / 100;
                            Log.d("AccelerometerActivity", "radius:" + radius);
                            //// TODO: 设置模糊度
                            BlurUtils.setDirectionLevel(level);
                        }
                    } else {
                        //// TODO: 恢复正常
                        BlurUtils.setDirectionLevel(0);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }

        if (moveDetect == null) {
            moveDetect = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    if (!model.isWalkBlur()) {
                        return;
                    }

                    float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
                    float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
                    float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis

                    int level = Math.max(levelX.getLevel(xValue), levelY.getLevel(yValue));
                    // TODO: 设置透明级别
                    BlurUtils.setMoveLevel(level);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }

        if (observer) {
            mSensorManager.registerListener(directionDetect, directionSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(moveDetect, moveSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            mSensorManager.unregisterListener(directionDetect);
            mSensorManager.unregisterListener(moveDetect);
        }
    }

    public class MyBinder extends Binder {

        public ProtectService getMyService() {
            return ProtectService.this;
        }

    }
}
