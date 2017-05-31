package com.fcq.protecteye.View;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fcq.protecteye.R;

/**
 * Created by FengChaoQun
 * on 2017/5/21
 */

public class RadioButtonDialog {
    private String title;
    private OnClickSure onClickSure;
    private RadioButton yes, no;
    private View cancel, sure;
    private TextView titleTextView;
    private Context context;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public RadioButtonDialog(@NonNull Context context, String title, boolean is, final OnClickSure onClickSure) {
        this.context = context;
        this.title = title;
        this.onClickSure = onClickSure;

        builder = new AlertDialog.Builder(context);

        View contentView = View.inflate(context, R.layout.radio_button_dialog, null);
        yes = (RadioButton) contentView.findViewById(R.id.yes);
        no = (RadioButton) contentView.findViewById(R.id.no);
        yes.setChecked(is);
        no.setChecked(!is);
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
                onClickSure.onClick(yes.isChecked());
                alertDialog.dismiss();
            }
        });
    }


    public interface OnClickSure {
        void onClick(boolean is);
    }

    public void show() {
        alertDialog.show();
    }
}
