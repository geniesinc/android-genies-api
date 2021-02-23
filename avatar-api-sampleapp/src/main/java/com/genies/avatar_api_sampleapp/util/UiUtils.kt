package com.genies.avatar_api_sampleapp.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.Toast

fun getBackgroundGradient(): Drawable {
    val colors = intArrayOf(
        Color.rgb(255, 255, 255),
        Color.rgb(255, 255, 255)
//        Color.rgb(90, 105, 166),
//        Color.rgb(90, 105, 166)
//        Color.rgb(83, 90, 145),
//        Color.rgb(77, 82, 135),
//        Color.rgb(55, 48, 85),
//        Color.rgb(220, 176, 228),
//        Color.rgb(138, 109, 172)
    )
    //create a new gradient color
    return GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM, colors
    )

}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()