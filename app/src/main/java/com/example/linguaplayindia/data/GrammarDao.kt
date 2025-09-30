package com.example.linguaplayindia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GrammarDao {
    @Insert
    suspend fun insertScore(score: GrammarEntity)

    @Query("SELECT * FROM grammar_scores")
    suspend fun getAllScores(): List<GrammarEntity>
}
