package com.example.tummocassignment.domain.usecase

import com.example.tummocassignment.domain.repository.VehicleRepository
import javax.inject.Inject

class DeleteVehicleUseCase @Inject constructor(private val repo: VehicleRepository) {
    suspend operator fun invoke(id: Int) = repo.deleteVehicleById(id)
}
