package com.fcq.protecteye.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.fcq.protecteye.R;


/**
 * 带有图标和删除符号的可编辑输入框，用户可以自定义传入的显示图标
 *
 * @author
 */
public class ClearableEditText extends android.support.v7.widget.AppCompatEditText implements OnTouchListener, TextWatcher {

    // 删除符号
    Drawable deleteImage = getResources().getDrawable(R.mipmap.delete);


    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        ClearableEditText.this.setOnTouchListener(this);
        ClearableEditText.this.addTextChangedListener(this);
        deleteImage.setBounds(0, 0, deleteImage.getIntrinsicWidth(), deleteImage.getIntrinsicHeight());
        manageClearButton();
    }

    /**
     * 传入删除图标资源id
     *
     * @param id
     */
    public void setDeleteImage(int id) {
        deleteImage = getResources().getDrawable(id);
        deleteImage.setBounds(0, 0, deleteImage.getIntrinsicWidth(), deleteImage.getIntrinsicHeight());
        manageClearButton();
    }

    void manageClearButton() {
        if (this.getText().toString().equals(""))
            removeClearButton();
        else
            addClearButton();
    }

    void removeClearButton() {
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
    }

    void addClearButton() {
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], deleteImage,
                this.getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ClearableEditText et = ClearableEditText.this;

        if (et.getCompoundDrawables()[2] == null)
            return false;
        if (event.getAction() != MotionEvent.ACTION_UP)
            return false;
        if (event.getX() > et.getWidth() - et.getPaddingRight() - deleteImage.getIntrinsicWidth()) {
            et.setText("");
            ClearableEditText.this.removeClearButton();
        }
        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ClearableEditText.this.manageClearButton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
