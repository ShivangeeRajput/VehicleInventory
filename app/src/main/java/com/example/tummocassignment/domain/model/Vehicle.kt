package com.example.tummocassignment.domain.model

data class Vehicle(
    val id: Int = 0,
    val brand: String,
    val model: String,
    val fuelType: String,
    val vehicleNumber: String? = null,
    val yearOfPurchase: Int? = null,
    val ownerName: String? = null,
    val createdAt: Long = 0L
)
