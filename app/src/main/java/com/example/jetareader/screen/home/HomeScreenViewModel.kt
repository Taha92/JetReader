package com.example.jetareader.screen.home

import androidx.lifecycle.ViewModel
import com.example.jetareader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository
): ViewModel() {
}