package com.example.linguaplayindia

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class GameGapFillActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnNext: Button
    private lateinit var options: List<Button>

    private var currentIndex = 0
    private lateinit var questions: List<Map<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_gap_fill)

        tvQuestion = findViewById(R.id.tvQuestion)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnNext = findViewById(R.id.btnNext)
        options = listOf(
            findViewById(R.id.opt1),
            findViewById(R.id.opt2),
            findViewById(R.id.opt3),
            findViewById(R.id.opt4)
        )

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val language = prefs.getString("selected_language", "English") ?: "English"

        // Load questions based on selected language
        questions = QuestionManager.loadQuestions(this, language, "gap_fill")

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
        val q = questions[currentIndex]
        tvFeedback.visibility = View.GONE
        tvQuestion.text = q["question"].toString()

        val correctAnswer = q["answer"].toString()

        // ✅ Safe handling for both JSONArray and ArrayList
        val opts: List<String> = when (val raw = q["options"]) {
            is JSONArray -> (0 until raw.length()).map { raw.getString(it) }
            is List<*> -> raw.map { it.toString() }
            else -> emptyList()
        }

        options.forEachIndexed { index, btn ->
            btn.text = opts.getOrNull(index) ?: ""
            btn.isEnabled = true
            btn.setBackgroundColor(Color.DKGRAY)

            btn.setOnClickListener {
                if (btn.text == correctAnswer) {
                    showCorrect(btn)
                } else {
                    showWrong(btn)
                }
                options.forEach { it.isEnabled = false }
            }
        }
    }

    private fun showCorrect(button: Button) {
        tvFeedback.visibility = View.VISIBLE
        tvFeedback.text = getString(R.string.correct)
        tvFeedback.setTextColor(Color.GREEN)
        ScoreManager.addScore(this, true)
        animateButton(button)
    }

    private fun showWrong(button: Button) {
        tvFeedback.visibility = View.VISIBLE
        tvFeedback.text = getString(R.string.wrong)
        tvFeedback.setTextColor(Color.RED)
        ScoreManager.addScore(this, false)
        animateButton(button)
    }

    private fun animateButton(button: Button) {
        ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.1f, 1f).setDuration(400).start()
        ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.1f, 1f).setDuration(400).start()
    }
}
