package com.example.linguaplayindia

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.*
import java.util.*

class TranslatorActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var inputText: EditText
    private lateinit var outputText: TextView
    private lateinit var btnTranslate: Button
    private lateinit var btnSpeak: Button
    private lateinit var sourceSpinner: Spinner
    private lateinit var targetSpinner: Spinner
    private lateinit var tts: TextToSpeech

    private val languages = mapOf(
        "English" to TranslateLanguage.ENGLISH,
        "Hindi" to TranslateLanguage.HINDI,
        "Telugu" to TranslateLanguage.TELUGU,
        "Tamil" to TranslateLanguage.TAMIL,
        "Marathi" to TranslateLanguage.MARATHI
    )

    private val locales = mapOf(
        "English" to Locale.ENGLISH,
        "Hindi" to Locale("hi", "IN"),
        "Telugu" to Locale("te", "IN"),
        "Tamil" to Locale("ta", "IN"),
        "Marathi" to Locale("mr", "IN")
    )

    private var targetLocale: Locale = Locale.ENGLISH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        inputText = findViewById(R.id.inputText)
        outputText = findViewById(R.id.outputText)
        btnTranslate = findViewById(R.id.btnTranslate)
        btnSpeak = findViewById(R.id.btnSpeak)
        sourceSpinner = findViewById(R.id.sourceSpinner)
        targetSpinner = findViewById(R.id.targetSpinner)

        // Initialize Text-to-Speech
        tts = TextToSpeech(this, this)

        val langNames = languages.keys.toList()
        sourceSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, langNames)
        targetSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, langNames)

        btnTranslate.setOnClickListener {
            val text = inputText.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Enter text to translate", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sourceLang = languages[sourceSpinner.selectedItem.toString()]!!
            val targetLang = languages[targetSpinner.selectedItem.toString()]!!
            targetLocale = locales[targetSpinner.selectedItem.toString()]!!

            translateText(text, sourceLang, targetLang)
        }

        btnSpeak.setOnClickListener {
            val text = outputText.text.toString()
            if (text.isNotEmpty()) {
                tts.language = targetLocale
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Toast.makeText(this, "Translate something first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun translateText(text: String, sourceLang: String, targetLang: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder().requireWifi().build()

        Toast.makeText(this, "Downloading language models...", Toast.LENGTH_SHORT).show()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Toast.makeText(this, "Model ready! Translating...", Toast.LENGTH_SHORT).show()

                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        outputText.text = translatedText
                        btnSpeak.isEnabled = true // Enable Speak button
                    }
                    .addOnFailureListener {
                        outputText.text = "❌ Translation failed: ${it.message}"
                        btnSpeak.isEnabled = false
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Model download failed: ${it.message}", Toast.LENGTH_LONG).show()
                btnSpeak.isEnabled = false
            }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.ENGLISH
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
