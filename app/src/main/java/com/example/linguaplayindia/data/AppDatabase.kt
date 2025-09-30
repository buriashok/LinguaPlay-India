package com.example.linguaplayindia.data

@Database(entities = [GrammarEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun grammarDao(): GrammarDao
}
