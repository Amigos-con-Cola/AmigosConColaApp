package io.github.amigosconcola.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.amigosconcola.domain.model.Animal
import io.github.amigosconcola.domain.repository.AnimalRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
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
    data class SearchTextChanged(val text: String) : HomeUiEvent
}

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animalRepository: AnimalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    init {
        viewModelScope.launch { loadNextPage() }
        viewModelScope.launch {
            searchText
                .debounce(500L)
                .collect { loadNextPage(fromStart = true) }
        }
    }

    fun onEvent(evt: HomeUiEvent) {
        when (evt) {
            is HomeUiEvent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            is HomeUiEvent.SearchTextChanged -> onSearchTextChanged(evt.text)
        }
    }

    private fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    private suspend fun loadNextPage(fromStart: Boolean = false) {
        if (fromStart) {
            _state.update {
                it.copy(
                    isLoadingItems = true,
                    animals = emptyList(),
                    currentPage = 0,
                    endReached = false,
                )
            }
        } else {
            _state.update {
                it.copy(isLoadingItems = true)
            }
        }

        val page = _state.value.currentPage + 1
        val name = _searchText.value.ifBlank { null }
        val animals = animalRepository.getAnimals(page, name)

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