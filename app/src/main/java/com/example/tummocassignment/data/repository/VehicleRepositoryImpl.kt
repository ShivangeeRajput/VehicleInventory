package com.example.tummocassignment.data.repository

import com.example.tummocassignment.data.local.dao.VehicleDao
import com.example.tummocassignment.data.mapper.VehicleMapper
import com.example.tummocassignment.domain.model.Vehicle
import com.example.tummocassignment.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val dao: VehicleDao
) : VehicleRepository {
    override fun getAllVehicles(): Flow<List<Vehicle>> =
        dao.getAll().map { list -> list.map { VehicleMapper.fromEntity(it) } }

    override suspend fun addVehicle(vehicle: Vehicle): Long =
        dao.insert(VehicleMapper.toEntity(vehicle))

    override suspend fun updateVehicle(vehicle: Vehicle) =
        dao.update(VehicleMapper.toEntity(vehicle))

    override suspend fun deleteVehicleById(id: Int) =
        dao.deleteById(id)
}
