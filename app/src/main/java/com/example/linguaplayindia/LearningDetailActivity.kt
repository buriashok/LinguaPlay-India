package com.example.linguaplayindia

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LearningDetailActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isAudio = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_detail)

        val title = intent.getStringExtra("title") ?: ""
        val items = intent.getStringArrayListExtra("items") ?: arrayListOf()

        findViewById<TextView>(R.id.tvTitle).text = title
        val container = findViewById<LinearLayout>(R.id.container)

        isAudio = title.contains("Audio", ignoreCase = true)

        if (isAudio) tts = TextToSpeech(this, this)

        items.forEach {
            val tv = TextView(this)
            tv.text = "• $it"
            tv.textSize = 18f
            tv.setTextColor(resources.getColor(android.R.color.white))
            tv.setPadding(0, 10, 0, 10)
            tv.setOnClickListener {
                if (isAudio) tts.speak(tv.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
            }
            container.addView(tv)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) tts.language = Locale.ENGLISH
    }

    override fun onDestroy() {
        if (isAudio && ::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
