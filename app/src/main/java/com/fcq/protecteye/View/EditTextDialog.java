package com.fcq.protecteye.View;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fcq.protecteye.R;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class EditTextDialog {
    private String title;
    private OnClickSure onClickSure;
    private ClearableEditText clearableEditText;
    private View cancel, sure;
    private TextView titleTextView;
    private Context context;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public EditTextDialog(@NonNull Context context, String title, final OnClickSure onClickSure) {
        this.context = context;
        this.title = title;
        this.onClickSure = onClickSure;

        builder = new AlertDialog.Builder(context);

        View contentView = View.inflate(context, R.layout.edittext_dialog, null);
        clearableEditText = (ClearableEditText) contentView.findViewById(R.id.editText);
        cancel = contentView.findViewById(R.id.cancel);
        sure = contentView.findViewById(R.id.sure);
        titleTextView = (TextView) contentView.findViewById(R.id.title);
        titleTextView.setText(title);
        builder.setView(contentView);
        alertDialog = builder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = clearableEditText.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    onClickSure.onClick(text);
                }
                alertDialog.dismiss();
            }
        });
    }


    public void setIntegerModle() {
        clearableEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        clearableEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
    }

    public void setDecimalModle() {
        clearableEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        clearableEditText.setFilters(new InputFilter[]{new DecimalInputFilter(1, 1)});
    }

    public interface OnClickSure {
        void onClick(String text);
    }

    public void show() {
        alertDialog.show();
    }
}
