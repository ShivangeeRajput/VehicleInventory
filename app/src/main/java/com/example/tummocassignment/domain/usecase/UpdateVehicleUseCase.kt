package com.example.tummocassignment.domain.usecase

import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(private val repo: VehicleRepository) {
    suspend operator fun invoke(vehicle: Vehicle) = repo.updateVehicle(vehicle)
}
