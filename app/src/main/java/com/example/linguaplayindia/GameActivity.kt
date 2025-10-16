package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.linguaplayindia.model.GrammarQuestions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class GameActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var answerBtn: Button
    private var questionList: List<GrammarQuestions> = emptyList()
    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        questionText = findViewById(R.id.txt_question)
        answerBtn = findViewById(R.id.btn_answer)

        loadQuestions()
        showQuestion()

        answerBtn.setOnClickListener {
            score += 1 // dummy scoring
            currentIndex++
            if (currentIndex < questionList.size) {
                showQuestion()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("SCORE", score)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun loadQuestions() {
        val inputStream = assets.open("grammar_data.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<GrammarQuestions>>() {}.type
        questionList = Gson().fromJson(reader, type)
        reader.close()
    }

    private fun showQuestion() {
        val q = questionList[currentIndex]
        questionText.text = q.question
    }
}

