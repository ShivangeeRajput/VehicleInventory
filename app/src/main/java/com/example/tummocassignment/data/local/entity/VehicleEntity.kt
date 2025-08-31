package com.example.tummocassignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brand: String,
    val model: String,
    val fuelType: String,
    val vehicleNumber: String? = null,
    val yearOfPurchase: Int? = null,
    val ownerName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
