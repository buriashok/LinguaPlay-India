package com.example.linguaplayindia

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val tvCorrect = findViewById<TextView>(R.id.tvCorrect)
        val tvWrong = findViewById<TextView>(R.id.tvWrong)

        tvCorrect.text = "✅ Correct Answers: ${ScoreManager.getCorrect(this)}"
        tvWrong.text = "❌ Wrong Answers: ${ScoreManager.getWrong(this)}"
    }
}
