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
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authManager: AmplifyAuthManager = AmplifyAuthManager()
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun signUp(onSuccess: () -> Unit) {
        // Enhanced validation
        when {
            firstName.isBlank() -> {
                errorMessage = "First name is required"
                return
            }
            lastName.isBlank() -> {
                errorMessage = "Last name is required"
                return
            }
            password != confirmPassword -> {
                errorMessage = "Passwords do not match"
                return
            }
            phone.isBlank() -> {
                errorMessage = "Phone number is required"
                return
            }
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
            // Create sign-up options with additional attributes
            val signUpOptions = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build()

            authManager.signUpWithOptions(email, password, signUpOptions)
                .onSuccess {
                    isLoading = false
                    onSuccess()
                }
                .onFailure {
                    isLoading = false
                    errorMessage = it.localizedMessage ?: "Sign up failed"
                }

            val user = User.builder()
                .email(email)
                .phone(phone)
                .address(address)
                .firstName(firstName)
                .lastName(lastName)
                .build()


                Amplify.API.mutate(ModelMutation.create(user))
            } catch (failure: DataStoreException) {
                Log.e("AmbrosianaAmplify", "Save failed", failure)
            }
        }
    }
}