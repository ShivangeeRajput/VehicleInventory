package com.example.tummocassignment.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.FilterBrandBinding
import com.example.tummocassignment.databinding.FilterFueltypeBinding
import com.example.tummocassignment.databinding.FilterMainBinding
import com.example.tummocassignment.domain.model.Vehicle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheet(
    private val vehicles: List<Vehicle>,
    private val onFiltered: (List<Vehicle>) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FilterMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FilterMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)

        binding.brandOption.setOnClickListener { showBrandSelection() }
        binding.fuelTypeOption.setOnClickListener { showFuelTypeSelection() }
        binding.ivCloseFilter.setOnClickListener { dismiss() }
    }

    private fun showBrandSelection() {
        val brandViewBinding = FilterBrandBinding.inflate(layoutInflater)
        val container = binding.root.parent as ViewGroup
        container.removeAllViews()
        container.addView(brandViewBinding.root)

        val checkBoxes = listOf(
            brandViewBinding.cbTata to getString(R.string.brand_tata),
            brandViewBinding.cbHonda to getString(R.string.brand_honda),
            brandViewBinding.cbHero to getString(R.string.brand_hero),
            brandViewBinding.cbBajaj to getString(R.string.brand_bajaj),
            brandViewBinding.cbYamaha to getString(R.string.brand_yamaha),
            brandViewBinding.cbOther to getString(R.string.brand_other),
        )

        brandViewBinding.btnClearBrand.setOnClickListener {
            checkBoxes.forEach { it.first.isChecked = false }
        }

        brandViewBinding.btnApplyBrand.setOnClickListener {
            val selectedBrands = checkBoxes.filter { it.first.isChecked }.map { it.second }
            if (selectedBrands.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least 1 brand", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val filtered = vehicles.filter { it.brand in selectedBrands }
            onFiltered(filtered)
            dismiss()
        }
    }

    private fun showFuelTypeSelection() {
        val fuelViewBinding = FilterFueltypeBinding.inflate(layoutInflater)
        val container = binding.root.parent as ViewGroup
        container.removeAllViews()
        container.addView(fuelViewBinding.root)

        val checkBoxes = listOf(
            fuelViewBinding.cbPetrol to getString(R.string.fuel_petrol),
            fuelViewBinding.cbElectric to getString(R.string.fuel_electric),
            fuelViewBinding.cbDiesel to getString(R.string.fuel_diesel),
            fuelViewBinding.cbCNG to getString(R.string.fuel_cng)
        )

        fuelViewBinding.btnClearFuelType.setOnClickListener {
            checkBoxes.forEach { it.first.isChecked = false }
        }

        fuelViewBinding.btnApplyFuelType.setOnClickListener {
            val selectedFuel = checkBoxes.filter { it.first.isChecked }.map { it.second }
            if (selectedFuel.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least 1 fuel type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val filtered = vehicles.filter { it.fuelType in selectedFuel }
            onFiltered(filtered)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


