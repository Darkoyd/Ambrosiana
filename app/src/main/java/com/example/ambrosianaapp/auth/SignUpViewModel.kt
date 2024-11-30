package com.example.ambrosianaapp.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.datastore.generated.model.UserLibrary
import com.amplifyframework.datastore.generated.model.Wishlist
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.launch

sealed class SignUpState {
    object Initial : SignUpState()
    object Loading : SignUpState()
    object WaitingForConfirmation : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

class SignUpViewModel(
    private val authManager: AmplifyAuthManager = AmplifyAuthManager()
) : ViewModel() {
    // User input fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // Confirmation code field
    var confirmationCode by mutableStateOf("")

    // State management
    var currentState by mutableStateOf<SignUpState>(SignUpState.Initial)

    private fun validateSignUpInput(): String? {
        return when {
            firstName.isBlank() -> "First name is required"
            lastName.isBlank() -> "Last name is required"
            password != confirmPassword -> "Passwords do not match"
            phone.isBlank() -> "Phone number is required"
            email.isBlank() -> "Email is required"
            !email.contains("@") -> "Invalid email format"
            password.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
    }

    fun signUp() {
        validateSignUpInput()?.let { error ->
            currentState = SignUpState.Error(error)
            return
        }


        viewModelScope.launch {
            currentState = SignUpState.Loading
            try {
                // Create sign-up options with additional attributes
                val signUpOptions =
                    AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email)
                        .build()

                authManager.signUpWithOptions(email, password, signUpOptions).onSuccess {
                        currentState = SignUpState.WaitingForConfirmation
                    }.onFailure { error ->
                        currentState = SignUpState.Error(error.localizedMessage ?: "Sign up failed")
                    }

            } catch (e: Exception) {
                currentState = SignUpState.Error(e.localizedMessage ?: "Sign up failed")
            }
        }
    }

    fun confirmSignUp(onConfirmationSuccess: () -> Unit) {
        if (confirmationCode.isBlank()) {
            currentState = SignUpState.Error("Confirmation code is required")
            return
        }

        viewModelScope.launch {
            currentState = SignUpState.Loading
            try {
                // First confirm the signup
                authManager.confirmSignUp(email, confirmationCode).onSuccess {
                        // After confirmation, sign in the user
                        authManager.signIn(email, password).onSuccess { authUser ->
                                try {
                                    // Now create the user entities with the authenticated session
                                    val user =
                                        User.builder().email(email).phone(phone).address(address)
                                            .firstName(firstName).lastName(lastName).build()

                                    Amplify.API.mutate(ModelMutation.create(user))
                                    Log.d("SignUpViewModel", "Created user in database")

                                    val userLibrary = UserLibrary.builder().user(user).build()
                                    Amplify.API.mutate(ModelMutation.create(userLibrary))
                                    Log.d("SignUpViewModel", "Created user library")

                                    val wishlist = Wishlist.builder().user(user).build()
                                    Amplify.API.mutate(ModelMutation.create(wishlist))
                                    Log.d("SignUpViewModel", "Created wishlist")

                                    currentState = SignUpState.Success
                                    Log.d(
                                        "SignUpViewModel",
                                        "Calling onConfirmationSuccess callback"
                                    )
                                    onConfirmationSuccess()
                                } catch (e: DataStoreException) {
                                    Log.e("SignUpViewModel", "Failed to create user entities", e)
                                    currentState = SignUpState.Error("Failed to setup user account")
                                }
                            }.onFailure { error ->
                                Log.e("SignUpViewModel", "Sign in failed after confirmation", error)
                                currentState =
                                    SignUpState.Error("Failed to sign in after confirmation")
                            }
                    }.onFailure { error ->
                        Log.e("SignUpViewModel", "Confirmation failed", error)
                        currentState =
                            SignUpState.Error(error.localizedMessage ?: "Confirmation failed")
                    }
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Unexpected error during confirmation", e)
                currentState = SignUpState.Error(e.localizedMessage ?: "Confirmation failed")
            }
        }
    }


    fun resetState() {
        currentState = SignUpState.Initial
    }
}