package com.newbilius.longtextsharer

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

fun writeTextToImage(text: String,
                     maxSize: Size,
                     maxTextSize: Int,
                     padding: Int = 0): Bitmap {
    val bitmap = Bitmap.createBitmap(maxSize.width, maxSize.height, Bitmap.Config.ARGB_8888);
    val canvas = Canvas(bitmap);
    val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG);
    val paint = Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);

    canvas.drawRect(Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), paint);

    textPaint.setColor(Color.BLACK);
    textPaint.setTextSize(getMaxFontSizeOfMultilineText(text, maxSize, maxTextSize));
    textPaint.setAntiAlias(true);

    val sl = StaticLayout(text, textPaint, maxSize.width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    sl.draw(canvas);

    val bitmapWithPaddings = Bitmap.createBitmap(sl.getWidth() + padding * 2, sl.getHeight() + padding * 2, Bitmap.Config.ARGB_8888);
    val canvasWithPaddings = Canvas(bitmapWithPaddings);
    canvasWithPaddings.drawRect(Rect(0, 0, bitmapWithPaddings.getWidth(), bitmapWithPaddings.getHeight()), paint);
    canvasWithPaddings.drawBitmap(bitmap, padding.toFloat(), padding.toFloat(), Paint());

    return bitmapWithPaddings;
}