package com.example.linguaplayindia

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LanguageHelper {

    fun setLocale(context: Context, languageCode: String) {
        val locale = when (languageCode.lowercase()) {
            "hindi" -> Locale("hi")
            "telugu" -> Locale("te")
            "tamil" -> Locale("ta")
            "marathi" -> Locale("mr")
            else -> Locale("en")
        }

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        }

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getLanguageCode(languageName: String): String {
        return when (languageName.lowercase()) {
            "hindi" -> "hi"
            "telugu" -> "te"
            "tamil" -> "ta"
            "marathi" -> "mr"
            else -> "en"
        }
    }
}
