package com.example.linguaplayindia

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class GameErrorSpotterActivity : AppCompatActivity() {

    private lateinit var tvSentence: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnNext: Button

    private lateinit var questions: List<Map<String, Any>>
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_spotter)

        tvSentence = findViewById(R.id.tvSentence)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnNext = findViewById(R.id.btnNext)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val lang = prefs.getString("selected_language", "English") ?: "English"

        // Load questions
        questions = QuestionManager.loadQuestions(this, lang, "error_spotter")

        loadQuestion()

        btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                loadQuestion()
            } else {
                tvFeedback.text = getString(R.string.completed)
                tvFeedback.setTextColor(Color.YELLOW)
                btnNext.visibility = View.GONE
            }
        }
    }

    private fun loadQuestion() {
        tvFeedback.visibility = View.GONE
        val q = questions[currentIndex]

        val sentence = q["sentence"].toString()
        val correct = q["correct"].toString()
        val wrongWord = q["wrong"]?.toString() ?: ""

        tvSentence.text = sentence
        btnNext.visibility = View.GONE

        // Tap behavior
        tvSentence.setOnClickListener {
            tvFeedback.visibility = View.VISIBLE
            if (wrongWord.isNotEmpty()) {
                tvFeedback.text = "❌ ${getString(R.string.wrong)} Correct: $correct"
            } else {
                tvFeedback.text = "✅ ${getString(R.string.correct)}"
            }
            tvFeedback.setTextColor(Color.GREEN)
            ScoreManager.addScore(this, wrongWord.isEmpty())
            animateFeedback(tvFeedback)
            btnNext.visibility = View.VISIBLE
        }
    }

    private fun animateFeedback(view: TextView) {
        ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f).setDuration(400).start()
        ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f).setDuration(400).start()
    }
}
