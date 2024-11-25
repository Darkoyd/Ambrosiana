package com.example.ambrosianaapp.auth

import android.util.Log
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager {
    companion object {
        private const val TAG = "SessionManager"
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState: StateFlow<AuthState> = _authState

    suspend fun checkAuthSession(): AuthState {
        return try {
            Log.d(TAG, "Checking current auth session")
            val session = Amplify.Auth.fetchAuthSession()

            if (session.isSignedIn) {
                val user = Amplify.Auth.getCurrentUser()
                Log.i(TAG, "User is signed in: ${user.username.masked()}")
                AuthState.SignedIn(user)
            } else {
                Log.i(TAG, "No active session found")
                AuthState.SignedOut
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking auth session", e)
            AuthState.SignedOut
        }.also {
            _authState.value = it
        }
    }

    sealed class AuthState {
        object Unknown : AuthState()
        object SignedOut : AuthState()
        data class SignedIn(val user: AuthUser) : AuthState()
    }

    private fun String.masked() = "${take(2)}***${takeLast(2)}"
}