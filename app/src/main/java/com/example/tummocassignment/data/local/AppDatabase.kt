package com.example.tummocassignment.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tummocassignment.data.local.dao.VehicleDao
import com.example.tummocassignment.data.local.entity.VehicleEntity


@Database(entities = [VehicleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}
