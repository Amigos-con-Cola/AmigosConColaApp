package io.github.amigosconcola.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.amigosconcola.R
import io.github.amigosconcola.domain.model.Animal
import io.github.amigosconcola.ui.composables.AppBar
import io.github.amigosconcola.ui.viewmodel.HomeUiEvent
import io.github.amigosconcola.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by homeViewModel.state.collectAsState()

    val onMenuClicked: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AppBar(onMenuClicked = onMenuClicked) },
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .padding(innerPaddingValues)
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {
            Text(
                text = "Animalitos",
                fontSize = 24.sp,
            )
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            AnimalList(
                state.animals,
                state.endReached,
                state.isLoadingItems,
                { homeViewModel.onEvent(HomeUiEvent.LoadNextPage) },
            )
        }
    }
}

@Composable
fun AnimalList(
    animals: List<Animal>,
    endReached: Boolean,
    isLoading: Boolean,
    onLoadNextItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LaunchedEffect(
        scrollState,
        isLoading,
        endReached,
        animals.size
    ) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.collect {
            if (it != null && it >= animals.size - 1 && !endReached && !isLoading) {
                onLoadNextItems()
            }
        }
    }

    LazyColumn(
        state = scrollState,
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(animals.size) {
            AnimalCard(animals[it])
        }
        item {
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun AnimalCard(
    animal: Animal,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(animal.imagen)
                    .crossfade(true)
                    .build(),
                fallback = if (animal.especie == "Cat")
                    painterResource(id = R.drawable.default_cat)
                else
                    painterResource(id = R.drawable.default_dog),
                contentDescription = animal.nombre,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.height(200.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xffeff1f2))
                    .padding(16.dp)
            ) {
                Text(
                    text = animal.nombre,
                    fontSize = 24.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${animal.edad} años"
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    VerticalDivider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = modifier.height(16.dp)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = if (animal.adoptado) "Adoptado" else "No adoptado"
                    )
                }
            }
        }
    }
}