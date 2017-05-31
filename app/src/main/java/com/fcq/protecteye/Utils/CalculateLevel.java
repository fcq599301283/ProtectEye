package com.fcq.protecteye.Utils;

/**
 * Created by FengChaoQun
 * on 2017/5/10
 */

public class CalculateLevel {

    private float value1, value2, value3, value4, value5;

    private int lastLevel;

    private float minValue, maxValue;
    private float lastValue;


    public CalculateLevel(float minValue, float maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getLevel(float value) {
        value = Math.abs(value);
        if (value - lastValue > 3) {
            return lastLevel;
        }
        lastValue = value;

        if (value1 == 0) {
            value1 = value;
            return lastLevel;
        } else if (value2 == 0) {
            value2 = value;
            return lastLevel;
        } else if (value3 == 0) {
            value3 = value;
            return lastLevel;
        } else if (value4 == 0) {
            value4 = value;
            return lastLevel;
        } else if (value5 == 0) {
            value5 = value;
            return lastLevel;
        } else {
            value1 = value2;
            value2 = value3;
            value3 = value4;
            value4 = value5;
            value5 = value;
        }

        float averageValue = (value1 + value2 + value3 + value4 + value5) / 5;

        int level;
        if (averageValue < minValue) {
            level = 0;
        } else if (averageValue > maxValue) {
            level = 100;
        } else {
            level = (int) (100 * ((averageValue - minValue) / (maxValue - minValue)));
        }

        if (level > 100) {
            level = 100;
        }

        lastLevel = level;
        return lastLevel;

    }
}
