package com.example.ambrosianaapp.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import AmbrosianaBottomNavigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ambrosianaapp.components.AmbrosianaTextField
import com.example.ambrosianaapp.components.BookThumbnail
import com.example.ambrosianaapp.components.NavigationUtils
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor
import com.example.ambrosianaapp.ui.theme.AppFont

class SearchActivity : ComponentActivity() {
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {

                SearchScreen(
                    viewModel = viewModel,
                    onBookClick = { book ->
                        // TODO: Navigate to book details
                    },
                    isExpanded = false // You might want to make this configurable
                )
            }
        }
    }
}



@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBookClick: (SearchBookUiModel) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AmbrosianaColor.Details)
        ) {
            // Search Header
            SearchHeader(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.search(it)
                },
            )

            // Main Content
            when (val state = uiState) {
                is SearchUiState.Loading -> LoadingState()
                is SearchUiState.Success -> SearchBookGrid(
                    books = state.books,
                    isLoadingMore = state.isLoadingMore,
                    canLoadMore = state.canLoadMore,
                    onLoadMore = viewModel::loadMore,
                    onBookClick = onBookClick
                )
                is SearchUiState.Error -> ErrorState(state)
                SearchUiState.Empty -> EmptyState(searchQuery)
                SearchUiState.Initial -> {}
            }
        }

        // Bottom Navigation
        AmbrosianaBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            isExpanded = isExpanded,
            onSearchClick = { /* Already on search */ },
            onPostClick = { /* TODO */ },
            onLibraryClick = { NavigationUtils.navigateToScreen(context, NavigationUtils.Screen.SEARCH, NavigationUtils.Screen.LIBRARY ) },
            onNotificationsClick = { /* TODO */ }
        )
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmbrosianaColor.Primary)
            .padding(16.dp)
    ) {
        Text(
            text = "Search Books",
            style = MaterialTheme.typography.headlineMedium,
            color = AmbrosianaColor.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AmbrosianaTextField(
            value = query,
            onValueChange = onQueryChange,
            label = "Search by title, author, or ISBN",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = AmbrosianaColor.Green
                )
            }
        )
    }
}

@Composable
private fun SearchBookGrid(
    books: List<SearchBookUiModel>,
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    onLoadMore: () -> Unit,
    onBookClick: (SearchBookUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp,
            bottom = 80.dp // Account for bottom navigation
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = books,
            key = { it.id }
        ) { book ->
            SearchBookCard(
                book = book,
                onClick = { onBookClick(book) }
            )
        }

        if (isLoadingMore) {
            items(2) {
                SearchBookCardSkeleton()
            }
        }

        if (canLoadMore && !isLoadingMore) {
            item {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
        }
    }
}

@Composable
fun SearchBookCard(
    book: SearchBookUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmbrosianaColor.Secondary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = book.title,
                style = AppFont.typography.titleMedium,
                color = AmbrosianaColor.Black,
                maxLines = 2
            )

            // Thumbnail
            BookThumbnail(
                thumbnailKey = book.thumbnail,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Author
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AmbrosianaColor.Green,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = book.author.name,
                    style = AppFont.typography.titleSmall,
                    color = AmbrosianaColor.Green,
                    maxLines = 1
                )
            }

            // ISBN
            Text(
                text = "ISBN: ${book.isbn}",
                style = AppFont.typography.bodySmall,
                color = AmbrosianaColor.Black.copy(alpha = 0.6f)
            )

            // Listing Status
            if (book.isAvailable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Available for purchase",
                            tint = AmbrosianaColor.Green,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Available",
                            style = AppFont.typography.labelMedium,
                            color = AmbrosianaColor.Green
                        )
                    }
                    book.lowestPrice?.let { price ->
                        Text(
                            text = "$${String.format("%.2f", price)}",
                            style = AppFont.typography.titleMedium,
                            color = AmbrosianaColor.Green
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBookCardSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Thumbnail skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Author skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // ISBN skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}

@Composable
private fun LoadingState() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(6) {
            SearchBookCardSkeleton()
        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = AmbrosianaColor.Green,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (searchQuery.isBlank())
                "Search for books to get started"
            else
                "No books found for \"$searchQuery\"",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = AmbrosianaColor.Black
        )
    }
}

@Composable
private fun ErrorState(error: SearchUiState.Error) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (error) {
                is SearchUiState.Error.Network -> "No internet connection"
                is SearchUiState.Error.Generic -> error.message
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                when (error) {
                    is SearchUiState.Error.Network -> error.retry()
                    is SearchUiState.Error.Generic -> error.retry()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = AmbrosianaColor.Green
            )
        ) {
            Text("Retry")
        }
    }
}