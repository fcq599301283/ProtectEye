package com.fcq.protecteye.Utils;

import android.view.View;

import com.fcq.protecteye.R;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class OnBlurChangedImp implements BlurUtils.OnBlurChanged {
    private View targetView;

    private int moveLevel, directionLever;

    public OnBlurChangedImp(View view) {
        this.targetView = view.findViewById(R.id.realTimeBlurView);
    }

    @Override
    public void onMoveChanged(int level) {
        moveLevel = level;
        calculateLevel();
    }

    @Override
    public void onDirectionChange(int level) {
        directionLever = level;
        calculateLevel();
    }

    private void calculateLevel() {
        int level = Math.max(moveLevel, directionLever);
        float radius = level * 1f / 100;
        targetView.setAlpha(radius);
    }
}
