package com.example.ambrosianaapp.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenDefaultPreview() {
    AmbrosianaAppTheme {
        LoginScreen(
            viewModel = LoginViewModel().apply {
                email = "user@example.com"
                password = "password123"
            },
            onLoginSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenLoadingPreview() {
    AmbrosianaAppTheme {
        LoginScreen(
            viewModel = LoginViewModel().apply {
                email = "user@example.com"
                password = "password123"
                isLoading = true
            },
            onLoginSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenErrorPreview() {
    AmbrosianaAppTheme {
        LoginScreen(
            viewModel = LoginViewModel().apply {
                email = "user@example.com"
                password = "password123"
                errorMessage = "Invalid credentials"
            },
            onLoginSuccess = {}
        )
    }
}

// SignUp Screen Previews
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignUpScreenInitialPreview() {
    AmbrosianaAppTheme {
        SignUpScreen(
            viewModel = SignUpViewModel().apply {
                email = "user@example.com"
                password = "password123"
                username = "johnsmith"
                firstName = "John"
                lastName = "Smith"
                address = "123 Main St, City, Country"
                phone = "+1 (555) 123-4567"
                confirmPassword = "password123"
                currentState = SignUpState.Initial
            },
            onSignUpSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignUpScreenLoadingPreview() {
    AmbrosianaAppTheme {
        SignUpScreen(
            viewModel = SignUpViewModel().apply {
                email = "user@example.com"
                password = "password123"
                username = "johnsmith"
                firstName = "John"
                lastName = "Smith"
                address = "123 Main St, City, Country"
                phone = "+1 (555) 123-4567"
                confirmPassword = "password123"
                currentState = SignUpState.Loading
            },
            onSignUpSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignUpScreenErrorPreview() {
    AmbrosianaAppTheme {
        SignUpScreen(
            viewModel = SignUpViewModel().apply {
                email = "user@example.com"
                password = "password123"
                username = "johnsmith"
                firstName = "John"
                lastName = "Smith"
                address = "123 Main St, City, Country"
                phone = "+1 (555) 123-4567"
                confirmPassword = "different_password"
                currentState = SignUpState.Error("Passwords do not match")
            },
            onSignUpSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignUpScreenConfirmationPreview() {
    AmbrosianaAppTheme {
        SignUpScreen(
            viewModel = SignUpViewModel().apply {
                email = "user@example.com"
                password = "password123"
                username = "johnsmith"
                firstName = "John"
                lastName = "Smith"
                address = "123 Main St, City, Country"
                phone = "+1 (555) 123-4567"
                confirmPassword = "password123"
                confirmationCode = "123456"
                currentState = SignUpState.WaitingForConfirmation
            },
            onSignUpSuccess = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignUpScreenConfirmationErrorPreview() {
    AmbrosianaAppTheme {
        SignUpScreen(
            viewModel = SignUpViewModel().apply {
                email = "user@example.com"
                password = "password123"
                username = "johnsmith"
                firstName = "John"
                lastName = "Smith"
                address = "123 Main St, City, Country"
                phone = "+1 (555) 123-4567"
                confirmPassword = "password123"
                confirmationCode = "123456"
                currentState = SignUpState.Error("Invalid confirmation code")
            },
            onSignUpSuccess = {}
        )
    }
}

// Device Preview Annotations for different screen sizes
@Preview(
    name = "Phone Portrait",
    device = "spec:width=360dp,height=640dp,dpi=480"
)
@Preview(
    name = "Phone Landscape",
    device = "spec:width=640dp,height=360dp,dpi=480"
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=480"
)
@Composable
fun AuthScreensDevicePreview() {
    AmbrosianaAppTheme {
        LoginScreen(
            viewModel = LoginViewModel().apply {
                email = "user@example.com"
                password = "password123"
            },
            onLoginSuccess = {}
        )
    }
}