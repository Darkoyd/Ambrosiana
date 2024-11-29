package com.example.ambrosianaapp.search

import androidx.compose.runtime.Immutable
import com.example.ambrosianaapp.library.AuthorUiModel

@Immutable
data class SearchBookUiModel(
    val id: String,
    val title: String,
    val author: AuthorUiModel,
    val isbn: String,
    val thumbnail: String?,
    val lowestPrice: Double? = null,
    val isAvailable: Boolean,
    val hasListing: Boolean,

    )

sealed class SearchUiState {
    data object Initial : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(
        val books: List<SearchBookUiModel>,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true,
        val query: String = ""
    ) : SearchUiState()
    sealed class Error : SearchUiState() {
        data class Network(val retry: () -> Unit) : Error()
        data class Generic(val message: String, val retry: () -> Unit) : Error()
    }
    data object Empty : SearchUiState()
}