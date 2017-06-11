package com.fcq.protecteye.View;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;

import com.fcq.protecteye.R;
import com.fcq.protecteye.data.Model;
import com.fcq.protecteye.service.ProtectService;

import pl.droidsonroids.gif.GifImageView;

import static com.fcq.protecteye.service.ProtectService.secondPerMinute;

/**
 * Created by FengChaoQun
 * on 2017/6/9
 */

public class RestDialog extends Dialog {
    GifImageView gifView;

    private boolean clicked = false;
    private int clickCount;

    public RestDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_rest);
        setCancelable(false);
        gifView = (GifImageView) findViewById(R.id.gifView);

        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    clicked = true;
                    gifView.setImageResource(R.drawable.rest2);
                }

            }
        });
    }

    public void setCountTime(final Model model, final ProtectService protectService) {
//        new CountDownTimer(model.getDivideTime() * secondPerMinute * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                protectService.refreshNotification("剩余休息时间:" + millisUntilFinished / 1000 + "秒");
//            }
//
//            @Override
//            public void onFinish() {
//                gifView.setImageResource(R.drawable.rest3);
//                gifView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dismiss();
//                        protectService.updateModel(model);
//                    }
//                });
//            }
//        }.start();

        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (clickCount) {
                    case 0:
                        gifView.setImageResource(R.drawable.rest2);
                        clickCount++;
                        break;
                    case 1:
                        gifView.setImageResource(R.drawable.rest3);
                        clickCount++;
                        break;
                    case 2:
                        dismiss();
                        protectService.updateModel(model);
                        break;
                }
            }
        });

    }

    public void endShow(final ProtectService protectService) {
        gifView.setImageResource(R.drawable.rest3);
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
