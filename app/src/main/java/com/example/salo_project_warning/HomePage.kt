package com.example.salo_project_warning

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class) // Для разрешений Permission

@Composable
fun HomePage(
    onNavigateToRememberPage: () -> Unit,
    wordViewModel: WordViewModel = viewModel()

) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri // Когда изоброжение выбрано, обновляем состояние картинки
        }
    )

    val permissionRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        //На android 13 (API 33) и выше используем это разрешение
        Manifest.permission.READ_MEDIA_IMAGES

    } else {
        // На старых версиях (ниже API33) используем это разрешение
        Manifest.permission.READ_EXTERNAL_STORAGE

    }

    val permissionState = rememberPermissionState(
        permission = permissionRequest


    )

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .background(Color(0xFF1EB803), shape = RoundedCornerShape(20.dp))
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(Modifier.height(40.dp))
            Text("Enter a word to remember", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))


            //Окно выбора изображений

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDAE5CC))
                    .border(2.dp, Color(0xFF609362), CircleShape)
                    .clickable {
                        //Проверяем разрешение перед запуском галереи:
                        if (permissionState.status.isGranted) {
                            galleryLauncher.launch("image/*") // Открываем галлерею телефона

                        } else {
                            permissionState.launchPermissionRequest()// Иначе запрашиваем разрешение
                        }

                    },
                contentAlignment = Alignment.Center

            ) {
                if (imageUri != null) {
                    //Если изоброжение выбрано, отображаем его с помощью Coil:
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected image",
                        modifier = Modifier.fillMaxSize()
                    )

                } else {
                    Text(
                        text = "Tap to add\nan image",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF609362),
                        fontWeight = FontWeight.Bold

                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = wordViewModel.textFieldState.value,
                onValueChange = { wordViewModel.onTextFieldChange(it) },
                label = { Text("Write here...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(

                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFB8C4CC),
                    focusedBorderColor = Color(0xFF030F40),
                    unfocusedBorderColor = Color(0xFF283888),
                    cursorColor = Color(0xFF063ADA)
                )
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val wordToSave = wordViewModel.textFieldState.value
                    wordViewModel.saveModel(context, wordToSave, imageUri.toString())
                    // Очистим после сохранния:
                    imageUri = null
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF609362),
                    contentColor = Color.White
                )

            ) {
                Text("Save")
            }


            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onNavigateToRememberPage,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0x6BEDF1ED)),
                border = BorderStroke(2.dp, Color(0xFF08888D))


            ){
                Text("Show All remembered models")
            }

        }

    }

}