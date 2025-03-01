package com.example.istu

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.istu.ui.theme.IstuTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageSlider()
                }
            }
        }
    }
}

data class ImageInfo(
    val imageResId: Int, // Идентификатор ресурса изображения
    val title: String,   // Название
    val author: String,  // Автор
    val year: Int       // Год публикации
)

@Composable
fun ImageSlider() {
    val images = listOf(
        ImageInfo(
            imageResId = R.drawable.image1,
            title = "Арабская ночь",
            author = "Ван Гог",
            year = 1889
        ),
        ImageInfo(
            imageResId = R.drawable.image2,
            title = "Оранжевый закат",
            author = "Эндшпиль",
            year = 1503
        ),
        ImageInfo(
            imageResId = R.drawable.image3,
            title = "Летний вечер под кофе",
            author = "Mav-d",
            year = 1893
        )
    )

    // Текущий индекс изображения
    // var currentIndex by remember { mutableStateOf(0) }

    // Текущий индекс изображения (сохраняется при повороте экрана)
    var currentIndex by rememberSaveable { mutableStateOf(0) }

    // Получаем текущую конфигурацию экрана
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Отображение текущего изображения
        Image(
            painter = painterResource(id = images[currentIndex].imageResId),
            contentDescription = "Slider Image",
            modifier = Modifier
                .fillMaxWidth()
                //.height(300.dp),
                // Разные размеры для портрета и ландшафта
                .height(if (isLandscape) 200.dp else 300.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = if (index == currentIndex) Color.Blue else Color.Gray,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение информации об изображении
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = images[currentIndex].title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Автор: ${images[currentIndex].author}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Год: ${images[currentIndex].year}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки навигации
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Кнопка "Назад"
            Button(onClick = {
                currentIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
            }) {
                Text("Назад")
            }

            // Кнопка "Вперёд"
            Button(onClick = {
                currentIndex = if (currentIndex < images.size - 1) currentIndex + 1 else 0
            }) {
                Text("Вперёд")
            }
        }
    }
}