package com.example.jetareader.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetareader.component.ReaderAppBar
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens

@Composable
fun BookDetailsScreen(navController: NavController,
                      bookId: String,
                      viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController) {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        Surface(modifier = Modifier
            .padding(top = it.calculateTopPadding(), start = 3.dp, end = 3.dp, bottom = 3.dp)
            .fillMaxSize()
        ) {
            Column(modifier = Modifier
                .padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value

                if (bookInfo.data == null) {
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }
                } else {
                    Text(text = "Book: ${bookInfo.data.volumeInfo.title}")
                }
            }
        }
    }
}