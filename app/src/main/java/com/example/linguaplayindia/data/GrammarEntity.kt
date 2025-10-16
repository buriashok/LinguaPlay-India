package com.example.linguaplayindia.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "grammar_scores")
data class GrammarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int
)
