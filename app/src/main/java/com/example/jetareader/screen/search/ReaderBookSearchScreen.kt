package com.example.jetareader.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.component.InputField
import com.example.jetareader.component.ReaderAppBar
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens

@Composable
fun SearchScreen(navController: NavController,
                 viewModel: BooksSearchViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false
        ) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) {
        Surface(modifier = Modifier
            .padding(top = it.calculateTopPadding())
        ) {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { searchQuery ->
                    viewModel.searchBooks(query = searchQuery)
                }

                Spacer(modifier = Modifier.height(13.dp))
                
                BookList(navController, viewModel)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController,
             viewModel: BooksSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list
    /*val listOfBooks = listOf(
        MBook(id = "afhjdsf", title = "Hello Again1!", authors = "All of us", notes = null),
        MBook(id = "afhjdsf", title = "Hello Again2!", authors = "All of us", notes = null),
        MBook(id = "afhjdsf", title = "Hello Again3!", authors = "All of us", notes = null),
        MBook(id = "afhjdsf", title = "Hello Again4!", authors = "All of us", notes = null),
        MBook(id = "afhjdsf", title = "Hello Again5!", authors = "All of us", notes = null)
    )*/

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = listOfBooks) {book ->
            BookRow(book, navController)
        }
    }
}

@Composable
fun BookRow(book: Item,
            navController: NavController) {
    Card(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .height(100.dp)
        .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier
            .padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl: String = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty())
                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"            else {
                book.volumeInfo.imageLinks.smallThumbnail
            }
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Book image",
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
                contentScale = ContentScale.FillBounds
            )
            
            Column {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium)
                //TODO: Add more fields later
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = hint,
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                    onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}