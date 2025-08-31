package com.example.tummocassignment.di

import android.content.Context
import androidx.room.Room
import com.example.tummocassignment.data.local.AppDatabase
import com.example.tummocassignment.data.local.dao.VehicleDao
import com.example.tummocassignment.data.repository.VehicleRepositoryImpl
import com.example.tummocassignment.domain.repository.VehicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "tummoc_db").build()

    @Provides
    fun provideVehicleDao(db: AppDatabase): VehicleDao = db.vehicleDao()

    @Provides
    @Singleton
    fun provideVehicleRepository(dao: VehicleDao): VehicleRepository =
        VehicleRepositoryImpl(dao)
}
