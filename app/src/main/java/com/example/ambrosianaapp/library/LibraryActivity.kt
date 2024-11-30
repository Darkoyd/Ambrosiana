package com.example.ambrosianaapp.library

import AmbrosianaBottomNavigation
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ambrosianaapp.MainActivity
import com.example.ambrosianaapp.components.AmbrosianaToast
import com.example.ambrosianaapp.components.BookThumbnail
import com.example.ambrosianaapp.components.NavigationUtils
import com.example.ambrosianaapp.components.rememberToastState
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor
import com.example.ambrosianaapp.ui.theme.AppFont
import kotlinx.coroutines.launch


data class Book(
    val title: String, val author: String, val category: String
)

class LibraryActivity : ComponentActivity() {
    private val viewModel: LibraryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToMainActivity.collect { navigate ->
                    if (navigate) {
                        redirectToMain()
                        viewModel.onNavHandled()
                    }
                }
            }
        }
        setContent {
            AmbrosianaAppTheme {
                LibraryView(viewModel = viewModel, onBookClick = { book ->
                    // We'll implement navigation to book details later
                }, isExpanded = false, onSignOutClick = {
                    viewModel.signOut()
                })
            }
        }
    }

    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

@Composable
fun LibraryView(
    viewModel: LibraryViewModel,
    onBookClick: (BookUiModel) -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onSignOutClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val toastState = rememberToastState()

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

        FloatingActionButton(
            onClick = onSignOutClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 76.dp), // 76.dp to position above bottom nav
            containerColor = AmbrosianaColor.Green,
            contentColor = AmbrosianaColor.White
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign Out"
            )
        }

        AmbrosianaBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            isExpanded = isExpanded,
            onSearchClick = {
                NavigationUtils.navigateToScreen(
                    context,
                    NavigationUtils.Screen.LIBRARY,
                    NavigationUtils.Screen.SEARCH
                )
            },
            onPostClick = { toastState.show("Posts feature coming soon!") },
            onLibraryClick = { /* Nothing */ },
            onNotificationsClick = { toastState.show("Notifications feature coming soon!") })
    }

    AmbrosianaToast(
        message = toastState.message,
        isVisible = toastState.isVisible,
        onDismiss = { toastState.hide() })
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
            start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = books, key = { it.id }) { book ->
            BookCard(
                book = book, onClick = { onBookClick(book) })
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
            }, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {}, colors = ButtonDefaults.buttonColors(
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
            text = "Add books from the search screen!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BookCard(
    book: BookUiModel, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp), colors = CardDefaults.cardColors(
            containerColor = AmbrosianaColor.Secondary,
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp, pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Title
            Text(
                text = book.title,
                style = AppFont.typography.titleMedium,
                color = AmbrosianaColor.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(8.dp)
            )

            // Thumbnail
            BookThumbnail(
                thumbnailKey = book.thumbnail, modifier = Modifier.fillMaxWidth()
            )

            // Author
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(8.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = AmbrosianaColor.Green,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = book.author.name,
                    style = AppFont.typography.titleSmall,
                    color = AmbrosianaColor.Green,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // ISBN
            Text(
                text = "ISBN:" + book.isbn,
                style = AppFont.typography.bodySmall,
                color = AmbrosianaColor.Black.copy(alpha = 0.6f),
                maxLines = 1
            )

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
                        containerColor = AmbrosianaColor.Green, contentColor = AmbrosianaColor.White
                    ),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "View details",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCardSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
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
        thumbnail = "TODO()",
        isListed = true,
    ), BookUiModel(
        id = "2",
        title = "1984",
        author = AuthorUiModel("2", "George Orwell"),
        isbn = "978-0451524935",
        isListed = true,
        thumbnail = "TODO()"
    ), BookUiModel(
        id = "3",
        title = "Pride and Prejudice",
        author = AuthorUiModel("3", "Jane Austen"),
        isbn = "978-0141439518",
        isListed = false,
        thumbnail = "TODO()"
    ), BookUiModel(
        id = "4",
        title = "The Hobbit",
        author = AuthorUiModel("4", "J.R.R. Tolkien"),
        isbn = "978-0547928227",
        isListed = true,
        thumbnail = "TODO()"
    )
)


@Preview(name = "Loading State")
@Composable
private fun LibraryViewLoadingPreview() {
    AmbrosianaAppTheme {
        LibraryView(
            viewModel = LibraryViewModel().apply {
            _uiState.value = LibraryUiState.Loading
        }, onBookClick = {}, isExpanded = false
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
                books = previewBooks, isLoadingMore = false, canLoadMore = true
            )
        }, onBookClick = {}, isExpanded = false
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
        }, onBookClick = {}, isExpanded = false
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
        }, onBookClick = {}, isExpanded = false
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
                books = previewBooks, isLoadingMore = false, canLoadMore = true
            )
        }, onBookClick = {}, isExpanded = false
        )
    }
}