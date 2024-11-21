import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

data class NavigationItem(
    val title: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit
)

@Composable
fun AmbrosianaBottomNavigation(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onSearchClick: () -> Unit,
    onPostClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val density = LocalDensity.current
    val iconSize = 24.dp

    // Calculate the height based on screen width to maintain square buttons
    val screenWidth = LocalDensity.current.run {
        androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
    }
    val expandedHeight = (screenWidth / 4)
    val collapsedHeight = 56.dp

    val height by animateDpAsState(
        targetValue = if (isExpanded) expandedHeight else collapsedHeight,
        animationSpec = tween(durationMillis = 300),
        label = "height"
    )

    val navigationItems = listOf(
        NavigationItem(
            title = "Search",
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            onClick = onSearchClick
        ),
        NavigationItem(
            title = "Post",
            icon = { Icon(Icons.Filled.Share, contentDescription = "Post") },
            onClick = onPostClick
        ),
        NavigationItem(
            title = "Library",
            icon = { Icon(Icons.Filled.Person, contentDescription = "Library") },
            onClick = onLibraryClick
        ),
        NavigationItem(
            title = "Notifications",
            icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notifications") },
            onClick = onNotificationsClick
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = AmbrosianaColor.Primary,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                NavigationButton(
                    item = item,
                    isExpanded = isExpanded,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavigationButton(
    item: NavigationItem,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = item.onClick,
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AmbrosianaColor.Primary,
            contentColor = AmbrosianaColor.Green
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item.icon()
            if (isExpanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    color = AmbrosianaColor.Green
                )
            }
        }
    }
}