package com.example.tummocassignment.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.tummocassignment.R
import com.example.tummocassignment.domain.model.Vehicle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class FilterBottomSheet(
    private val vehicles: List<Vehicle>,
    private val onFiltered: (List<Vehicle>) -> Unit
) : BottomSheetDialogFragment() {

    private var currentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.filter_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        currentView = view

        view.findViewById<LinearLayout>(R.id.brandOption).setOnClickListener {
            showBrandSelection()
        }

        view.findViewById<LinearLayout>(R.id.fuelTypeOption).setOnClickListener {
            showFuelTypeSelection()
        }
    }

    private fun showBrandSelection() {
        val brandView = layoutInflater.inflate(R.layout.filter_brand, null)
        val container = dialog?.findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)
        container?.removeAllViews()
        container?.addView(brandView)

        val cbTata = brandView.findViewById<CheckBox>(R.id.cbTata)
        val cbHonda = brandView.findViewById<CheckBox>(R.id.cbHonda)
        val cbHero = brandView.findViewById<CheckBox>(R.id.cbHero)
        val cbBajaj = brandView.findViewById<CheckBox>(R.id.cbBajaj)
        val cbYamaha = brandView.findViewById<CheckBox>(R.id.cbYamaha)
        val cbOther = brandView.findViewById<CheckBox>(R.id.cbOther)

        brandView.findViewById<MaterialButton>(R.id.btnClearBrand).setOnClickListener {
            cbTata.isChecked = false
            cbHonda.isChecked = false
            cbHero.isChecked = false
            cbBajaj.isChecked = false
            cbYamaha.isChecked = false
            cbOther.isChecked = false
        }

        brandView.findViewById<MaterialButton>(R.id.btnApplyBrand).setOnClickListener {
            val selectedBrands = mutableListOf<String>()
            if(cbTata.isChecked) selectedBrands.add("Tata")
            if(cbHonda.isChecked) selectedBrands.add("Honda")
            if(cbHero.isChecked) selectedBrands.add("Hero")
            if(cbBajaj.isChecked) selectedBrands.add("Bajaj")
            if(cbYamaha.isChecked) selectedBrands.add("Yamaha")
            if(cbOther.isChecked) selectedBrands.add("Other")

            val filtered = if(selectedBrands.isEmpty()) vehicles else vehicles.filter { it.brand in selectedBrands }
            onFiltered(filtered)
            dismiss()
        }
    }


    private fun showFuelTypeSelection() {
        val fuelView = layoutInflater.inflate(R.layout.filter_fueltype, null)
        val container = dialog?.findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)

        container?.removeAllViews()
        container?.addView(fuelView)

        val cbPetrol = fuelView.findViewById<CheckBox>(R.id.cbPetrol)
        val cbElectric = fuelView.findViewById<CheckBox>(R.id.cbElectric)
        val cbDiesel = fuelView.findViewById<CheckBox>(R.id.cbDiesel)
        val cbCNG = fuelView.findViewById<CheckBox>(R.id.cbCNG)

        fuelView.findViewById<MaterialButton>(R.id.btnClearFuelType).setOnClickListener {
            cbPetrol.isChecked = false
            cbElectric.isChecked = false
            cbDiesel.isChecked = false
            cbCNG.isChecked = false
        }

        fuelView.findViewById<MaterialButton>(R.id.btnApplyFuelType).setOnClickListener {
            val selectedFuel = mutableListOf<String>()
            if(cbPetrol.isChecked) selectedFuel.add("Petrol")
            if(cbElectric.isChecked) selectedFuel.add("Electric")
            if(cbDiesel.isChecked) selectedFuel.add("Diesel")
            if(cbCNG.isChecked) selectedFuel.add("CNG")

            val filtered = vehicles.filter { it.fuelType in selectedFuel }
            onFiltered(if(filtered.isEmpty()) emptyList() else filtered)
            dismiss()
        }
    }
}
