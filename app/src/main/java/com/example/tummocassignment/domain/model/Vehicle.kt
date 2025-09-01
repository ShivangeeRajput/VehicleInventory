package com.example.tummocassignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val id: Int? = null,
    val brand: String,
    val model: String,
    val fuelType: String,
    val vehicleNumber: String? = null,
    val yearOfPurchase: Int? = null,
    val ownerName: String? = null,
    val createdAt: Long = 0L
) : Parcelable