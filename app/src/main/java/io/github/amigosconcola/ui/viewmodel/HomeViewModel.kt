package io.github.amigosconcola.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.amigosconcola.domain.model.Animal
import io.github.amigosconcola.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeState(
    val isLoadingItems: Boolean = false,
    val currentPage: Int = 0,
    val animals: List<Animal> = emptyList(),
    val endReached: Boolean = false
)

sealed interface HomeUiEvent {
    data object LoadNextPage : HomeUiEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animals: AnimalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.launch {
            loadNextPage()
        }
    }

    fun onEvent(evt: HomeUiEvent) {
        when (evt) {
            is HomeUiEvent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }
        }
    }

    private suspend fun loadNextPage() {
        _state.update { it.copy(isLoadingItems = true) }

        val page = _state.value.currentPage + 1
        val animals = animals.getAnimals(page)

        _state.update {
            it.copy(
                isLoadingItems = false,
                animals = it.animals + animals,
                currentPage = page,
                endReached = animals.isEmpty()
            )
        }
    }
}