package com.example.linguaplayindia.data


class GrammarRepository(private val dao: GrammarDao) {
    suspend fun insert(score: Int) {
        dao.insertScore(GrammarEntity(score = score))
    }

    suspend fun getAllScores() = dao.getAllScores()
}
