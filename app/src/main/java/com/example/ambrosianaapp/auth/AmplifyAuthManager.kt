package com.example.ambrosianaapp.auth

import android.net.http.NetworkException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import aws.sdk.kotlin.services.cognitoidentityprovider.model.ExpiredCodeException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.cognito.exceptions.invalidstate.SignedInException
import com.amplifyframework.auth.cognito.exceptions.service.CodeMismatchException
import com.amplifyframework.auth.cognito.exceptions.service.LimitExceededException
import com.amplifyframework.auth.cognito.exceptions.service.TooManyRequestsException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotFoundException
import com.amplifyframework.auth.exceptions.NotAuthorizedException
import com.amplifyframework.auth.exceptions.ServiceException
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.example.ambrosianaapp.analytics.AmbrosianaAnalytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AmplifyAuthManager {
    companion object {
        private const val TAG = "AmplifyAuthManager"

        private const val AUTH_SIGNUP = "AuthSignUp"
        private const val AUTH_SIGNIN = "AuthSignIn"
        private const val AUTH_CONFIRM = "AuthConfirm"
        private const val AUTH_SIGNOUT = "AuthSignOut"
        private const val AUTH_STATE = "AuthState"
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun signUpWithOptions(
        email: String, password: String, options: AuthSignUpOptions
    ): Result<Boolean> = runCatching {
        Log.d(
            TAG,
            "$AUTH_SIGNUP: Attempting signup with custom options for email: ${email.masked()}"
        )

        Amplify.Auth.signUp(email, password, options)
        Log.i(
            TAG,
            "$AUTH_SIGNUP: Successfully initiated signup with options for email: ${email.masked()}"
        )
        true
    }.onFailure { exception ->
        logAuthError(AUTH_SIGNUP, "signup with options", email, exception)
    }

    suspend fun confirmSignUp(
        email: String, confirmationCode: String
    ): Result<Boolean> = runCatching {
        Log.d(TAG, "$AUTH_CONFIRM: Attempting to confirm signup for email: ${email.masked()}")

        Amplify.Auth.confirmSignUp(email, confirmationCode)
        AmbrosianaAnalytics.recordAppEvent(
            eventName = "_userauth.sign_up"
        )
        Log.i(TAG, "$AUTH_CONFIRM: Successfully confirmed signup for email: ${email.masked()}")
        true
    }.onFailure { exception ->
        when (exception) {
            is AuthException -> {
                when (exception.cause) {
                    is CodeMismatchException -> {
                        Log.w(
                            TAG,
                            "$AUTH_CONFIRM: Invalid confirmation code for email: ${email.masked()}"
                        )
                    }

                    is ExpiredCodeException -> {
                        Log.w(
                            TAG,
                            "$AUTH_CONFIRM: Confirmation code expired for email: ${email.masked()}"
                        )
                    }

                    is LimitExceededException -> {
                        Log.w(
                            TAG,
                            "$AUTH_CONFIRM: Too many attempts. Please try again later: ${email.masked()}"
                        )
                    }

                    else -> {
                        Log.e(
                            TAG,
                            "$AUTH_CONFIRM: Auth error during confirmation: ${exception.cause?.message}",
                            exception
                        )
                    }
                }
            }

            else -> {
                Log.e(
                    TAG,
                    "$AUTH_CONFIRM: Unexpected error during confirmation for email: ${email.masked()}",
                    exception
                )
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun signIn(
        email: String, password: String
    ): Result<AuthUser> = runCatching {
        Log.d(TAG, "$AUTH_SIGNIN: Attempting signin for email: ${email.masked()}")

        Amplify.Auth.signIn(email, password)
        val currentUser = Amplify.Auth.getCurrentUser()
        Log.i(TAG, "$AUTH_SIGNIN: Successfully signed in user: ${email.masked()}")
        AmbrosianaAnalytics.recordAppEvent(
            eventName = "_userauth.sign_in"
        )
        currentUser
    }.onFailure { exception ->
        when (exception) {
            is AuthException -> {
                when (exception.cause) {
                    is SignedInException -> {
                        signOutAndRetry(email, password)
                    }

                    is NotAuthorizedException -> {
                        Log.w(TAG, "$AUTH_SIGNIN: Invalid credentials for email: ${email.masked()}")
                    }

                    is UserNotConfirmedException -> {
                        Log.w(TAG, "$AUTH_SIGNIN: User not confirmed: ${email.masked()}")
                    }

                    is UserNotFoundException -> {
                        Log.w(TAG, "$AUTH_SIGNIN: User not found: ${email.masked()}")
                    }

                    is TooManyRequestsException -> {
                        Log.w(
                            TAG,
                            "$AUTH_SIGNIN: Too many attempts. Please try again later: ${email.masked()}"
                        )
                    }

                    else -> {
                        Log.e(
                            TAG,
                            "$AUTH_SIGNIN: Auth error during signin: ${exception.cause?.message}",
                            exception
                        )
                    }
                }
            }

            else -> {
                Log.e(
                    TAG,
                    "$AUTH_SIGNIN: Unexpected error during signin for email: ${email.masked()}",
                    exception
                )
                logAuthError(
                    operation = AUTH_SIGNIN,
                    action = "sign in",
                    email = email,
                    exception = exception,
                )
            }
        }
    }

    private suspend fun signOutAndRetry(
        email: String, password: String
    ) {
        signOut()
        signIn(email, password)
    }

    suspend fun resendSignUpCode(
        email: String
    ): Result<Boolean> = runCatching {
        Log.d(
            TAG,
            "$AUTH_SIGNUP: Attempting to resend confirmation code for email: ${email.masked()}"
        )

        Amplify.Auth.resendSignUpCode(email)
        Log.i(
            TAG,
            "$AUTH_SIGNUP: Successfully resent confirmation code for email: ${email.masked()}"
        )
        true
    }.onFailure { exception ->
        when (exception) {
            is AuthException -> {
                when (exception.cause) {
                    is LimitExceededException -> {
                        Log.w(
                            TAG,
                            "$AUTH_SIGNUP: Too many code resend attempts for email: ${email.masked()}"
                        )
                    }

                    is UserNotFoundException -> {
                        Log.w(
                            TAG,
                            "$AUTH_SIGNUP: User not found when resending code: ${email.masked()}"
                        )
                    }

                    else -> {
                        Log.e(
                            TAG,
                            "$AUTH_SIGNUP: Auth error during code resend: ${exception.cause?.message}",
                            exception
                        )
                    }
                }
            }

            else -> {
                Log.e(
                    TAG,
                    "$AUTH_SIGNUP: Unexpected error during code resend for email: ${email.masked()}",
                    exception
                )
            }
        }
    }

    suspend fun signOut(): Result<Boolean> = runCatching {
        Log.d(TAG, "$AUTH_SIGNOUT: Attempting to sign out current user")

        Amplify.Auth.signOut()
        Log.i(TAG, "$AUTH_SIGNOUT: Successfully signed out user")

        true
    }.onFailure { exception ->
        Log.e(TAG, "$AUTH_SIGNOUT: Error during sign out", exception)
    }

    fun observeAuthState(): Flow<AuthState> = flow {
        Log.d(TAG, "$AUTH_STATE: Starting auth state observation")

        try {
            val currentUser = Amplify.Auth.getCurrentUser()
            Log.d(TAG, "$AUTH_STATE: Current user found: ${currentUser.username.masked()}")
            emit(AuthState.SignedIn(currentUser))
        } catch (e: Exception) {
            Log.d(TAG, "$AUTH_STATE: No authenticated user found")
            emit(AuthState.SignedOut)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun logAuthError(
        operation: String,
        action: String,
        email: String,
        exception: Throwable
    ) {
        AmbrosianaAnalytics.recordAppEvent(
            eventName = "_userauth.auth_fail"
        )
        when (exception) {
            is AuthException -> {
                when (exception.cause) {
                    is ServiceException -> {
                        Log.e(
                            TAG,
                            "$operation: Service error during $action for email: ${email.masked()}",
                            exception
                        )
                    }

                    is NetworkException -> {
                        Log.e(
                            TAG,
                            "$operation: Network error during $action for email: ${email.masked()}",
                            exception
                        )
                    }

                    else -> {
                        Log.e(
                            TAG,
                            "$operation: Auth error during $action: ${exception.cause?.message}",
                            exception
                        )
                    }
                }
            }

            else -> {
                Log.e(
                    TAG,
                    "$operation: Unexpected error during $action for email: ${email.masked()}",
                    exception
                )
            }
        }
    }

    sealed class AuthState {
        data class SignedIn(val user: AuthUser) : AuthState()
        object SignedOut : AuthState()
    }
}

// Extension function to mask sensitive data in logs
private fun String.masked(): String {
    return if (this.contains("@")) {
        val parts = this.split("@")
        "${parts[0].take(2)}***@${parts[1]}"
    } else {
        "${this.take(2)}***${this.takeLast(2)}"
    }
}

