package com.example.ambrosianaapp.book.newbook

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.datastore.generated.model.Author
import com.amplifyframework.datastore.generated.model.Book
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StoragePath
import com.amplifyframework.storage.result.StorageUploadFileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class NewBookViewModel(application: Application) : AndroidViewModel(application) {
    // Form fields
    var title by mutableStateOf("")
    var author by mutableStateOf("")
    var isbn by mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)

    // Validation state
    var titleError by mutableStateOf<String?>(null)
    var authorError by mutableStateOf<String?>(null)
    var isbnError by mutableStateOf<String?>(null)

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Form submission state
    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState

    private fun validateForm(): Boolean {
        var isValid = true

        if (title.isBlank()) {
            titleError = "Title is required"
            isValid = false
        } else {
            titleError = null
        }

        if (author.isBlank()) {
            authorError = "Author is required"
            isValid = false
        } else {
            authorError = null
        }

        if (isbn.isBlank()) {
            isbnError = "ISBN is required"
            isValid = false
        } else {
            isbnError = null
        }

        return isValid
    }

    fun onTitleChange(newValue: String) {
        title = newValue
        if (titleError != null) validateForm()
    }

    fun onAuthorChange(newValue: String) {
        author = newValue
        if (authorError != null) validateForm()
    }

    fun onIsbnChange(newValue: String) {
        isbn = newValue
        if (isbnError != null) validateForm()
    }

    fun onImageSelected(uri: Uri) {
        selectedImageUri = uri
    }

    fun submitForm() {
        if (!validateForm()) return

        viewModelScope.launch {
            _isLoading.value = true
            _submissionState.value = SubmissionState.Submitting

            try {
                // First try to find or create the author
                val authorEntity = try {
                    checkAuthorByName(author) ?: run {
                        val newAuthor = Author.builder().name(author).build()
                        Amplify.API.mutate(ModelMutation.create(newAuthor)).data.also {
                            Log.d("NewBookViewModel", "Created new author: ${it.name}")
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Failed to process author: ${e.message}")
                }

                // Handle image upload if present
                val imageKey = selectedImageUri?.let { uri ->
                    try {
                        "${UUID.randomUUID()}.jpg".also { key ->
                            uploadImage(key, uri)
                            Log.d("NewBookViewModel", "Uploaded image with key: $key")
                        }
                    } catch (e: Exception) {
                        Log.w("NewBookViewModel", "Failed to upload image, continuing without image", e)
                        null
                    }
                }

                // Create the book
                val book = Book.builder()
                    .title(title)
                    .isbn(isbn)
                    .author(authorEntity)
                    .apply {
                        imageKey?.let { thumbnail(it) }
                    }
                    .build()

                Amplify.API.mutate(ModelMutation.create(book))
                Log.d("NewBookViewModel", "Successfully created book: ${book.title}")

                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                Log.e("NewBookViewModel", "Failed to create book", e)
                _submissionState.value = SubmissionState.Error(e.message ?: "Failed to create book")
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private suspend fun uploadImage(key: String, uri: Uri): StorageUploadFileResult {
        return try {
            // Get the ContentResolver
            val contentResolver = getApplication<Application>().contentResolver

            // Create a temporary file to store the image
            val tempFile = withContext(Dispatchers.IO) {
                File.createTempFile("upload", ".jpg")
            }.apply {
                deleteOnExit() // Clean up after we're done
            }

            // Copy the content from the URI to our temporary file
            contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: throw Exception("Failed to read image content")

            // Upload the temporary file
            val upload = Amplify.Storage.uploadFile(
                StoragePath.fromString("images/$key"),
                tempFile
            )

            // Wait for the result
            val result = upload.result()

            // Clean up
            tempFile.delete()

            result
        } catch (e: Exception) {
            Log.e("NewBookViewModel", "Upload failed", e)
            throw Exception("Failed to upload image: ${e.message}")
        }
    }

    private suspend fun checkAuthorByName(name: String): Author? {
        try {
            val response = Amplify.API.query(
                ModelQuery.list(Author::class.java, Author.NAME.eq(name))
            )

            // Safely get the first author if it exists
            return response.data.items.firstOrNull()?.let { author ->
                // Log successful author lookup
                Log.d("NewBookViewModel", "Found existing author: ${author.name}")
                author
            }
        } catch (error: ApiException) {
            Log.e("NewBookViewModel", "Could not check authors", error)
            throw Exception("Failed to check author: ${error.message}")
        }
    }

    sealed class SubmissionState {
        object Idle : SubmissionState()
        object Submitting : SubmissionState()
        object Success : SubmissionState()
        data class Error(val message: String) : SubmissionState()
    }
}