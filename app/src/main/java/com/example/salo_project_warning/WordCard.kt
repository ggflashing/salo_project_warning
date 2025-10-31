package com.example.salo_project_warning

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.salo_project_warning.models.WordItem

@Composable
fun WordItem(wordItem: WordItem){
    Card (
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3ECB5))

    ){

        Row (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
             verticalAlignment = Alignment.CenterVertically

        ){
            //ОТОбражаем изображения модели WordItem, только если оно есть (image не null
            wordItem.imageUri?.let { uri->
                AsyncImage(
                    model = uri,
                    contentDescription = "Image for ${wordItem.text}",
                    modifier = Modifier
                        .size(80.dp)//Задаем фиксированный размер размер для картинки ширину и длинну
                        .clip(RoundedCornerShape(8.dp)), // углы закругл
                    contentScale = ContentScale.Crop // Изображение заполняет все
                    // пространство данное элементом AsyncImage
                )

                Spacer(Modifier.width(16.dp)) // Отступ между картинкой и текстом

            }
            // Отображаем текст модели WordItem
            Text(
                text = wordItem.text,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                modifier = Modifier.weight(1F) // Занимает оставшееся место внутри Row
            )

        }

    }


}