package com.example.salo_project_warning

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.accompanist.permissions.ExperimentalPermissionsApi

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun RememberPage(
    // Функция для возврата назад
    onNavigateBack: () -> Unit,
    //вызов экземплера класса WordViewModel
    //для вытягивания данных из локального хранилища DataStore
    wordViewModel: WordViewModel = viewModel()
){
    val context = LocalContext.current //Осмотр текущего состояния страницы

    // Вытягиваем список из локальной DataStore от ViewModel с его текущим состоянием State:
    val words_list by wordViewModel.getWords(context).collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(
            title = {Text("Remembered Words and Images") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

        )

    }) {paddingValues ->
        if (words_list.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ){
                Text("No words and images saved yet.")

            }

        }else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF3EDB1)),
                contentPadding = PaddingValues(16.dp),
                //Добавим немного отступов между карточками:
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(words_list){wordItem ->


                }
            }

        }

    }



}