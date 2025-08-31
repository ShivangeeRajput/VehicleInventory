package com.example.tummocassignment.domain.usecase

import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import javax.inject.Inject

class AddVehicleUseCase @Inject constructor(private val repo: VehicleRepository) {
    suspend operator fun invoke(vehicle: Vehicle): Long = repo.addVehicle(vehicle)
}
