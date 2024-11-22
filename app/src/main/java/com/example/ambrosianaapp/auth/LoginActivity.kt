package com.example.ambrosianaapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.ambrosianaapp.library.LibraryActivity
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        Log.d("SignUpActivity", "SignUp success callback triggered")
                        val intent = Intent(this, LibraryActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}



@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val showPassword = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbrosianaColor.Details),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth().background(AmbrosianaColor.Primary)
                .padding(32.dp)
        )
        Form(viewModel, showPassword, onLoginSuccess, modifier = Modifier.padding(0.dp, 40.dp))

    }


}

@Composable
private fun Form(
    viewModel: LoginViewModel,
    showPassword: MutableState<Boolean>,
    onLoginSuccess: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {// Email TextField
        AmbrosianaTextField(
        value = viewModel.email,
        onValueChange = { viewModel.email = it },
        label = "Email",
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

        // Error Message
        viewModel.errorMessage?.let { error ->
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        AmbrosianaButton(
        onClick = { viewModel.login(onLoginSuccess) },
        enabled = !viewModel.isLoading,
        text = if (viewModel.isLoading) "Loading..." else "Login"
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
            text = "Log into Ambrosiana!",
            style = MaterialTheme.typography.displayMedium,
            color = AmbrosianaColor.Black
        )
    }

}