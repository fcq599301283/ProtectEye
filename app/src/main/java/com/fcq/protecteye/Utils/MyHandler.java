package com.fcq.protecteye.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class MyHandler extends Handler {

    public static final int receiveMsg = 10;

    private List<OnStateChange> OnStateChanges = new ArrayList<>();
    private int currentState;

    public static MyHandler getInstance() {
        return MyHandler.SingletonHolder.myHandler;
    }

    private static class SingletonHolder {
        private static final MyHandler myHandler = new MyHandler();
    }

    private MyHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Log.d("MyHandler", "state change:" + currentState + "--->" + msg.what);

        for (int i = 0; i < OnStateChanges.size(); i++) {
            OnStateChanges.get(i).onChange(msg);
        }
        currentState = msg.what;
        switch (msg.what) {
            case receiveMsg:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String text = new String(readBuf, 0, msg.arg1);
                BlurUtils.setDistanceLevel(Integer.valueOf(text) * 25);
                Log.d("MyHandler", text);
                break;
            default:
                BlurUtils.setDistanceLevel(0);
                break;
        }
    }

    public void register(OnStateChange OnStateChange, boolean register) {
        if (OnStateChange == null) {
            return;
        }
        if (register) {
            OnStateChanges.add(OnStateChange);
        } else {
            OnStateChanges.remove(OnStateChange);
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public interface OnStateChange {
        void onChange(Message msg);
    }
}
