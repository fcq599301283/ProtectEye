package com.fcq.protecteye.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class Model extends RealmObject {
    @PrimaryKey
    private String name;
    private int totalUseTime;
    private int divideTime;
    private boolean walkBlur;
    private boolean directionBlur;
    private boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalUseTime() {
        return totalUseTime;
    }

    public void setTotalUseTime(int totalUseTime) {
        this.totalUseTime = totalUseTime;
    }

    public int getDivideTime() {
        return divideTime;
    }

    public void setDivideTime(int divideTime) {
        this.divideTime = divideTime;
    }

    public boolean isWalkBlur() {
        return walkBlur;
    }

    public void setWalkBlur(boolean walkBlur) {
        this.walkBlur = walkBlur;
    }

    public boolean isDirectionBlur() {
        return directionBlur;
    }

    public void setDirectionBlur(boolean directionBlur) {
        this.directionBlur = directionBlur;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", totalUseTime=" + totalUseTime +
                ", divideTime=" + divideTime +
                ", walkBlur=" + walkBlur +
                ", directionBlur=" + directionBlur +
                ", isDefault=" + isDefault +
                '}';
    }
}
