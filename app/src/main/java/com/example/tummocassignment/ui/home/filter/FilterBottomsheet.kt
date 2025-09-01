package com.example.tummocassignment.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.FilterBrandBinding
import com.example.tummocassignment.databinding.FilterFueltypeBinding
import com.example.tummocassignment.databinding.FilterMainBinding
import com.example.tummocassignment.domain.model.Vehicle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheet(
    private val vehicles: List<Vehicle>,
    private val onFiltered: (List<Vehicle>) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FilterMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandBinding: FilterBrandBinding
    private lateinit var fuelBinding: FilterFueltypeBinding

    private var selectedBrands = mutableSetOf<String>()
    private var selectedFuelTypes = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterMainBinding.inflate(inflater, container, false)

        brandBinding = FilterBrandBinding.inflate(inflater)
        fuelBinding = FilterFueltypeBinding.inflate(inflater)

        val root = binding.root as ViewGroup
        val brandInclude = root.findViewById<View>(R.id.brandSelectionView)
        val fuelInclude = root.findViewById<View>(R.id.fuelTypeSelectionView)

        val indexBrand = root.indexOfChild(brandInclude)
        val indexFuel = root.indexOfChild(fuelInclude)

        root.removeView(brandInclude)
        root.removeView(fuelInclude)

        root.addView(brandBinding.root, indexBrand)
        root.addView(fuelBinding.root, indexFuel)

        brandBinding.root.id = R.id.brandSelectionView
        fuelBinding.root.id = R.id.fuelTypeSelectionView

        brandBinding.root.visibility = View.GONE
        fuelBinding.root.visibility = View.GONE

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)

        binding.brandOption.setOnClickListener { showSelection(brandBinding.root) }
        binding.fuelTypeOption.setOnClickListener { showSelection(fuelBinding.root) }
        binding.ivCloseFilter.setOnClickListener { dismiss() }

        setupBrandSelection()
        setupFuelTypeSelection()
    }

    private fun showSelection(selectionView: View) {
        binding.mainFilterView.animate()
            .translationX(-binding.mainFilterView.width.toFloat())
            .setDuration(250)
            .withEndAction {
                binding.mainFilterView.visibility = View.GONE
                selectionView.visibility = View.VISIBLE
                selectionView.translationX = selectionView.width.toFloat()
                selectionView.animate()
                    .translationX(0f)
                    .setDuration(250)
                    .start()
            }
            .start()
    }

    private fun setupBrandSelection() {
        val checkBoxes = listOf(
            brandBinding.cbTata to getString(R.string.brand_tata),
            brandBinding.cbHonda to getString(R.string.brand_honda),
            brandBinding.cbHero to getString(R.string.brand_hero),
            brandBinding.cbBajaj to getString(R.string.brand_bajaj),
            brandBinding.cbYamaha to getString(R.string.brand_yamaha),
            brandBinding.cbOther to getString(R.string.brand_other)
        )

        checkBoxes.forEach { (cb, brand) -> cb.isChecked = brand in selectedBrands }

        brandBinding.btnClearBrand.setOnClickListener {
            checkBoxes.forEach { it.first.isChecked = false }
        }

        brandBinding.btnApplyBrand.setOnClickListener {
            selectedBrands.clear()
            selectedBrands.addAll(checkBoxes.filter { it.first.isChecked }.map { it.second })
            applyFilters()
        }

    }

    private fun setupFuelTypeSelection() {
        val checkBoxes = listOf(
            fuelBinding.cbPetrol to getString(R.string.fuel_petrol),
            fuelBinding.cbElectric to getString(R.string.fuel_electric),
            fuelBinding.cbDiesel to getString(R.string.fuel_diesel),
            fuelBinding.cbCNG to getString(R.string.fuel_cng)
        )

        checkBoxes.forEach { (cb, fuel) -> cb.isChecked = fuel in selectedFuelTypes }

        fuelBinding.btnClearFuelType.setOnClickListener {
            checkBoxes.forEach { it.first.isChecked = false }
        }

        fuelBinding.btnApplyFuelType.setOnClickListener {
            selectedFuelTypes.clear()
            selectedFuelTypes.addAll(checkBoxes.filter { it.first.isChecked }.map { it.second })
            applyFilters()
        }
    }

    private fun applyFilters() {
        val filtered = vehicles.filter { vehicle ->
            (selectedBrands.isEmpty() || vehicle.brand in selectedBrands) &&
                    (selectedFuelTypes.isEmpty() || vehicle.fuelType in selectedFuelTypes)
        }
        onFiltered(filtered)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}






