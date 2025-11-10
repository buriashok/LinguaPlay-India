package com.example.linguaplayindia

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class GameGrammarTapperActivity : AppCompatActivity() {

    private lateinit var tvSentence: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnNext: Button

    private lateinit var questions: List<Map<String, Any>>
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_tapper)

        tvSentence = findViewById(R.id.tvSentence)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnNext = findViewById(R.id.btnNext)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val lang = prefs.getString("selected_language", "English") ?: "English"

        // Load JSON questions based on selected language
        questions = QuestionManager.loadQuestions(this, lang, "grammar_tapper")

        loadQuestion()

        btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) loadQuestion()
            else {
                tvFeedback.text = getString(R.string.completed)
                tvFeedback.setTextColor(Color.YELLOW)
                btnNext.visibility = View.GONE
            }
        }
    }

    private fun loadQuestion() {
        tvFeedback.visibility = View.GONE
        btnNext.visibility = View.GONE

        val q = questions[currentIndex]
        val sentence = q["sentence"].toString()
        val correct = q["verb"].toString()
        val words = sentence.split(" ")

        val spannable = SpannableString(sentence)
        var start = 0

        for (word in words) {
            val end = start + word.length
            val clickable = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (word.equals(correct, ignoreCase = true)) {
                        showFeedback(true, word)
                    } else {
                        showFeedback(false, word)
                    }
                    btnNext.visibility = View.VISIBLE
                }
            }
            spannable.setSpan(clickable, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end + 1
        }

        tvSentence.text = spannable
        tvSentence.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showFeedback(isCorrect: Boolean, word: String) {
        tvFeedback.visibility = View.VISIBLE
        if (isCorrect) {
            tvFeedback.text = getString(R.string.grammar_correct, word)
            tvFeedback.setTextColor(Color.GREEN)
            animateFeedback(tvFeedback)
            ScoreManager.addScore(this, true)
        } else {
            tvFeedback.text = getString(R.string.grammar_wrong, word)
            tvFeedback.setTextColor(Color.RED)
            animateFeedback(tvFeedback)
            ScoreManager.addScore(this, false)
        }
    }

    private fun animateFeedback(view: TextView) {
        ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f).setDuration(400).start()
        ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f).setDuration(400).start()
    }
}
