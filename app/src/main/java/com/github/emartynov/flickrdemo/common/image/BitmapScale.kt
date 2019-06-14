package com.github.emartynov.flickrdemo.common.image

import android.graphics.Bitmap
import android.graphics.Matrix

interface BitmapScale {
    fun scaleCenterCrop(bitmap: Bitmap, destWidth: Int, destHeight: Int): Bitmap
}

internal class BitmapScaleImpl : BitmapScale {
    override fun scaleCenterCrop(bitmap: Bitmap, destWidth: Int, destHeight: Int) =
        crop(bitmap, destWidth, destHeight)

    // algorithm was taken from
    // https://android.googlesource.com/platform/packages/apps/UnifiedEmail/+/f4fda67/src/com/android/mail/photomanager/BitmapUtil.java
    private fun crop(
        src: Bitmap,
        width: Int,
        height: Int
    ): Bitmap {
        val srcWidth = src.width
        val srcHeight = src.height
        // exit early if no resize/crop needed
        if (width == srcWidth && height == srcHeight) {
            return src
        }
        val matrix = Matrix()
        val scale = Math.max(
            width.toFloat() / srcWidth,
            height.toFloat() / srcHeight
        )
        matrix.setScale(scale, scale)
        val srcCroppedW: Int
        val srcCroppedH: Int
        var srcX: Int
        var srcY: Int
        srcCroppedW = Math.round(width / scale)
        srcCroppedH = Math.round(height / scale)
        srcX = (srcWidth * 0.5f - srcCroppedW / 2).toInt()
        srcY = (srcHeight * 0.5f - srcCroppedH / 2).toInt()
        // Nudge srcX and srcY to be within the bounds of src
        srcX = Math.max(Math.min(srcX, srcWidth - srcCroppedW), 0)
        srcY = Math.max(Math.min(srcY, srcHeight - srcCroppedH), 0)
        return Bitmap.createBitmap(
            src,
            srcX,
            srcY,
            srcCroppedW,
            srcCroppedH,
            matrix,
            true
        )
    }
}
