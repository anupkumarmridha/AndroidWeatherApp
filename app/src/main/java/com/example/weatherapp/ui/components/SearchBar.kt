import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() } // Store search history

    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it }, // Update the search query state
        onSearch = {
            if (searchQuery.isNotBlank()) {
                if (!searchHistory.contains(searchQuery)) {
                    searchHistory.add(0, searchQuery) // Add to history (latest at the top)
                }
                onSearch(searchQuery) // Trigger search when the user submits
            }
            isActive = false
        },
        active = isActive,
        onActiveChange = { isActive = it }, // Handle activation state
        placeholder = { Text("Search for a location...") },
        modifier = modifier
    ) {
        // Display search history as selectable items
        if (searchHistory.isNotEmpty()) {
            Column {
                Text(
                    text = "Recent Searches",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
                searchHistory.forEach { location ->
                    Text(
                        text = location,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchQuery = location // Set selected location
                                onSearch(location) // Trigger search
                                isActive = false
                            }
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

