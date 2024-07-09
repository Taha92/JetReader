package com.example.jetareader.screen.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.component.ReaderAppBar
import com.example.jetareader.model.Item
import com.example.jetareader.model.MBook
import com.example.jetareader.navigation.ReaderScreens
import com.example.jetareader.screen.home.HomeScreenViewModel
import com.example.jetareader.screen.search.BookRow
import com.example.jetareader.util.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun ReaderStatsScreen(navController: NavController,
                      viewModel: HomeScreenViewModel = hiltViewModel()
) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBar(title = "Book Stats",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier
            .padding(top = it.calculateTopPadding()
            )
        ) {
            //only show books by this user that have been read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else {
                emptyList()
            }

            Column {
                Row {
                    Box(modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = "Person icon"
                        )
                    }
                    
                    Text(text = "Hi, ${
                        currentUser?.email.toString()
                            .split("@")[0].uppercase(Locale.getDefault())}")
                }

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser!!.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }

                    val readingBooks = books.filter { mBook ->
                        (mBook.startedReading != null && mBook.finishedReading == null)
                    }

                    Column(modifier = Modifier
                        .padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Stats",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size}")
                        Text(text = "You've read: ${readBooksList.size}")
                    }
                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                } else {
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        // Filter books by finished ones
                        val readBooks: List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                            viewModel.data.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }

                        items(items = readBooks) { book ->
                            BookRowStats(book = book)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookRowStats(book: MBook) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier
            .padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl: String = if (book.photoUrl != null) {
                if (book.photoUrl.toString().isEmpty()) {
                    "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
                } else {
                    book.photoUrl.toString()
                }
            } else {
                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
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
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = book.title.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (book.rating!! >= 4) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumbs up icon",
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                    } else {
                        Box {}
                    }
                }

                Text(text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

                Text(text = "Started: ${formatDate(book.startedReading!!)}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

                Text(text = "Finished: ${formatDate(book.finishedReading!!)}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}