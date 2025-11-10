package com.example.linguaplayindia

import android.content.Context

object ScoreManager {
    private const val PREFS = "ScorePrefs"

    fun addScore(context: Context, correct: Boolean) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val correctCount = prefs.getInt("correct", 0)
        val wrongCount = prefs.getInt("wrong", 0)
        if (correct) editor.putInt("correct", correctCount + 1)
        else editor.putInt("wrong", wrongCount + 1)
        editor.apply()
    }

    fun getCorrect(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt("correct", 0)

    fun getWrong(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt("wrong", 0)
}
