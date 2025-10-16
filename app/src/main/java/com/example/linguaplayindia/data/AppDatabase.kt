package com.example.linguaplayindia.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GrammarEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun grammarDao(): GrammarDao
}
