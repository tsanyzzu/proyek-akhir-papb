// File: app/src/main/java/com/kelompok4/serena/ui/viewmodel/CounselingScheduleViewModel.kt
package com.kelompok4.serena.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

// Data class untuk menampung state UI
data class CounselingScheduleUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val availableTimes: List<String> = emptyList(),
    val selectedTime: String? = null
)

class CounselingScheduleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CounselingScheduleUiState())
    val uiState: StateFlow<CounselingScheduleUiState> = _uiState.asStateFlow()

    init {
        fetchAvailableTimesForDate(LocalDate.now())
    }
    fun onDateSelected(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date,
                selectedTime = null // Reset waktu yang dipilih saat tanggal berubah
            )
        }
        fetchAvailableTimesForDate(date)
    }

    fun onTimeSelected(time: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedTime = time)
        }
    }

    private fun fetchAvailableTimesForDate(date: LocalDate) {
        // data dummy
        val dummyTimes = listOf("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00")
        _uiState.update { currentState ->
            currentState.copy(availableTimes = dummyTimes)
        }
    }
}