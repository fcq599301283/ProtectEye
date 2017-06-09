package com.fcq.protecteye.Utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.ContextCompat;

/**
 * Created by FengChaoQun
 * on 2017/2/21
 */

public class CheckPermission {
    public static final String MIUI = "Xiaomi";

    public static boolean isGranted(Context context, String permission) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        boolean isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        if (Build.MANUFACTURER.equals(MIUI)) {
            return isGranted && checkOpsPermission(context, permission);
        }
        return isGranted;
    }

    @TargetApi(23)
    private static boolean checkOpsPermission(Context context, String permission) {
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String opsName = AppOpsManager.permissionToOp(permission);
            if (opsName == null) {
                return true;
            }
            int opsMode = appOpsManager.checkOpNoThrow(opsName, Process.myUid(), context.getPackageName());
            return opsMode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            return true;
        }

    }
}
