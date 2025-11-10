package com.example.linguaplayindia

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.graphics.*
import android.view.MotionEvent
import android.widget.LinearLayout

class DrawingPadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val drawingView = DrawingView(this)
        val clearBtn = Button(this).apply {
            text = "Clear Pad"
            setOnClickListener { drawingView.clearCanvas() }
        }

        layout.addView(clearBtn)
        layout.addView(drawingView)
        setContentView(layout)
    }

    class DrawingView(context: android.content.Context) : View(context) {
        private var path = Path()
        private var paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.BLACK)
            canvas.drawPath(path, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> path.moveTo(x, y)
                MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
            }
            invalidate()
            return true
        }

        fun clearCanvas() {
            path.reset()
            invalidate()
        }
    }
}
