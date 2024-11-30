package com.example.ambrosianaapp.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.smithy.kotlin.runtime.io.IOException
import com.amplifyframework.annotations.InternalAmplifyApi
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.LoadedModelList
import com.amplifyframework.core.model.LoadedModelReference
import com.amplifyframework.core.model.includes
import com.amplifyframework.datastore.generated.model.BookLibrary
import com.amplifyframework.datastore.generated.model.UserLibrary
import com.amplifyframework.datastore.generated.model.UserLibraryPath
import com.amplifyframework.kotlin.core.Amplify
import com.example.ambrosianaapp.analytics.AmbrosianaAnalytics
import com.example.ambrosianaapp.auth.AmplifyAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LibraryViewModel(
    private val authManager: AmplifyAuthManager = AmplifyAuthManager()
) : ViewModel() {
    companion object {
        private const val TAG = "LibraryViewModel"
    }

    val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    val _navigateToMainActivity = MutableStateFlow(false)
    val navigateToMainActivity: StateFlow<Boolean> get() = _navigateToMainActivity

    private var lastBookId: String? = null
    private var isLoading = false
    private val pageSize = 20

    init {
        loadLibrary()
    }

    @OptIn(InternalAmplifyApi::class)
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
                Log.d(TAG, "Fetching library for user: $userId")

                // First, try to get the user's library
                val userLibrary = fetchUserLibrary(userId)
                if (userLibrary == null) {
                    Log.e(TAG, "User library not found for userId: $userId")
                    _uiState.value = LibraryUiState.Empty
                    return@launch
                }

                // Now fetch books for the library
                val books = fetchBooksForLibrary(userLibrary.id)
                if (books.isNullOrEmpty()) {
                    Log.d(TAG, "No books found in library")
                    _uiState.value = LibraryUiState.Empty
                } else {
                    Log.d(TAG, "Fetched ${books.size} books")
                    val bookUiModels = books.mapNotNull { bookLibrary ->
                        bookLibrary.book?.let { book ->
                            val bookx = (book as LoadedModelReference).value
                            val authorx = (bookx?.author as LoadedModelReference).value
                            bookx.let {
                                authorx?.let { it1 ->
                                    AuthorUiModel(
                                        id = it1.id, name = authorx.name
                                    )
                                }?.let { it2 ->
                                    BookUiModel(
                                        id = it.id,
                                        title = it.title,
                                        author = it2,
                                        isbn = it.isbn,
                                        thumbnail = it.thumbnail,
                                    )
                                }
                            }
                        }
                    }

                    _uiState.value = LibraryUiState.Success(
                        books = bookUiModels,
                        isLoadingMore = false,
                        canLoadMore = bookUiModels.size >= pageSize
                    )
                }

            } catch (e: Exception) {
                handleError(e)
                Log.e(TAG, "Failed to load library", e)
            } finally {
                isLoading = false
            }
        }
    }


    private suspend fun fetchUserLibrary(userId: String): UserLibrary? = supervisorScope {
        try {
            Log.d(TAG, "Querying for library with userId: $userId")
            val response = Amplify.API.query(
                ModelQuery.list(
                    UserLibrary::class.java, UserLibrary.USER.eq(userId)
                )
            )

            if (response.data == null) {
                Log.e(TAG, "Query response data is null")
                throw IOException("Failed to fetch library: response data is null")
            }

            val library = response.data.firstOrNull()
            if (library != null) {
                Log.d(TAG, "Found library with id: ${library.id}")
            } else {
                Log.d(TAG, "No library found for user")
            }

            library
        } catch (e: ApiException) {
            Log.e(TAG, "Query failed", e)
            throw IOException("Failed to fetch library", e)
        }
    }

    private suspend fun fetchBooksForLibrary(libraryId: String): List<BookLibrary>? =
        supervisorScope {
            val start = System.currentTimeMillis()
            try {
                val response = Amplify.API.query(
                    ModelQuery.get<UserLibrary, UserLibraryPath>(
                        UserLibrary::class.java, libraryId
                    ) { libraryPath ->
                        includes(
                            libraryPath.books.book.author,
                            libraryPath.books.book.categories.category,
                            libraryPath.books.book.ratings,
                            libraryPath.books.book.listings
                        )
                    })

                if (response.data == null) {
                    throw IOException("Failed to fetch books: response data is null")
                }

                val books = (response.data.books as? LoadedModelList<BookLibrary>)?.items?.toList()
                Log.d(TAG, "Fetched ${books?.size ?: 0} books for library")

                val duration = System.currentTimeMillis() - start
                AmbrosianaAnalytics.trackApiCall(
                    endpoint = "fetchBooksForLibrary", isSuccess = true, durationMs = duration
                )
                books
            } catch (e: ApiException) {
                Log.e(TAG, "Query failed", e)

                val duration = System.currentTimeMillis() - start
                AmbrosianaAnalytics.trackApiCall(
                    endpoint = "fetchBooksForLibrary",
                    isSuccess = false,
                    durationMs = duration,
                    errorType = e.javaClass.simpleName,
                    errorMessage = e.message ?: "Unknown error"
                )
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
                message = error.message ?: "Unknown error occurred", retry = { loadLibrary() })
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

    fun signOut() {
        viewModelScope.launch {
            try {
                authManager.signOut()
                _navigateToMainActivity.value = true
            } catch (e: Exception) {
                Log.e(TAG, "Auth error: ${e.message}")
            }
        }
    }

    fun onNavHandled() {
        _navigateToMainActivity.value = false
    }
}