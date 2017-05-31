package com.fcq.protecteye.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class UserInfo extends RealmObject {
    @PrimaryKey
    private String name;
    private int age;
    private boolean isWearGlasses;
    private float leftEye;
    private float rightEye;
    private boolean Astigmatism;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isWearGlasses() {
        return isWearGlasses;
    }

    public void setWearGlasses(boolean wearGlasses) {
        isWearGlasses = wearGlasses;
    }

    public float getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(float leftEye) {
        this.leftEye = leftEye;
    }

    public float getRightEye() {
        return rightEye;
    }

    public void setRightEye(float rightEye) {
        this.rightEye = rightEye;
    }

    public boolean isAstigmatism() {
        return Astigmatism;
    }

    public void setAstigmatism(boolean astigmatism) {
        Astigmatism = astigmatism;
    }


}
