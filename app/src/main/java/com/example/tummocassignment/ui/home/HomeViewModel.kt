package com.example.tummocassignment.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    val vehicles: StateFlow<List<Vehicle>> =
        repository.getAllVehicles()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun deleteVehicle(id: Int) {
        viewModelScope.launch {
            repository.deleteVehicleById(id)
        }
    }
}
