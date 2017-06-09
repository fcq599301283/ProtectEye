package com.fcq.protecteye.Utils;

import java.util.ArrayList;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class BlurUtils {
    public interface OnBlurChanged {
        void onMoveChanged(int level);

        void onDirectionChange(int level);

        void onDistanceChange(int level);
    }

    private static ArrayList<OnBlurChanged> onBlurChangeds = new ArrayList<>();

    public static void registerObserver(OnBlurChanged onBlurChanged, boolean register) {
        if (onBlurChanged == null) {
            return;
        }
        if (register) {
            onBlurChangeds.add(onBlurChanged);
        } else {
            onBlurChangeds.remove(onBlurChanged);
        }
    }

    public static void setMoveLevel(int level) {
        for (OnBlurChanged onBlurChanged : onBlurChangeds) {
            onBlurChanged.onMoveChanged(level);
        }
    }

    public static void setDirectionLevel(int level) {
        for (OnBlurChanged onBlurChanged : onBlurChangeds) {
            onBlurChanged.onDirectionChange(level);
        }
    }

    public static void setDistanceLevel(int level) {
        for (OnBlurChanged onBlurChanged : onBlurChangeds) {
            onBlurChanged.onDistanceChange(level);
        }
    }
}
