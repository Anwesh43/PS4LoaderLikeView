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
val crossDeg : Float = 45f
val plusDeg : Float = 90f
val fullDeg : Float = 360f


fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse()) // -> curr point which we are animating.
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n // -> the ith part
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawTriangle(size : Float, paint : Paint) {
    val path : Path = Path()
    path.moveTo(-size, size)
    path.lineTo(size, size)
    path.lineTo(0f, -size)
    drawPath(path, paint)
}

fun Canvas.drawSquare(size : Float, paint : Paint) {
    drawRect(RectF(-size, -size, size, size), paint)
}

fun Canvas.drawCircle(size : Float, paint : Paint) {
    drawCircle(0f, 0f, size, paint)
}

fun Canvas.drawCross(size : Float, paint : Paint) {
    save()
    rotate(crossDeg)
    for (j in 0..1) {
        save()
        rotate(90f)
        drawLine(0f, -size, 0f, size, paint)
        restore()
    }
    restore()
}

fun Canvas.getShapeDrawArray() : Array<(Float, Paint) -> Unit> {

    val drawSqFn : (Float, Paint) -> Unit = {size, paint ->
        drawSquare(size, paint)
    }
    val drawTriangleFn : (Float, Paint) -> Unit = {size, paint ->
        drawSquare(size, paint)
    }
    val drawCrossFn : (Float, Paint) -> Unit = {size, paint ->
        drawCross(size, paint)
    }
    val drawCircleFn : (Float, Paint) -> Unit = {size, paint ->
        drawCircle(size, paint)
    }
    return arrayOf(drawSqFn, drawTriangleFn, drawCircleFn, drawCircleFn)
}


fun Canvas.drawShape(size : Float, scale : Float, paint : Paint) {
    val scDiv : Double = 1.0 / shapes
    val i : Int = Math.floor(scale / scDiv).toInt()
    val sf : Float = scale.divideScale(i, shapes).sinify()
    val newSize : Float = size * sf
    save()
    rotate(fullDeg * newSize)
    getShapeDrawArray()[i]
    restore()
}

fun Canvas.drawPS4Node(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w / 2, gap * (i + 1))
    restore()
}

class PS4LoaderLikeView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class PS4Node(var i : Int, val state : State = State()) {

        private var next : PS4Node? = null
        private var prev : PS4Node? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = PS4Node(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawPS4Node(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : PS4Node {
            var curr : PS4Node? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}