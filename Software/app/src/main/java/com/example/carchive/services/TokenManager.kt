package com.example.carchive.services
import com.example.carchive.MainActivity

class TokenManager {
    companion object {
        private const val TOKEN_KEY = "jwt_token"
    }

    private val prefs = MainActivity.getPrefs()

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}