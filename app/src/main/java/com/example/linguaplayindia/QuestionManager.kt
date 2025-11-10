package com.example.linguaplayindia

import android.content.Context
import org.json.JSONObject
import java.io.InputStream

object QuestionManager {

    fun loadQuestions(context: Context, languageCode: String, gameType: String): List<Map<String, Any>> {
        val fileName = when (languageCode.lowercase()) {
            "hindi" -> "questions_hi.json"
            "telugu" -> "questions_te.json"
            "tamil" -> "questions_ta.json"
            "marathi" -> "questions_mr.json"
            else -> "questions_en.json"
        }

        val jsonString = loadJSONFromAsset(context, fileName)
        val json = JSONObject(jsonString)
        val array = json.getJSONArray(gameType)

        val questionList = mutableListOf<Map<String, Any>>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val map = mutableMapOf<String, Any>()
            obj.keys().forEach { key ->
                map[key] = obj.get(key)
            }
            questionList.add(map)
        }
        return questionList
    }

    private fun loadJSONFromAsset(context: Context, filename: String): String {
        val inputStream: InputStream = context.assets.open(filename)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
