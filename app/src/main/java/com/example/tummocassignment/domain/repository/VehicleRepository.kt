package com.example.tummocassignment.domain.repository

import com.example.tummocassignment.data.local.dao.VehicleDao
import com.example.tummocassignment.data.local.entity.VehicleEntity
import com.example.tummocassignment.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun getAllVehicles(): Flow<List<Vehicle>>
    suspend fun addVehicle(vehicle: Vehicle): Long
    suspend fun updateVehicle(vehicle: Vehicle)
    suspend fun deleteVehicleById(id: Int)
}
