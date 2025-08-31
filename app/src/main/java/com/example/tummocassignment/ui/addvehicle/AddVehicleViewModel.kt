package com.example.tummocassignment.ui.addvehicle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.usecase.AddVehicleUseCase
import com.example.tummocassignment.domain.usecase.DeleteVehicleUseCase
import com.example.tummocassignment.domain.usecase.GetVehiclesUseCase
import com.example.tummocassignment.domain.usecase.UpdateVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVehicleViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesUseCase,
    private val addVehicleUseCase: AddVehicleUseCase,
    private val updateVehicleUseCase: UpdateVehicleUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase
) : ViewModel() {

    val vehicles: LiveData<List<Vehicle>> = getVehiclesUseCase().asLiveData()

    private val _addSuccess = MutableLiveData<Long>()
    val addSuccess: LiveData<Long> = _addSuccess

    private val _addError = MutableLiveData<String>()
    val addError: LiveData<String> = _addError

    private val _updateSuccess = MutableLiveData<Unit>()
    val updateSuccess: LiveData<Unit> = _updateSuccess

    private val _updateError = MutableLiveData<String>()
    val updateError: LiveData<String> = _updateError

    private val _deleteSuccess = MutableLiveData<Unit>()
    val deleteSuccess: LiveData<Unit> = _deleteSuccess

    private val _deleteError = MutableLiveData<String>()
    val deleteError: LiveData<String> = _deleteError

    fun addVehicle(vehicle: Vehicle) = viewModelScope.launch {
        try {
            val id = addVehicleUseCase(vehicle)
            _addSuccess.postValue(id)
        } catch (t: Throwable) {
            _addError.postValue(t.localizedMessage ?: "Add failed")
        }
    }

    fun updateVehicle(vehicle: Vehicle) = viewModelScope.launch {
        try {
            updateVehicleUseCase(vehicle)
            _updateSuccess.postValue(Unit)
        } catch (t: Throwable) {
            _updateError.postValue(t.localizedMessage ?: "Update failed")
        }
    }

    fun deleteVehicle(id: Int) = viewModelScope.launch {
        try {
            deleteVehicleUseCase(id)
            _deleteSuccess.postValue(Unit)
        } catch (t: Throwable) {
            _deleteError.postValue(t.localizedMessage ?: "Delete failed")
        }
    }
}
