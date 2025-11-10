package com.example.linguaplayindia

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class GameSentenceBuilderActivity : AppCompatActivity() {

    private lateinit var tvPrompt: TextView
    private lateinit var builtSentence: TextView
    private lateinit var container: LinearLayout
    private lateinit var btnNext: Button

    private lateinit var sentences: List<Map<String, Any>>
    private val userSentence = mutableListOf<String>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_sentence_builder)

        tvPrompt = findViewById(R.id.tvPrompt)
        builtSentence = findViewById(R.id.tvBuiltSentence)
        container = findViewById(R.id.wordContainer)
        btnNext = findViewById(R.id.btnNext)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val lang = prefs.getString("selected_language", "English") ?: "English"

        // Load questions by language
        sentences = QuestionManager.loadQuestions(this, lang, "sentence_builder")

        loadSentence()

        btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < sentences.size) {
                loadSentence()
            } else {
                builtSentence.setTextColor(Color.YELLOW)
                builtSentence.text = getString(R.string.completed)
                btnNext.isEnabled = false
            }
        }
    }

    private fun loadSentence() {
        userSentence.clear()
        container.removeAllViews()
        builtSentence.text = ""

        val q = sentences[currentIndex]
        tvPrompt.text = q["prompt"]?.toString() ?: getString(R.string.arrange_words)

        // ✅ Safe JSON handling
        val correctList: List<String> = when (val raw = q["correct"]) {
            is JSONArray -> (0 until raw.length()).map { raw.getString(it) }
            is List<*> -> raw.map { it.toString() }
            else -> emptyList()
        }

        val shuffled = correctList.shuffled()

        for (word in shuffled) {
            val btn = Button(this).apply {
                text = word
                textSize = 18f
                setBackgroundColor(Color.DKGRAY)
                setTextColor(Color.WHITE)
                setOnClickListener {
                    userSentence.add(word)
                    builtSentence.text = userSentence.joinToString(" ")
                    isEnabled = false
                    if (userSentence.size == correctList.size) checkSentence(correctList)
                }
            }
            container.addView(btn)
        }

        btnNext.visibility = View.GONE
    }

    private fun checkSentence(correctList: List<String>) {
        btnNext.visibility = View.VISIBLE
        if (userSentence == correctList) {
            builtSentence.setTextColor(Color.GREEN)
            builtSentence.text = getString(R.string.correct)
            ScoreManager.addScore(this, true)
        } else {
            builtSentence.setTextColor(Color.RED)
            builtSentence.text = getString(R.string.wrong)
            ScoreManager.addScore(this, false)
        }
    }
}
