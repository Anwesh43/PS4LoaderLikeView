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
