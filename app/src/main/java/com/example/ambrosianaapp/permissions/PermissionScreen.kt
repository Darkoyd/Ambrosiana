package com.example.ambrosianaapp.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ambrosianaapp.components.AmbrosianaButton
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

@Composable
fun PermissionScreen(
    permission: String,
    rationaleTitle: String,
    rationaleMessage: String,
    onPermissionResult: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var shouldShowRationale by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionResult(true)
            }
            shouldShowRationale -> {
                // Show in-app rationale
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbrosianaColor.Details)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = rationaleTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = AmbrosianaColor.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = rationaleMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = AmbrosianaColor.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        AmbrosianaButton(
            onClick = { permissionLauncher.launch(permission) },
            text = "Grant Permission"
        )
    }
}

@Composable
fun LocationPermissionScreen(
    onPermissionResult: (Boolean) -> Unit
) {
    PermissionScreen(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        rationaleTitle = "Location Access",
        rationaleMessage = "Ambrosiana needs access to your location to help connect you with book sellers in your area.",
        onPermissionResult = onPermissionResult
    )
}

@Composable
fun ImagePermissionScreen(
    onPermissionResult: (Boolean) -> Unit
) {
    PermissionScreen(
        permission = Manifest.permission.READ_MEDIA_IMAGES,
        rationaleTitle = "Photo Access",
        rationaleMessage = "Ambrosiana needs access to your photos to let you upload book images.",
        onPermissionResult = onPermissionResult
    )
}