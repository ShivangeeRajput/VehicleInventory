package com.example.tummocassignment.data.mapper

import com.example.tummocassignment.data.local.entity.VehicleEntity
import com.example.tummocassignment.domain.model.Vehicle

object VehicleMapper {
    fun fromEntity(e: VehicleEntity) = Vehicle(
        id = e.id,
        brand = e.brand,
        model = e.model,
        fuelType = e.fuelType,
        vehicleNumber = e.vehicleNumber,
        yearOfPurchase = e.yearOfPurchase,
        ownerName = e.ownerName,
        createdAt = e.createdAt
    )

    fun toEntity(v: Vehicle) = VehicleEntity(
        id = v.id ?: 0,
        brand = v.brand,
        model = v.model,
        fuelType = v.fuelType,
        vehicleNumber = v.vehicleNumber,
        yearOfPurchase = v.yearOfPurchase,
        ownerName = v.ownerName,
        createdAt = if (v.createdAt == 0L) System.currentTimeMillis() else v.createdAt
    )
}

