package com.example.ambrosianaapp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(getSystemService(ConnectivityManager::class.java)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {

                val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

                WelcomeScreen(
                    onLoginClick = {
                        //startActivity(Intent(this, LoginActivity::class.java))
                    },
                    onSignUpClick = {
                        //startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    isOnline = isOnline
                )
            }
        }
    }
}

object AppColors {
    val Secondary = Color(0xFFAAADC4)
    val Grey = Color(0xFF8D909B)
    val Primary = Color(0xFFD9F2B4)
    val Green = Color(0xFF29524A)
    val Black = Color(0xFF06070E)
    val White = Color(0xFFFFFFFF)
}

@Composable
fun AmbrosianaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(0.7f),
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.Green,
            disabledContainerColor = AppColors.Grey
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            color = if (enabled) AppColors.White else AppColors.Secondary,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isOnline: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Secondary)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.Primary)
                .padding(32.dp)
        )

        MainContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 32.dp),
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
            isOnline = isOnline
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Welcome to Ambrosiana!",
            style = MaterialTheme.typography.displayMedium,
            color = AppColors.Black
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isOnline: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Log into the\nmarketplace...",
            style = MaterialTheme.typography.headlineLarge,
            color = AppColors.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        AmbrosianaButton(
            onClick = onLoginClick,
            enabled = isOnline,
            text = "Login",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or sign up now!",
            style = MaterialTheme.typography.headlineLarge,
            color = AppColors.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        AmbrosianaButton(
            onClick = onSignUpClick,
            enabled = isOnline,
            text = "Sign up",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (!isOnline) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No internet connection",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// Network connectivity utility
fun ConnectivityManager.observeConnectivity(): Flow<Boolean> = callbackFlow {
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(true)
        }

        override fun onLost(network: Network) {
            trySend(false)
        }
    }

    val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    registerNetworkCallback(request, callback)

    awaitClose {
        unregisterNetworkCallback(callback)
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onLoginClick = {},
        onSignUpClick = {},
        isOnline = true
    )
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenOfflinePreview() {
    WelcomeScreen(
        onLoginClick = {},
        onSignUpClick = {},
        isOnline = false
    )
}