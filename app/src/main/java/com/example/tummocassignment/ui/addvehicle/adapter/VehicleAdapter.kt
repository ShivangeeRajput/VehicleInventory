package com.example.tummocassignment.ui.addvehicle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.data.local.entity.VehicleEntity
import com.example.tummocassignment.databinding.ItemVehicleBinding
import com.example.tummocassignment.domain.model.Vehicle
import java.util.Calendar

class VehicleAdapter : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    private val vehicles = mutableListOf<Vehicle>()

    fun submitList(list: List<Vehicle>) {
        vehicles.clear()
        vehicles.addAll(list)
        notifyDataSetChanged()
    }

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.tvModel.text = vehicle.model
            binding.tvBrand.text = vehicle.brand
            binding.tvVehicleNumber.text = vehicle.vehicleNumber ?: ""
            binding.tvFuelType.text = vehicle.fuelType
            binding.tvYear.text = vehicle.yearOfPurchase?.toString() ?: "-"

            // Optional: calculate duration
            vehicle.yearOfPurchase?.let {
                val years = Calendar.getInstance().get(Calendar.YEAR) - it
                binding.tvDuration.text = "$years years old"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(vehicles[position])
    }

    override fun getItemCount() = vehicles.size
}
