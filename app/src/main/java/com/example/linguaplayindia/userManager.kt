package com.example.linguaplayindia.auth

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.experimental.and
import kotlin.text.Charsets.UTF_8

data class User(
    val id: String,          // UUID or email
    val name: String,
    val email: String,
    val saltHex: String,
    val hashHex: String,
    val createdAt: Long
)

object UserManager {
    private const val FILE_NAME = "users.json"
    private const val MAX_USERS = 20
    private val random = SecureRandom()

    // PBKDF2 settings
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256

    private fun file(context: Context): File = File(context.filesDir, FILE_NAME)

    suspend fun loadAll(context: Context): List<User> = withContext(Dispatchers.IO) {
        val f = file(context)
        if (!f.exists()) return@withContext emptyList<User>()
        val text = f.readText(UTF_8)
        if (text.isBlank()) return@withContext emptyList()
        val arr = JSONArray(text)
        val list = mutableListOf<User>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list += User(
                id = o.getString("id"),
                name = o.getString("name"),
                email = o.getString("email"),
                saltHex = o.getString("salt"),
                hashHex = o.getString("hash"),
                createdAt = o.optLong("createdAt", System.currentTimeMillis())
            )
        }
        list
    }

    private suspend fun persist(context: Context, users: List<User>) = withContext(Dispatchers.IO) {
        val arr = JSONArray()
        users.forEach { u ->
            val o = JSONObject()
            o.put("id", u.id)
            o.put("name", u.name)
            o.put("email", u.email)
            o.put("salt", u.saltHex)
            o.put("hash", u.hashHex)
            o.put("createdAt", u.createdAt)
            arr.put(o)
        }
        file(context).writeText(arr.toString())
    }

    suspend fun registerUser(context: Context, name: String, email: String, password: CharArray): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val users = loadAll(context).toMutableList()
            if (users.any { it.email.equals(email, ignoreCase = true) }) {
                return@withContext Result.failure(Exception("User with this email already exists"))
            }
            if (users.size >= MAX_USERS) return@withContext Result.failure(Exception("User limit reached"))
            // create salt & hash
            val salt = ByteArray(16)
            random.nextBytes(salt)
            val hash = pbkdf2(password, salt)
            val user = User(
                id = email.lowercase(), name = name, email = email.lowercase(),
                saltHex = salt.toHex(), hashHex = hash.toHex(), createdAt = System.currentTimeMillis()
            )
            users.add(user)
            persist(context, users)
            Result.success(Unit)
        }
    }

    suspend fun validateUser(context: Context, email: String, password: CharArray): Result<User> =
        withContext(Dispatchers.IO) {
            val users = loadAll(context)
            val user = users.find { it.email.equals(email, ignoreCase = true) }
                ?: return@withContext Result.failure(Exception("Incorrect email or password"))
            val salt = user.saltHex.hexToBytes()
            val expected = user.hashHex.hexToBytes()
            val computed = pbkdf2(password, salt)
            if (!computed.contentEquals(expected)) return@withContext Result.failure(Exception("Incorrect email or password"))
            Result.success(user)
        }

    // PBKDF2 helper
    private fun pbkdf2(password: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
        val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val key = skf.generateSecret(spec)
        spec.clearPassword()
        return key.encoded
    }

    // helpers for hex
    private fun ByteArray.toHex(): String {
        val sb = StringBuilder(this.size * 2)
        this.forEach {
            sb.append(String.format("%02x", it))
        }
        return sb.toString()
    }
    private fun String.hexToBytes(): ByteArray {
        val len = this.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i+1], 16)).toByte()
            i += 2
        }
        return data
    }
}
