package com.example.istu

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.istu.ui.theme.IstuTheme
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalDensity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IstuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // Состояние вкладки (сохраняется при повороте экрана)
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // Состояние слайдера (сохраняется при повороте экрана)
    var currentIndex by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.getTop(LocalDensity.current).dp) // Отступ сверху
    ) {
        // Вкладки для переключения между слайдером и списком
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Слайдер") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Список") }
            )
        }

        // Отображение выбранного экрана
        when (selectedTab) {
            0 -> ImageSlider(currentIndex = currentIndex, onIndexChange = { currentIndex = it })
            1 -> ImageList()
        }
    }
}

data class ImageInfo(
    val imageResId: Int,
    val title: String,
    val author: String,
    val year: Int
)

@Composable
fun ImageSlider(currentIndex: Int, onIndexChange: (Int) -> Unit) {
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

    // Получаем текущую конфигурацию экрана
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Ландшафтный режим
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Изображение
            Image(
                painter = painterResource(id = images[currentIndex].imageResId),
                contentDescription = "Slider Image",
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Текст и кнопки
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Индикатор текущего изображения
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

                // Информация об изображении
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = images[currentIndex].title,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Автор: ${images[currentIndex].author}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Год: ${images[currentIndex].year}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Кнопки навигации
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Кнопка "Назад"
                    Button(onClick = {
                        val newIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
                        onIndexChange(newIndex)
                    }) {
                        Text("Назад")
                    }

                    // Кнопка "Вперёд"
                    Button(onClick = {
                        val newIndex = if (currentIndex < images.size - 1) currentIndex + 1 else 0
                        onIndexChange(newIndex)
                    }) {
                        Text("Вперёд")
                    }
                }
            }
        }
    } else {
        // Портретный режим
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
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            // Индикатор текущего изображения
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
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Автор: ${images[currentIndex].author}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Год: ${images[currentIndex].year}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
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
                    val newIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
                    onIndexChange(newIndex)
                }) {
                    Text("Назад")
                }

                // Кнопка "Вперёд"
                Button(onClick = {
                    val newIndex = if (currentIndex < images.size - 1) currentIndex + 1 else 0
                    onIndexChange(newIndex)
                }) {
                    Text("Вперёд")
                }
            }
        }
    }
}

@Composable
fun ImageList() {
    // Список данных для блоков
    val imageItems = listOf(
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

    // Получаем текущую конфигурацию экрана
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Количество столбцов в зависимости от ориентации
    val columns = if (isLandscape) 2 else 1

    // Прокручивающийся список
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns), // Количество столбцов
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(imageItems) { item ->
            ImageBlock(imageInfo = item, isLandscape = isLandscape)
        }
    }
}

@Composable
fun ImageBlock(imageInfo: ImageInfo, isLandscape: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Изображение
        Image(
            painter = painterResource(id = imageInfo.imageResId),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLandscape) 150.dp else 200.dp) // Меньшая высота в ландшафтном режиме
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Название
        Text(
            text = imageInfo.title,
            style = if (isLandscape) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )

        // Автор
        Text(
            text = "Автор: ${imageInfo.author}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        // Год
        Text(
            text = "Год: ${imageInfo.year}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}