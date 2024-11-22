package com.example.ambrosianaapp.library

import AmbrosianaBottomNavigation
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor
import com.example.ambrosianaapp.ui.theme.AppFont
import androidx.activity.viewModels


data class Book(
    val title: String,
    val author: String,
    val category: String
)

class LibraryActivity : ComponentActivity() {
    private val viewModel: LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {
                LibraryView(
                    viewModel = viewModel,
                    onBookClick = { book ->
                        // We'll implement navigation to book details later
                    },
                    isExpanded = false // You might want to control this state
                )
            }
        }
    }
}

@Composable
fun LibraryView(
    viewModel: LibraryViewModel,
    onBookClick: (BookUiModel) -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is LibraryUiState.Loading -> LoadingState()
            is LibraryUiState.Success -> BookGrid(
                books = state.books,
                isLoadingMore = state.isLoadingMore,
                canLoadMore = state.canLoadMore,
                onLoadMore = viewModel::loadMore,
                onBookClick = onBookClick
            )
            is LibraryUiState.Error -> ErrorState(state)
            LibraryUiState.Empty -> EmptyState()
        }

        AmbrosianaBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            isExpanded = isExpanded,
            onSearchClick = { /* ... */ },
            onPostClick = { /* ... */ },
            onLibraryClick = { /* ... */ },
            onNotificationsClick = { /* ... */ }
        )
    }
}

@Composable
private fun BookGrid(
    books: List<BookUiModel>,
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    onLoadMore: () -> Unit,
    onBookClick: (BookUiModel) -> Unit,
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
            BookCard(
                book = book,
                onClick = { onBookClick(book) }
            )
        }

        if (isLoadingMore) {
            items(2) {
                BookCardSkeleton()
            }
        }

        if (canLoadMore && !isLoadingMore) {
            item(span = { GridItemSpan(2) }) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
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
            BookCardSkeleton()
        }
    }
}

@Composable
private fun ErrorState(error: LibraryUiState.Error) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (error) {
                is LibraryUiState.Error.Network -> "No internet connection"
                is LibraryUiState.Error.Generic -> error.message
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = AmbrosianaColor.Green
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your library is empty",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add books from the search screen or create your own entries",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BookCard(
    book: BookUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ratingText = remember(book.rating) {
        book.rating?.let { "%.1f★".format(it) }
    }

    val categoriesText = remember(book.categories) {
        book.categories.take(2).joinToString(", ") +
                if (book.categories.size > 2) "..." else ""
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmbrosianaColor.Secondary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = book.title,
                style = AppFont.typography.titleMedium,
                color = AmbrosianaColor.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Author
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = AmbrosianaColor.Green,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = book.author.name,
                    style = AppFont.typography.bodyMedium,
                    color = AmbrosianaColor.Green,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Categories and Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Categories
                Text(
                    text = categoriesText,
                    style = AppFont.typography.labelSmall,
                    color = AmbrosianaColor.Black.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // Rating
                if (ratingText != null) {
                    Text(
                        text = ratingText,
                        style = AppFont.typography.labelMedium,
                        color = AmbrosianaColor.Green
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (book.isListed) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Listed for sale",
                        tint = AmbrosianaColor.Green,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmbrosianaColor.Green,
                        contentColor = AmbrosianaColor.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "View details",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCardSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}

private val previewBooks = listOf(
    BookUiModel(
        id = "1",
        title = "The Great Gatsby",
        author = AuthorUiModel("1", "F. Scott Fitzgerald"),
        isbn = "978-0743273565",
        categories = listOf("Fiction", "Classic Literature"),
        rating = 4.5f,
        isListed = true
    ),
    BookUiModel(
        id = "2",
        title = "1984",
        author = AuthorUiModel("2", "George Orwell"),
        isbn = "978-0451524935",
        categories = listOf("Science Fiction", "Dystopian"),
        rating = 4.8f,
        isListed = false
    ),
    BookUiModel(
        id = "3",
        title = "Pride and Prejudice",
        author = AuthorUiModel("3", "Jane Austen"),
        isbn = "978-0141439518",
        categories = listOf("Romance", "Classic Literature"),
        rating = 4.7f,
        isListed = false
    ),
    BookUiModel(
        id = "4",
        title = "The Hobbit",
        author = AuthorUiModel("4", "J.R.R. Tolkien"),
        isbn = "978-0547928227",
        categories = listOf("Fantasy", "Adventure"),
        rating = 4.9f,
        isListed = true
    )
)


@Preview(name = "Loading State")
@Composable
private fun LibraryViewLoadingPreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
                _uiState.value = LibraryUiState.Loading
            },
            onBookClick = {},
            isExpanded = false
        )
    }
}

@Preview(name = "Success State")
@Composable
private fun LibraryViewSuccessPreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
                _uiState.value = LibraryUiState.Success(
                    books = previewBooks,
                    isLoadingMore = false,
                    canLoadMore = true
                )
            },
            onBookClick = {},
            isExpanded = false
        )
    }
}

@Preview(name = "Empty State")
@Composable
private fun LibraryViewEmptyPreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
                _uiState.value = LibraryUiState.Empty
            },
            onBookClick = {},
            isExpanded = false
        )
    }
}

@Preview(name = "Error State")
@Composable
private fun LibraryViewErrorPreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
                _uiState.value = LibraryUiState.Error.Network { }
            },
            onBookClick = {},
            isExpanded = false
        )
    }
}

@Preview(
    name = "Library View - Light Theme",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Library View - Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)


@Composable
private fun LibraryViewThemePreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
                _uiState.value = LibraryUiState.Success(
                    books = previewBooks,
                    isLoadingMore = false,
                    canLoadMore = true
                )
            },
            onBookClick = {},
            isExpanded = false
        )
    }
}