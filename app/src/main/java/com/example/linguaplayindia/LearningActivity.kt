package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONObject
import java.io.InputStream

class LearningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        // Setup Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLang = prefs.getString("selected_language", "English") ?: "English"

        val content = loadLearningContent(selectedLang.lowercase())

        findViewById<TextView>(R.id.tvGreeting).text =
            "Learning Resources - $selectedLang Grammar"

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
        findViewById<LinearLayout>(R.id.cardQuiz).setOnClickListener {
            startActivity(Intent(this, GamesActivity::class.java))
        }
    }

    private fun loadLearningContent(language: String): Map<String, List<String>> {
        val fileName = "learning/$language.json"
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val json = JSONObject(inputStream.bufferedReader().use { it.readText() })

            mapOf(
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
        } catch (e: Exception) {
            e.printStackTrace()
            // fallback empty content if file missing / parse error
            mapOf(
                "grammar_rules" to emptyList(),
                "vocabulary" to emptyList(),
                "practice_sentences" to emptyList(),
                "audio_lessons" to emptyList()
            )
        }
    }

    private fun openDetail(title: String, items: List<String>) {
        val intent = Intent(this, LearningDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putStringArrayListExtra("items", ArrayList(items))
        startActivity(intent)
    }
}
