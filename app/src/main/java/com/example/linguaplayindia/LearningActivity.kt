package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.InputStream

class LearningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLang = prefs.getString("selected_language", "English") ?: "English"

        // Load the learning JSON file based on selected language
        val content = loadLearningContent(selectedLang.lowercase())

        // Map buttons to sections
        findViewById<LinearLayout>(R.id.cardGrammar).setOnClickListener {
            openDetail("Grammar Rules", content["grammar_rules"] ?: emptyList())
        }
        findViewById<LinearLayout>(R.id.cardVocabulary).setOnClickListener {
            openDetail("Vocabulary Builder", content["vocabulary"] ?: emptyList())
        }
        findViewById<LinearLayout>(R.id.cardPractice).setOnClickListener {
            openDetail("Practice Sentences", content["practice_sentences"] ?: emptyList())
        }
        findViewById<LinearLayout>(R.id.cardAudio).setOnClickListener {
            openDetail("Audio Lessons", content["audio_lessons"] ?: emptyList())
        }

        findViewById<TextView>(R.id.tvGreeting).text =
            "Learning Resources - $selectedLang Grammar"
    }

    private fun loadLearningContent(language: String): Map<String, List<String>> {
        val fileName = "learning/$language.json"
        val inputStream: InputStream = assets.open(fileName)
        val json = JSONObject(inputStream.bufferedReader().use { it.readText() })

        return mapOf(
            "grammar_rules" to json.getJSONArray("grammar_rules").let { arr ->
                List(arr.length()) { arr.getString(it) }
            },
            "vocabulary" to json.getJSONArray("vocabulary").let { arr ->
                List(arr.length()) { arr.getJSONObject(it).toString() }
            },
            "practice_sentences" to json.getJSONArray("practice_sentences").let { arr ->
                List(arr.length()) { arr.getJSONObject(it).toString() }
            },
            "audio_lessons" to json.getJSONArray("audio_lessons").let { arr ->
                List(arr.length()) { arr.getString(it) }
            }
        )
    }

    private fun openDetail(title: String, items: List<String>) {
        val intent = Intent(this, LearningDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putStringArrayListExtra("items", ArrayList(items))
        startActivity(intent)
    }
}
