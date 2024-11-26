package com.example.ambrosianaapp.book.newbook

import android.graphics.ColorSpace.Model
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class NewBookViewModel : ViewModel() {
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
                var authorEntity = checkAuthorByName(author)

                if (authorEntity == null) {
                    val auth = Author.builder().name(author).build()
                    authorEntity = Amplify.API.mutate(ModelMutation.create(auth)).data
                }

                // Upload image if selected
                val key = "${UUID.randomUUID()}.jpg"
                selectedImageUri?.let { uri ->
                    uploadImage(key, uri)
                }




                val book = Book.builder()
                    .title(title)
                    .isbn(isbn)
                    .author(authorEntity)
                    .thumbnail(key)
                    .build()

                Amplify.API.mutate(ModelMutation.create(book))

                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Failed to create book")
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private suspend fun uploadImage(key: String, uri: Uri): StorageUploadFileResult {
        return try {
            val file = uri.toFile()
            val upload = Amplify.Storage.uploadFile(StoragePath.fromString("images/$key"), file)
            val res = upload.result()
            res
        } catch (e: Exception) {
            throw Exception("Failed to upload image: ${e.message}")
        }
    }

    private suspend fun checkAuthorByName(name: String): Author? {

        try {
            val response = Amplify.API.query(ModelQuery.list(Author::class.java, Author.NAME.eq(name)))
            return response.data.items.toList()[0]


        } catch (error: ApiException) {
            Log.e("NewBookViewModel", "Could not check authors: $error")
        }

        return null
    }

    sealed class SubmissionState {
        object Idle : SubmissionState()
        object Submitting : SubmissionState()
        object Success : SubmissionState()
        data class Error(val message: String) : SubmissionState()
    }
}