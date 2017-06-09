package com.fcq.protecteye.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * Created by FengChaoQun
 * on 2017/6/8
 */

public class LinkService {

    public static final int NoState = 0;
    public static final int Connecting = 1;
    public static final int Connected = 2;
    public static final int ConnectError = 3;
    private int currentState;

    private final Handler mHandler;
    private final BluetoothAdapter mAdapter;

    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private LinkService(Handler mHandler) {
        this.mHandler = mHandler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static LinkService getInstance() {
        return SingletonHolder.linkService;
    }

    private static class SingletonHolder {
        private static final LinkService linkService = new LinkService(MyHandler.getInstance());
    }

    public void connect(BluetoothDevice device) {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public void connected(BluetoothSocket mmSocket) {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }
}
