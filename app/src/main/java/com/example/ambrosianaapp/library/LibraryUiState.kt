package com.example.ambrosianaapp.library

import androidx.compose.runtime.Immutable

@Immutable
data class BookUiModel(
    val id: String,
    val title: String,
    val author: AuthorUiModel,
    val isbn: String,
    val isListed: Boolean = false
)

@Immutable
data class AuthorUiModel(
    val id: String,
    val name: String
)

sealed class LibraryUiState {
    data object Loading : LibraryUiState()
    data class Success(
        val books: List<BookUiModel>,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true
    ) : LibraryUiState()
    sealed class Error : LibraryUiState() {
        data class Network(val retry: () -> Unit) : Error()
        data class Generic(val message: String, val retry: () -> Unit) : Error()
    }
    data object Empty : LibraryUiState()
}