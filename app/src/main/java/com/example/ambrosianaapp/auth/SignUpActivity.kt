package com.example.ambrosianaapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ambrosianaapp.R
import com.example.ambrosianaapp.components.AmbrosianaButton
import com.example.ambrosianaapp.components.AmbrosianaTextField
import com.example.ambrosianaapp.library.LibraryActivity
import com.example.ambrosianaapp.permissions.LocationPermissionScreen
import com.example.ambrosianaapp.permissions.PermissionManager
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

class SignUpActivity : AuthBaseActivity() {
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(this)
    }

    override fun showAuthFlow() {
        setContent {
            SignUpFlow(
                viewModel = viewModel, permissionManager = permissionManager, onSignUpSuccess = {
                    val intent = Intent(this, LibraryActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                })
        }
    }
}

@Composable
private fun SignUpFlow(
    viewModel: SignUpViewModel, permissionManager: PermissionManager, onSignUpSuccess: () -> Unit
) {
    AmbrosianaAppTheme {
        var showLocationPermission by remember {
            mutableStateOf(!permissionManager.checkLocationPermission())
        }

        if (showLocationPermission) {
            LocationPermissionScreen { isGranted ->
                showLocationPermission = false
                // Proceed with sign up regardless of permission result
            }
        } else {
            SignUpScreen(
                viewModel = viewModel, onSignUpSuccess = onSignUpSuccess
            )
        }
    }
}

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel, onSignUpSuccess: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbrosianaColor.Details)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .background(AmbrosianaColor.Primary)
                .padding(32.dp),
            state = viewModel.currentState
        )

        when (viewModel.currentState) {
            SignUpState.WaitingForConfirmation -> ConfirmationForm(viewModel, onSignUpSuccess)
            else -> SignUpForm(viewModel, modifier = Modifier.verticalScroll(scrollState))
        }
    }
}

@Composable
private fun Header(modifier: Modifier, state: SignUpState) {
    Box(
        modifier = modifier, contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = when (state) {
                SignUpState.WaitingForConfirmation -> "Confirm Your Email"
                else -> "Create Your Account"
            }, style = MaterialTheme.typography.displayMedium, color = AmbrosianaColor.Black
        )
    }
}

@Composable
private fun ConfirmationForm(
    viewModel: SignUpViewModel, onConfirmationSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "We've sent a confirmation code to ${viewModel.email}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AmbrosianaTextField(
            value = viewModel.confirmationCode,
            onValueChange = { viewModel.confirmationCode = it },
            label = "Confirmation Code",
            modifier = Modifier.padding(52.dp, 0.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error message for confirmation
        when (viewModel.currentState) {
            is SignUpState.Error -> {
                Text(
                    text = (viewModel.currentState as SignUpState.Error).message,
                    color = AmbrosianaColor.Green,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            else -> {}
        }

        AmbrosianaButton(
            onClick = { viewModel.confirmSignUp(onConfirmationSuccess) },
            enabled = viewModel.currentState !is SignUpState.Loading,
            text = if (viewModel.currentState is SignUpState.Loading) "Confirming..." else "Confirm Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
private fun SignUpForm(
    viewModel: SignUpViewModel, modifier: Modifier
) {
    val showPassword = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Existing form fields...
        AmbrosianaTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = "First Name",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = "Last Name",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = "Email Address",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = "Phone",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.address,
            onValueChange = { viewModel.address = it },
            label = "Address",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Password",
            modifier = Modifier.padding(52.dp, 0.dp),
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        painter = painterResource(R.drawable.visibility_24px),
                        contentDescription = "Toggle password visibility",
                        tint = AmbrosianaColor.Green
                    )
                }
            })

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = "Confirm Password",
            modifier = Modifier.padding(52.dp, 0.dp),
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        painter = painterResource(R.drawable.visibility_24px),
                        contentDescription = "Toggle password visibility",
                        tint = AmbrosianaColor.Green
                    )
                }
            })

        // Error message
        when (viewModel.currentState) {
            is SignUpState.Error -> {
                Text(
                    text = (viewModel.currentState as SignUpState.Error).message,
                    color = AmbrosianaColor.Green,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaButton(
            onClick = { viewModel.signUp() },
            enabled = viewModel.currentState !is SignUpState.Loading,
            text = if (viewModel.currentState is SignUpState.Loading) "Creating Account..." else "Sign Up"
        )
    }
}