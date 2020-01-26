package com.anwesh.uiprojects.ps4likeloaderview

/**
 * Created by anweshmishra on 26/01/20.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Path
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val shapes : Int = 4
val scGap : Float = 0.02f / shapes
val sizeFactor : Float = 3.2f
val foreColor : Int = Color.parseColor("#BDBDBD")
val backColor : Int = Color.parseColor("#0D47A1")
val delay : Long = 20
val strokeFactor : Float = 90f


fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse()) // -> curr point which we are animating.
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n // -> the ith part
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()
