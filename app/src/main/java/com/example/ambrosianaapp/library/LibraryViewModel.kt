package com.example.ambrosianaapp.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.smithy.kotlin.runtime.io.IOException
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.LoadedModelList
import com.amplifyframework.core.model.includes
import com.amplifyframework.datastore.generated.model.BookLibrary
import com.amplifyframework.datastore.generated.model.UserLibrary
import com.amplifyframework.datastore.generated.model.UserLibraryPath
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LibraryViewModel : ViewModel() {
    val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private var lastBookId: String? = null
    private var isLoading = false
    private val pageSize = 20

    init {
        loadLibrary()
    }

    private fun loadLibrary() {
        if (isLoading) return

        viewModelScope.launch {
            try {
                isLoading = true
                if (lastBookId == null) {
                    _uiState.value = LibraryUiState.Loading
                } else {
                    updateLoadingMore(true)
                }

                val userId = Amplify.Auth.fetchUserAttributes()[0].value
                Log.d("LibraryViewModel", "Fetching library for user: $userId")

                // First, try to get the user's library
                val userLibrary = fetchUserLibrary(userId)


                // Now fetch books for the library
                val books = userLibrary?.let { fetchBooksForLibrary(it.id) }

                // Set initial state if no books
                if (books.isNullOrEmpty()) {
                    _uiState.value = LibraryUiState.Empty
                } else {
                    Log.d("LibraryViewModel", "Fetched books!: $books")

//                    _uiState.value = LibraryUiState.Success(
//                        books = books,
//                        isLoadingMore = false,
//                        canLoadMore = books.size >= pageSize
//                    )
                }

            } catch (e: Exception) {
                handleError(e)
                Log.e("LibraryViewModel", "Failed to load library", e)
            } finally {
                isLoading = false
            }
        }
    }


    private suspend fun fetchUserLibrary(userId: String): UserLibrary? =
        supervisorScope {
            try {
                Log.d("LibraryViewModel", "Querying for library with userId: $userId")
                val response = Amplify.API.query(
                    ModelQuery.list(
                        UserLibrary::class.java,
                        UserLibrary.USER.eq(userId)
                    )
                )

                val library = response.data.firstOrNull()
                if (library != null) {
                    Log.d("LibraryViewModel", "Found library with id: ${library.id}")
                } else {
                    Log.d("LibraryViewModel", "No library found for user")
                }

                library
            } catch (e: ApiException) {
                Log.e("LibraryViewModel", "Query failed", e)
                throw IOException("Failed to fetch library", e)
            }
        }

    private suspend fun fetchBooksForLibrary(libraryId: String): List<BookLibrary> =
        supervisorScope {
            try {
                val response = Amplify.API.query(
                    ModelQuery.list(BookLibrary::class.java, BookLibrary.LIBRARY.eq(libraryId))
                )

                val books = response.data.items.toList()
                Log.d("LibraryViewModel", "Fetched response: $response")
                Log.d("LibraryViewModel", "Fetched ${books.size} books for library")
                books

            } catch (e: ApiException) {
                Log.e("LibraryViewModel", "Query failed", e)
                throw IOException("Failed to fetch books", e)
            }
        }


    fun loadMore() {
        if (!isLoading && lastBookId != null) {
            loadLibrary()
        }
    }

    private fun handleError(error: Exception) {
        val errorState = when (error) {
            is IOException -> LibraryUiState.Error.Network { loadLibrary() }
            else -> LibraryUiState.Error.Generic(
                message = error.message ?: "Unknown error occurred",
                retry = { loadLibrary() }
            )
        }

        if (_uiState.value !is LibraryUiState.Success) {
            _uiState.value = errorState
        }
    }

    private fun updateLoadingMore(isLoadingMore: Boolean) {
        if (_uiState.value is LibraryUiState.Success) {
            _uiState.value = (_uiState.value as LibraryUiState.Success).copy(
                isLoadingMore = isLoadingMore
            )
        }
    }
}