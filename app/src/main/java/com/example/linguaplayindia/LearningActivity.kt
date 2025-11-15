package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import org.json.JSONObject
import java.io.InputStream

class LearningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        // 🔙 Back Button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Load selected language
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLang = prefs.getString("selected_language", "English") ?: "English"

        val content = loadLearningContent(selectedLang.lowercase())

        // Heading
        findViewById<TextView>(R.id.tvHeading).text =
            "Learning Resources - $selectedLang"

        // 📘 Grammar Card
        findViewById<CardView>(R.id.cardGrammar).setOnClickListener {
            openDetail("Grammar Rules", content["grammar_rules"] ?: emptyList())
        }

        // 🗣 Vocabulary Card
        findViewById<CardView>(R.id.cardVocabulary).setOnClickListener {
            openDetail("Vocabulary Builder", content["vocabulary"] ?: emptyList())
        }

        // ✍️ Practice Card
        findViewById<CardView>(R.id.cardPractice).setOnClickListener {
            openDetail("Practice Sentences", content["practice_sentences"] ?: emptyList())
        }

        // 🔊 Audio Lessons
        findViewById<CardView>(R.id.cardAudio).setOnClickListener {
            openDetail("Audio Lessons", content["audio_lessons"] ?: emptyList())
        }
    }

    private fun openDetail(title: String, items: List<String>) {
        val intent = Intent(this, LearningDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putStringArrayListExtra("items", ArrayList(items))
        startActivity(intent)
    }

    private fun loadLearningContent(language: String): Map<String, List<String>> {
        val fileName = "learning/$language.json"
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val json = JSONObject(inputStream.bufferedReader().use { it.readText() })

            mapOf(
                "grammar_rules" to List(json.getJSONArray("grammar_rules").length()) {
                    json.getJSONArray("grammar_rules").getString(it)
                },
                "vocabulary" to List(json.getJSONArray("vocabulary").length()) {
                    json.getJSONArray("vocabulary").getJSONObject(it).toString()
                },
                "practice_sentences" to List(json.getJSONArray("practice_sentences").length()) {
                    json.getJSONArray("practice_sentences").getJSONObject(it).toString()
                },
                "audio_lessons" to List(json.getJSONArray("audio_lessons").length()) {
                    json.getJSONArray("audio_lessons").getString(it)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            mapOf(
                "grammar_rules" to emptyList(),
                "vocabulary" to emptyList(),
                "practice_sentences" to emptyList(),
                "audio_lessons" to emptyList()
            )
        }
    }
}
