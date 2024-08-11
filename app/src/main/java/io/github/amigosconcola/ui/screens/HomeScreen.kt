package io.github.amigosconcola.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.amigosconcola.domain.model.Animal
import io.github.amigosconcola.ui.composables.AnimalAvatar
import io.github.amigosconcola.ui.composables.AnimalGender
import io.github.amigosconcola.ui.composables.AppBar
import io.github.amigosconcola.ui.viewmodel.HomeUiEvent
import io.github.amigosconcola.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val state by homeViewModel.state.collectAsState()
    val searchText by homeViewModel.searchText.collectAsState()

    val onMenuClicked: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Animal>()
    val scrollState = rememberScrollState()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Scaffold(
        modifier = modifier,
        topBar = { AppBar(onMenuClicked = onMenuClicked) },
    ) { innerPaddingValues ->
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane(
                    modifier = Modifier.padding(innerPaddingValues)
                ) {
                    AnimalListPane(
                        animals = state.animals,
                        endReached = state.endReached,
                        isLoadingItems = state.isLoadingItems,
                        searchText = searchText,
                        onSearchChanged = { homeViewModel.onEvent(HomeUiEvent.SearchTextChanged(it)) },
                        onLoadNextItems = { homeViewModel.onEvent(HomeUiEvent.LoadNextPage) },
                        onAnimalClicked = {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                it
                            )
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane(
                    modifier = Modifier.padding(innerPaddingValues)
                ) {
                    navigator.currentDestination?.content?.let {
                        AnimalDetails(
                            animal = it,
                            modifier = Modifier.verticalScroll(scrollState)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun AnimalDetails(
    animal: Animal,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(10.dp),
    ) {
        AnimalAvatar(
            url = animal.imagen,
            contentDescription = animal.nombre,
            species = animal.especie,
            modifier = Modifier
                .height(300.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = animal.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                AnimalGender(gender = animal.genero)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (animal.adoptado) "Adoptado" else "No adoptado",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${animal.edad} Años",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "${animal.peso} KG",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                    if (animal.codigo != null) {
                        Text(
                            text = animal.codigo,
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "ubicacion"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = animal.ubicacion,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = animal.historia ?: "",
            )
        }
    }
}

@Composable
fun AnimalListPane(
    animals: List<Animal>,
    endReached: Boolean,
    isLoadingItems: Boolean,
    searchText: String,
    onSearchChanged: (String) -> Unit,
    onLoadNextItems: () -> Unit,
    onAnimalClicked: (Animal) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Busca un animalito") },
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            }
        )
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        AnimalList(
            animals,
            endReached,
            isLoadingItems,
            onLoadNextItems,
            onAnimalClicked,
        )
    }
}

@Composable
fun AnimalList(
    animals: List<Animal>,
    endReached: Boolean,
    isLoading: Boolean,
    onLoadNextItems: () -> Unit,
    onAnimalClicked: (Animal) -> Unit,
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
        if (animals.isEmpty() && !isLoading) {
            item {
                Text(
                    text = "No se encontraron animalitos"
                )
            }
        }

        items(animals.size) {
            AnimalCard(
                animals[it],
                onAnimalClicked,
            )
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
    onAnimalClicked: (Animal) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = { onAnimalClicked(animal) },
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimalAvatar(
                url = animal.imagen,
                contentDescription = animal.nombre,
                species = animal.especie
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xffeff1f2))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = animal.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AnimalGender(gender = animal.genero)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${animal.edad} años"
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    VerticalDivider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = modifier.height(16.dp)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = if (animal.adoptado) "Adoptado" else "No adoptado",
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}