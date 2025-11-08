package com.zen.videoplayertestapp.core.helper


import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

abstract class DoubleClickListener : View.OnClickListener,View.OnTouchListener {
    private var isSingleEvent = false
    private val doubleClickQualificationSpanInMillis: Long
    private var timestampLastClick: Long
    private val handler: Handler
    private val runnable: Runnable
    private var anchorX = 0F
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - timestampLastClick < doubleClickQualificationSpanInMillis) {
            isSingleEvent = false
            handler.removeCallbacks(runnable)
            onDoubleClick()
            return
        }
        isSingleEvent = true
        handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN)
        timestampLastClick = SystemClock.elapsedRealtime()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (SystemClock.elapsedRealtime() - timestampLastClick < doubleClickQualificationSpanInMillis) {
                    isSingleEvent = false
                    handler.removeCallbacks(runnable)
                    onDoubleClick()

                } else {
                    isSingleEvent = true
                    handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN)
                    timestampLastClick = SystemClock.elapsedRealtime()
                }
                anchorX = event.x
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (Math.abs(event.x - anchorX) > 100) {
                    if (event.x > anchorX) {
                        onSwipRight()
                    } else {
                        onSwipLeft()
                    }
                }
                return true
            }
        }
        return v!!.performClick()
    }


    abstract fun onDoubleClick()
    abstract fun onSingleClick()
    abstract fun onSwipRight()
    abstract fun onSwipLeft()


    companion object {
        private const val DEFAULT_QUALIFICATION_SPAN: Long = 300
    }

    init {
        doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN
        timestampLastClick = 0
        handler = Handler()
        runnable = Runnable {
            if (isSingleEvent) {
                onSingleClick()
            }
        }
    }
}