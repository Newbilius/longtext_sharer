package com.newbilius.longtextsharer

import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

public fun getMaxFontSizeOfMultilineText(text: String, maxSize: Size, maxTextSize: Int): Float {

    fun getHeightOfMultiLineText(text: String, textSize: Int, maxWidth: Int): Int {
        val textPaint = TextPaint();
        textPaint.setTextSize(textSize.toFloat());
        val staticLayout = StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        val lineCount = staticLayout.getLineCount();

        //perfectionist hell...
        val charsTest = "éöóêåíãøùçõúôûâàïðîëäæýÿ÷ñìèòüáþ¸¨ÉÖÓÊÅÍÃØÙÇÕÚÔÛÂÀÏÐÎËÄÆÝß×ÑÌÈÒÜÁÞQWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        val charsTestBounds = Rect();
        textPaint.getTextBounds(charsTest, 0, charsTest.length(), charsTestBounds);
        val lineSpacing = Math.max(0.0f, ((lineCount - 1) * charsTestBounds.height() * 0.25f));//It's magic number, I don't have to explain it!
        return (lineSpacing + (lineCount) * charsTestBounds.height()).toInt();
    }

    var textSize = maxTextSize;
    while (getHeightOfMultiLineText(text, textSize, maxSize.width) > maxSize.height)
        textSize--;

    return textSize.toFloat();
}