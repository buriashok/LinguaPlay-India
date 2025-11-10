package com.example.linguaplayindia.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class Definition(val definition: String)
data class Meaning(val partOfSpeech: String, val definitions: List<Definition>)
data class WordResponse(val word: String, val meanings: List<Meaning>)

interface DictionaryService {
    @GET("api/v2/entries/en/{word}")
    fun getMeaning(@Path("word") word: String): Call<List<WordResponse>>
}
