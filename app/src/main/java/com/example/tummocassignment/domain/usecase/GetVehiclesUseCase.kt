
package com.example.tummocassignment.domain.usecase

import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    operator fun invoke(): Flow<List<Vehicle>> = repository.getAllVehicles()
}