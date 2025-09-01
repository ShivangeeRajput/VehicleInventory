package com.example.tummocassignment.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.data.local.entity.VehicleEntity
import com.example.tummocassignment.databinding.ItemVehicleBinding
import com.example.tummocassignment.domain.model.Vehicle
import java.util.Calendar

class VehicleAdapter : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    private val vehicles = mutableListOf<Vehicle>()
    private var onVehicleLongClick: ((Vehicle) -> Unit)? = null

    fun submitList(list: List<Vehicle>) {
        vehicles.clear()
        vehicles.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnVehicleLongClickListener(listener: (Vehicle) -> Unit) {
        onVehicleLongClick = listener
    }

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.tvModel.text = vehicle.model
            binding.tvBrand.text = vehicle.brand
            binding.tvVehicleNumber.text = vehicle.vehicleNumber ?: ""
            binding.tvFuelType.text = vehicle.fuelType
            binding.tvYear.text = vehicle.yearOfPurchase?.toString() ?: "-"

            vehicle.yearOfPurchase?.let {
                val years = Calendar.getInstance().get(Calendar.YEAR) - it
                binding.tvDuration.text = "$years years old"
            }

            binding.root.setOnLongClickListener {
                onVehicleLongClick?.invoke(vehicle)
                true
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
