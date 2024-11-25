package com.example.ambrosianaapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.ambrosianaapp.library.LibraryActivity
import kotlinx.coroutines.launch

abstract class AuthBaseActivity : ComponentActivity() {
    private val sessionManager = SessionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for existing session before showing any UI
        checkSession()
    }

    private fun checkSession() {
        lifecycleScope.launch {
            when (sessionManager.checkAuthSession()) {
                is SessionManager.AuthState.SignedIn -> {
                    Log.i("AuthBaseActivity", "Active session found, redirecting to app")
                    navigateToApp()
                }
                is SessionManager.AuthState.SignedOut -> {
                    Log.i("AuthBaseActivity", "No active session, showing auth flow")
                    showAuthFlow()
                }
                SessionManager.AuthState.Unknown -> {
                    Log.w("AuthBaseActivity", "Unknown auth state, showing auth flow")
                    showAuthFlow()
                }
            }
        }
    }

    private fun navigateToApp() {
        startActivity(Intent(this, LibraryActivity::class.java))
        finish()
    }

    protected abstract fun showAuthFlow()
}