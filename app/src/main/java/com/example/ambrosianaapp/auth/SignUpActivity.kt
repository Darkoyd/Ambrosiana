package com.example.ambrosianaapp.auth

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.unit.dp
import com.example.ambrosianaapp.R
import com.example.ambrosianaapp.components.AmbrosianaButton
import com.example.ambrosianaapp.components.AmbrosianaTextField
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

class SignUpActivity : ComponentActivity() {
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AmbrosianaAppTheme {
                SignUpScreen(
                    viewModel = viewModel,
                    onSignUpSuccess = {
                        // Navigate to confirmation screen or main app
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpSuccess: () -> Unit
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
                .padding(32.dp)
        )

        Form(viewModel, onSignUpSuccess, modifier = Modifier.verticalScroll(scrollState))
    }
}

@Composable
private fun Form(
    viewModel: SignUpViewModel,
    onSignUpSuccess: () -> Unit,
    modifier: Modifier
) {

    val showPassword = remember { mutableStateOf(false) }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        // First Name TextField
        AmbrosianaTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = "First Name",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Last Name TextField
        AmbrosianaTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = "Last Name",
            modifier = Modifier.padding(52.dp, 0.dp),
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        AmbrosianaTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = "Email Address",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone TextField
        AmbrosianaTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = "Phone",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Address TextField
        AmbrosianaTextField(
            value = viewModel.address,
            onValueChange = { viewModel.address = it },
            label = "Address",
            modifier = Modifier.padding(52.dp, 0.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        AmbrosianaTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Password",
            modifier = Modifier.padding(52.dp, 0.dp),
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        painter = painterResource(R.drawable.visibility_24px),
                        contentDescription = "Toggle password visibility",
                        tint = AmbrosianaColor.Green
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password TextField
        AmbrosianaTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = "Confirm Password",
            modifier = Modifier.padding(52.dp, 0.dp),
            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        painter = painterResource(R.drawable.visibility_24px),
                        contentDescription = "Toggle password visibility",
                        tint = AmbrosianaColor.Green
                    )
                }
            }
        )

        // Error Message
        viewModel.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Sign Up Button
        AmbrosianaButton(
            onClick = { viewModel.signUp(onSignUpSuccess) },
            enabled = !viewModel.isLoading,
            text = if (viewModel.isLoading) "Creating Account..." else "Sign Up"
        )
    }
}

@Composable
private fun Header(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Create Your Account",
            style = MaterialTheme.typography.displayMedium,
            color = AmbrosianaColor.Black
        )
    }

}