package com.example.ambrosianaapp.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.smithy.kotlin.runtime.io.IOException
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.LazyModelList
import com.amplifyframework.core.model.LazyModelReference
import com.amplifyframework.datastore.generated.model.Book
import com.amplifyframework.kotlin.core.Amplify
import com.example.ambrosianaapp.library.AuthorUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class SearchViewModel : ViewModel() {
    companion object {
        private const val TAG = "SearchViewModel"
        private const val PAGE_SIZE = 20
    }

    // UI state management
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // Search state
    private var nextToken: String? = null
    private var isLoading = false
    private var currentSearchQuery: String = ""

    init {
        loadBooks()
    }

    // Public function to initiate a new search
    fun search(query: String) {
        currentSearchQuery = query
        nextToken = null
        loadBooks(true)
    }

    // Public function to load more results
    fun loadMore() {
        if (!isLoading && nextToken != null) {
            loadBooks(false)
        }
    }

    private fun loadBooks(isNewSearch: Boolean = false) {
        if (isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true

                if (isNewSearch) {
                    _uiState.value = SearchUiState.Loading
                } else {
                    updateLoadingMore(true)
                }

                // Fetch books from data source
                val books = fetchBooks()
                Log.d(TAG, "Fetched books: $books")

                // Process and filter books into UI models
                val uibooks = books.mapNotNull { book ->
                    try {
                        val auth = (book.author as? LazyModelReference)?.fetchModel()

                        val listings = (book.listings as? LazyModelList)?.fetchPage()?.items?.toList()

                        val available = listings?.filter { it.status.name == "available" }


                        val lowest = available?.minOfOrNull { it.price }

                        val authUi = auth?.let {
                            AuthorUiModel(
                                id = it.id,
                                name = it.name
                            )
                        }


                        val bui = authUi?.let {
                            available?.let { it1 ->
                                SearchBookUiModel(
                                    id = book.id,
                                    title = book.title,
                                    author = it,
                                    isbn = book.isbn,
                                    thumbnail = book.thumbnail,
                                    lowestPrice = lowest,
                                    isAvailable = available.isNotEmpty(),
                                    hasListing = listings.isNotEmpty(),
                                )
                            }
                        }
                        bui
                    } catch (e: Exception) {
                        null
                    }
                }


                _uiState.value = SearchUiState.Success(uibooks)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load books", e)

            } finally {
                isLoading = false
                updateLoadingMore(false)
            }
        }
    }



    private suspend fun fetchBooks(): List<Book> = supervisorScope {
        try {
            Log.d(TAG, "Fetching books with query: $currentSearchQuery")

            val response = Amplify.API.query(
                ModelQuery.list(
                    Book::class.java,
                    Book.ISBN.contains(currentSearchQuery).or(Book.AUTHOR.contains(currentSearchQuery).or(Book.TITLE.contains(currentSearchQuery)))
                )
            )

            Log.d(TAG, "Fetching books: ${response.data.items.toList()}" )


            response.data.items.toList()

        } catch (e: ApiException) {
            Log.e(TAG, "API error while fetching books", e)
            throw IOException("Failed to fetch books", e)
        }
    }

    private fun handleError(error: Exception) {
        val errorState = when (error) {
            is IOException -> SearchUiState.Error.Network { loadBooks(true) }
            else -> SearchUiState.Error.Generic(
                message = error.message ?: "Unknown error occurred",
                retry = { loadBooks(true) }
            )
        }

        if (_uiState.value !is SearchUiState.Success) {
            _uiState.value = errorState
        }
    }

    private fun updateLoadingMore(isLoadingMore: Boolean) {
        if (_uiState.value is SearchUiState.Success) {
            _uiState.value = (_uiState.value as SearchUiState.Success).copy(
                isLoadingMore = isLoadingMore
            )
        }
    }

    fun resetSearch() {
        currentSearchQuery = ""
        nextToken = null
        loadBooks(true)
    }
}