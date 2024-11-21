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
    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
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

                val userId = getCurrentUserId()
                val library = fetchUserLibrary(userId)
                val books = fetchBooksForLibrary(library.id)

                //handleLibraryResult(books)
            } catch (e: Exception) {
                handleError(e)
                Log.e("LibraryViewModel", "Failed to load library", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMore() {
        if (!isLoading && lastBookId != null) {
            loadLibrary()
        }
    }

    private suspend fun getCurrentUserId(): String =
        supervisorScope {
            try {
                Amplify.Auth.getCurrentUser().userId
            } catch (e: Exception) {
                throw IOException("Failed to get current user", e)
            }
        }

    private suspend fun fetchUserLibrary(userId: String): UserLibrary =
        supervisorScope {
            try {
                val response = Amplify.API.query(
                    ModelQuery.list(
                        UserLibrary::class.java,
                        UserLibrary.USER.eq(userId)
                    )
                )

                response.data.firstOrNull()
                    ?: throw IOException("Library not found")
            } catch (e: ApiException) {
                Log.e("LibraryViewModel", "Query failed", e)
                throw IOException("Failed to fetch library", e)
            }
        }

    private suspend fun fetchBooksForLibrary(libraryId: String) =
        supervisorScope {
            try {
                val response = Amplify.API.query(
                    ModelQuery.get<UserLibrary, UserLibraryPath>(
                        UserLibrary::class.java,
                        libraryId
                    ) { libraryPath ->
                        includes(
                            libraryPath.books.book
                        )
                    }
                )
                val books = (response.data.books as? LoadedModelList<BookLibrary>)?.items
                books?.map{
                    Log.println(Log.INFO, "AmbrosianaApp", "Book:" + it.book.toString())
                }



            } catch (e: ApiException) {
                Log.e("LibraryViewModel", "Query failed", e)
                throw IOException("Failed to fetch books", e)
            }
        }

//    private fun handleLibraryResult(books: List<BookLibrary>?) {
//        val newBooks = books?.map { it.book.toUi }
//
//        if (newBooks != null) {
//            if (newBooks.isEmpty() && _uiState.value is LibraryUiState.Loading) {
//                _uiState.value = LibraryUiState.Empty
//                return
//            }
//        }
//
//        val currentBooks = if (_uiState.value is LibraryUiState.Success) {
//            (_uiState.value as LibraryUiState.Success).books
//        } else {
//            emptyList()
//        }
//
//        _uiState.value = LibraryUiState.Success(
//            books = currentBooks + newBooks,
//            isLoadingMore = false,
//            canLoadMore = books.size == pageSize
//        )
//    }

    private fun updateLoadingMore(isLoadingMore: Boolean) {
        if (_uiState.value is LibraryUiState.Success) {
            _uiState.value = (_uiState.value as LibraryUiState.Success).copy(
                isLoadingMore = isLoadingMore
            )
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

//    private fun Book.toUiModel() = BookUiModel(
//        id = id,
//        title = title,
//        author = AuthorUiModel(
//            id = author.id,
//            name = author.name
//        ),
//        isbn = isbn,
//        categories = categories.map { it.category.name },
//        rating = ratings.map { it.rating }.average().takeIf { !it.isNaN() }?.toFloat(),
//        isListed = listings.isNotEmpty()
//    )
}