package com.example.ambrosianaapp.book.newbook


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ambrosianaapp.components.AmbrosianaButton
import com.example.ambrosianaapp.components.AmbrosianaTextField
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

class NewBookActivity : ComponentActivity() {
    private val viewModel: NewBookViewModel by viewModels {
        NewBookViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {
                NewBookScreen(
                    viewModel = viewModel, onNavigateUp = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBookScreen(
    viewModel: NewBookViewModel, onNavigateUp: () -> Unit, modifier: Modifier = Modifier
) {
    LocalContext.current
    val scrollState = rememberScrollState()
    val isLoading by viewModel.isLoading.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    LaunchedEffect(submissionState) {
        if (submissionState is NewBookViewModel.SubmissionState.Success) {
            onNavigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Book") }, navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AmbrosianaColor.Primary
            )
            )
        }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmbrosianaColor.Details)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmbrosianaTextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                label = "Title",
                isError = viewModel.titleError != null
            )
            viewModel.titleError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            AmbrosianaTextField(
                value = viewModel.author,
                onValueChange = viewModel::onAuthorChange,
                label = "Author",
                isError = viewModel.authorError != null
            )
            viewModel.authorError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            AmbrosianaTextField(
                value = viewModel.isbn,
                onValueChange = viewModel::onIsbnChange,
                label = "ISBN",
                isError = viewModel.isbnError != null
            )
            viewModel.isbnError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            AmbrosianaButton(
                onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }, text = if (viewModel.selectedImageUri != null) "Change Image" else "Select Image"
            )

            viewModel.selectedImageUri?.let { uri ->
                Text("Image selected: ${uri.lastPathSegment}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit button
            AmbrosianaButton(
                onClick = { viewModel.submitForm() },
                enabled = !isLoading,
                text = if (isLoading) "Creating..." else "Create Book"
            )

            // Error message
            if (submissionState is NewBookViewModel.SubmissionState.Error) {
                Text(
                    text = (submissionState as NewBookViewModel.SubmissionState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}