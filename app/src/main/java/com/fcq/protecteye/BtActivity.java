package com.fcq.protecteye;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fcq.protecteye.Utils.CheckPermission;
import com.fcq.protecteye.Utils.LinkService;
import com.fcq.protecteye.Utils.MyHandler;
import com.fcq.protecteye.Utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/6/8
 */

public class BtActivity extends Activity implements MyHandler.OnStateChange {

    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 200;
    @Bind(R.id.search)
    Button search;
    @Bind(R.id.listView)
    ListView listView;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        deviceAdapter = new DeviceAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        listView.setAdapter(deviceAdapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        if (detectBt()) {
            trySearchLocalBtDevice();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySearchLocalBtDevice();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinkService.getInstance().connect(bluetoothDevices.get(position));
            }
        });

        MyHandler.getInstance().register(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        MyHandler.getInstance().register(this, false);
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    private boolean detectBt() {
        if (bluetoothAdapter == null) {
            ToastUtils.showShort(this, "没有找到蓝牙");
            finish();
            return false;
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        } else {
            return true;
        }
    }

    private void trySearchLocalBtDevice() {

        if (AndPermission.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                search();
            } else {
                showLocationRejectedDialog();
            }
        } else {
            requestLocationPermission();
        }
    }

    public void showLocationRejectedDialog() {
        AndPermission.defaultSettingDialog(this, REQUEST_LOCATION_PERMISSION)
                .setTitle("申请权限失败")
                .setMessage("需要位置权限才能搜索附近的蓝牙,请在设置页面的权限管理中授权，否则该功能无法使用.")
                .setPositiveButton("确定")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void requestLocationPermission() {
        AndPermission.with(getActivity())
                .requestCode(REQUEST_LOCATION_PERMISSION)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(getActivity(), rationale).show();
                    }
                })
                .send();
    }

    private void search() {
        if (!bluetoothAdapter.isEnabled()) {
            ToastUtils.showShort(this, "请开启蓝牙");
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            ToastUtils.showShort(this, "正在刷新");
            return;
        }
        bluetoothAdapter.startDiscovery();
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onChange(Message msg) {
        switch (msg.what) {
            case LinkService.Connected:
                ToastUtils.showShort(getActivity(), "连接成功");
                finish();
                break;
            case LinkService.Connecting:
                ToastUtils.showShort(getActivity(), "正在连接...");
                break;
            case LinkService.ConnectError:
                ToastUtils.showShort(getActivity(), "连接失败");
                break;
        }
    }

    class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BluetoothDevice> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            BluetoothDevice device = getItem(position);
            convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(device.getName() == null ? "未命名" : device.getName());
            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                trySearchLocalBtDevice();
            } else {
                finish();
            }
        }
    }
}
