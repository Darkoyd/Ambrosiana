package com.example.ambrosianaapp.library

import AmbrosianaBottomNavigation
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor
import com.example.ambrosianaapp.ui.theme.AppFont

data class Book(
    val title: String,
    val author: String,
    val category: String
)

@Composable
fun LibraryView(
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean
) {
    Box(modifier = modifier.fillMaxSize()) {
        BookGrid(
            books = sampleBooks,
            onBookClick = onBookClick,
            modifier = Modifier.fillMaxSize()
        )

        AmbrosianaBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            isExpanded = isExpanded,
            onSearchClick = {
                //startActivity(Intent(this, SearchActivity::class.java))
            },
            onPostClick = {
                //startActivity(Intent(this, PostActivity::class.java))
            },
            onLibraryClick = { /* Already in Library */ },
            onNotificationsClick = {
                //startActivity(Intent(this, NotificationsActivity::class.java))
            }
        )
    }
}

@Composable
private fun BookGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                onClick = { onBookClick(book) }
            )
        }
    }
}
@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AmbrosianaColor.Secondary,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                style = AppFont.typography.titleMedium,
                color = AmbrosianaColor.Black,
                maxLines = 1,  // Changed from 2 to 1
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.author,
                style = AppFont.typography.bodyMedium,
                color = AmbrosianaColor.Green,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.category,
                style = AppFont.typography.labelSmall,
                color = AmbrosianaColor.Black.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmbrosianaColor.Green,
                    contentColor = AmbrosianaColor.White
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View details",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Sample data for preview
private val sampleBooks = listOf(
    Book("The Great Gatsby", "F. Scott Fitzgerald", "Classic Literature"),
    Book("Dune", "Frank Herbert", "Science Fiction"),
    Book("Pride and Prejudice", "Jane Austen", "Romance"),
    Book("1984", "George Orwell", "Dystopian"),
    Book("The Hobbit", "J.R.R. Tolkien", "Fantasy"),
    Book("Murder on the Orient Express", "Agatha Christie", "Mystery"),
    Book("The Catcher in the Rye", "J.D. Salinger", "Coming of Age"),
    Book("Brave New World", "Aldous Huxley", "Science Fiction"),
    Book("To Kill a Mockingbird", "Harper Lee", "Literary Fiction"),
    Book("The Chronicles of Narnia", "C.S. Lewis", "Fantasy")
)

@Composable
@Preview
fun LibraryScreenPreview() {
    LibraryView(
        onBookClick = {},
        isExpanded = false
    )
}