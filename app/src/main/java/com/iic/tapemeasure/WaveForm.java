package com.iic.tapemeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shay on 12/3/14.
 */
public class WaveForm extends View {

    ArrayList<Short> volume = new ArrayList<Short>();
    ArrayList<Short> samples = new ArrayList<Short>();
    static int samplesView = 1000;
    long lastRenderTime = 0;

    public WaveForm(Context context) {
        super(context);
        //setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public WaveForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public WaveForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public void AddSample(Short sample) {
        samples.add(sample);
    }

    public void AddVolume(Short sample) {
        volume.add(sample);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = canvas.getClipBounds();
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, black);

        long now = System.currentTimeMillis();
        long delta = now - lastRenderTime;
        lastRenderTime = now;
        int fps = 1000/(int)delta;

        Paint white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL_AND_STROKE);
        white.setTextSize(40.0f);
        canvas.drawText(String.valueOf(fps), 20.0f, 40.0f, white);

        if (samples.size() == 0 || volume.size() == 0) {
            postInvalidate();
            return;
        }
        List<Short> list = samples.subList(samples.size() - Math.min(samplesView, samples.size()), samples.size() -1);
        List<Short> list2 = volume.subList(volume.size() - Math.min(samplesView, volume.size()), volume.size() -1);

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);

        Paint p2 = new Paint();
        p2.setStyle(Paint.Style.FILL);
        p2.setColor(Color.YELLOW);

        drawSamples(canvas, rect, p, list);
        drawSamples(canvas, rect, p2, list2);

        postInvalidate();
    }

    void drawSamples(Canvas canvas, Rect rect, Paint p, List<Short> list) {
        float width = (float)rect.width() / (float)samplesView;
        for (int i = 0; i < list.size(); i++) {
            float value = (float)list.get(i) / 30000.0f * (float)rect.height() * 0.5f;
            float left = (float)(rect.left) + (float)i * width;
            float top = value > 0.0f ? (float)rect.centerY() - value : (float)rect.centerY();
            float bottom = value > 0.0f ? (float)rect.centerY() : (float)rect.centerY() - value;
            canvas.drawRect(left, top, left+width, bottom, p);
        }

    }
}
