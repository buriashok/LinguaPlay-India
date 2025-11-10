package com.example.linguaplayindia

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.linguaplayindia.api.DictionaryService
import com.example.linguaplayindia.api.WordResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryActivity : AppCompatActivity() {
    private lateinit var inputWord: EditText
    private lateinit var outputMeaning: TextView
    private lateinit var btnSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        inputWord = findViewById(R.id.inputWord)
        outputMeaning = findViewById(R.id.outputMeaning)
        btnSearch = findViewById(R.id.btnSearch)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DictionaryService::class.java)

        btnSearch.setOnClickListener {
            val word = inputWord.text.toString().trim()
            if (word.isEmpty()) return@setOnClickListener

            service.getMeaning(word).enqueue(object : Callback<List<WordResponse>> {
                override fun onResponse(call: Call<List<WordResponse>>, response: Response<List<WordResponse>>) {
                    if (response.isSuccessful) {
                        val meaning = response.body()?.firstOrNull()?.meanings?.firstOrNull()
                        val text = "(${meaning?.partOfSpeech}) ${meaning?.definitions?.firstOrNull()?.definition}"
                        outputMeaning.text = text ?: getString(R.string.dictionary_not_found)
                    } else {
                        outputMeaning.text = getString(R.string.dictionary_not_found)
                    }
                }

                override fun onFailure(call: Call<List<WordResponse>>, t: Throwable) {
                    outputMeaning.text = "Error: ${t.message}"
                }
            })
        }
    }
}
