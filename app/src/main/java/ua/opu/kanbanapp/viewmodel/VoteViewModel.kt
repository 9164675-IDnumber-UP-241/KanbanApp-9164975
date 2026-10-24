package ua.opu.kanbanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ua.opu.kanbanapp.model.VoteOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import java.util.UUID

class VoteViewModel : ViewModel() {

    private val _options =
        MutableStateFlow<List<VoteOption>>(emptyList())

    val options: StateFlow<List<VoteOption>> =
        _options.asStateFlow()

    val leader: StateFlow<VoteOption?> =
        _options.map { list ->
            list.maxByOrNull { it.votes }
                ?.takeIf { it.votes > 0 }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun addOption(title: String) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.isBlank()) return

        val newOption = VoteOption(
            id = UUID.randomUUID().toString(),
            title = cleanedTitle
        )

        _options.value =
            (_options.value + newOption)
                .sortedByDescending { it.votes }
    }

    fun vote(id: String) {
        _options.value = _options.value
            .map { option ->
                if (option.id == id) {
                    option.copy(votes = option.votes + 1)
                } else {
                    option
                }
            }
            .sortedByDescending { it.votes }
    }
}