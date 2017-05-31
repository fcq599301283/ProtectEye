package com.fcq.protecteye.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by FengChaoQun
 * on 2016/4/20
 * description:圆形的imageview
 */
public class CirecleImage extends android.support.v7.widget.AppCompatImageView {
    public static final int PERSON_INFO = 1000;
    public static final int PERSON_STATE = 2000;
    /**
     * 获取屏幕密度
     */
    private final float density = getContext().getResources().getDisplayMetrics().density;
    private Context context;
    private int intent_type;
    private String account;
    /**
     * �?
     */
    private float roundness;


    public CirecleImage(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CirecleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CirecleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    @Override
    public void draw(Canvas canvas) {
        final Bitmap composedBitmap;
        final Bitmap originalBitmap;
        final Canvas composedCanvas;
        final Canvas originalCanvas;
        final Paint paint;
        final int height;
        final int width;

        width = getWidth();

        height = getHeight();

        composedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        originalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        composedCanvas = new Canvas(composedBitmap);
        originalCanvas = new Canvas(originalBitmap);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        super.draw(originalCanvas);

        composedCanvas.drawARGB(0, 0, 0, 0);

        composedCanvas.drawRoundRect(new RectF(0, 0, width, height), this.roundness, this.roundness, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);

        canvas.drawBitmap(composedBitmap, 0, 0, new Paint());

    }

    public float getRoundness() {
        return this.roundness / this.density;
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness * this.density;
    }

    private void init() {
        // 括号中的数字是调整图片弧度的 调成100为圆形图�? 调成15为圆角图�?
        setRoundness(100);
    }

}
