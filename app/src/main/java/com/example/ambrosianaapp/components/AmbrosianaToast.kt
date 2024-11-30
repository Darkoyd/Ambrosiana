package com.example.ambrosianaapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor
import kotlinx.coroutines.delay

@Composable
fun AmbrosianaToast(
    message: String, isVisible: Boolean, onDismiss: () -> Unit, durationMillis: Long = 2000
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(visible = isVisible, enter = fadeIn() + slideInVertically {
            with(density) { 50.dp.roundToPx() }
        }, exit = fadeOut() + slideOutVertically {
            with(density) { 50.dp.roundToPx() }
        }) {
            Surface(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = AmbrosianaColor.Green,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = message,
                    color = AmbrosianaColor.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(durationMillis)
            onDismiss()
        }
    }
}

class ToastState {
    var isVisible by mutableStateOf(false)
        private set
    var message by mutableStateOf("")
        private set

    fun show(message: String) {
        this.message = message
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}

@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}