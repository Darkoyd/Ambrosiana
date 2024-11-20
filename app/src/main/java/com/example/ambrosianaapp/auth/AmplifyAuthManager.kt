package com.example.ambrosianaapp.auth

import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AmplifyAuthManager {
    suspend fun signUp(
        email: String,
        password: String
    ): Result<Boolean> = runCatching {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        val result = Amplify.Auth.signUp(email, password, options)
        true // Return true if no exception was thrown
    }

    suspend fun signUpWithOptions(
        email: String,
        password: String,
        options: AuthSignUpOptions
    ): Result<Boolean> = runCatching {
        val result = Amplify.Auth.signUp(email, password, options)
        true // Return true if no exception was thrown
    }

    suspend fun confirmSignUp(
        email: String,
        confirmationCode: String
    ): Result<Boolean> = runCatching {
        Amplify.Auth.confirmSignUp(email, confirmationCode)
        true
    }

    suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthUser> = runCatching {
        Amplify.Auth.signIn(email, password)
        // If sign in is successful, get the current user
        Amplify.Auth.getCurrentUser()
    }

    suspend fun signOut(): Result<Boolean> = runCatching {
        Amplify.Auth.signOut()
        true
    }

    fun observeAuthState(): Flow<AuthState> = flow {
        try {
            val currentUser = Amplify.Auth.getCurrentUser()
            emit(AuthState.SignedIn(currentUser))
        } catch (e: Exception) {
            emit(AuthState.SignedOut)
        }
    }

    sealed class AuthState {
        data class SignedIn(val user: AuthUser) : AuthState()
        object SignedOut : AuthState()
    }
}