package com.example.sss.toeflsampler.Controls;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by Bulpet on 2.06.2017.
 */

public class SquareImageButton extends ImageButton {

    public SquareImageButton(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
