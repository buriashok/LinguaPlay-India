package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UtilityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility)

        findViewById<Button>(R.id.btnTranslator).setOnClickListener {
            startActivity(Intent(this, TranslatorActivity::class.java))
        }
        findViewById<Button>(R.id.btnDrawingPad).setOnClickListener {
            startActivity(Intent(this, DrawingPadActivity::class.java))
        }
        findViewById<Button>(R.id.btnTTS).setOnClickListener {
            startActivity(Intent(this, TTSActivity::class.java))
        }
        findViewById<Button>(R.id.btnDictionary).setOnClickListener {
            startActivity(Intent(this, DictionaryActivity::class.java))
        }
    }
}
