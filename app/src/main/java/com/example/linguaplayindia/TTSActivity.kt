package com.example.linguaplayindia

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TTSActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var inputText: EditText
    private lateinit var spinner: Spinner
    private lateinit var btnSpeak: Button

    // Store current selected language locale
    private var selectedLocale: Locale = Locale.ENGLISH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ttsactivity)

        inputText = findViewById(R.id.editText)
        spinner = findViewById(R.id.spinnerLanguage)
        btnSpeak = findViewById(R.id.btnSpeak)

        // Initialize TTS engine
        tts = TextToSpeech(this, this)

        // Map of supported languages
        val languages = mapOf(
            "English" to Locale.ENGLISH,
            "Hindi" to Locale("hi", "IN"),
            "Telugu" to Locale("te", "IN"),
            "Tamil" to Locale("ta", "IN"),
            "Marathi" to Locale("mr", "IN")
        )

        // Spinner setup
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages.keys.toList())

        // When user selects a language → update selectedLocale
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val langName = spinner.selectedItem.toString()
                selectedLocale = languages[langName]!!
                if (::tts.isInitialized) {
                    // ✅ Set TTS language here
                    val result = tts.setLanguage(selectedLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(applicationContext, "Language not supported on this device", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Speak button
        btnSpeak.setOnClickListener {
            val text = inputText.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Enter text to speak", Toast.LENGTH_SHORT).show()
            } else {
                tts.language = selectedLocale   // ✅ Optional safety
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = selectedLocale // Default to English on startup
            Toast.makeText(this, "TTS Ready", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
