package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentBasicInfoBinding
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BasicInfoFragment : Fragment() {

    private var _binding: FragmentBasicInfoBinding? = null
    private val binding get() = _binding!!
    private val vmVehicle: VehicleCatalogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasicInfoBinding.inflate(inflater, container, false)

        val spinnerLokacija = binding.spLokacija
        val lokacije = resources.getStringArray(R.array.gradovi)

        val lokacijeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lokacije)
        lokacijeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLokacija.adapter = lokacijeAdapter

        val spinnerMarke = binding.spMarka
        val marke = resources.getStringArray(R.array.Marke)

        val markaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, marke)
        markaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarke.adapter = markaAdapter

        val spinnerGearBox = binding.spGearbox
        val gearboxOptions = resources.getStringArray(R.array.GearBox)

        val gearBoxAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, gearboxOptions)
        gearBoxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGearBox.adapter = gearBoxAdapter

        val spinnerModeli = binding.spModel
        var modelsArray = emptyArray<String>()
        val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modelsArray)
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerModeli.adapter = modelAdapter

        spinnerMarke.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBrand = marke[position]
                modelsArray = when (selectedBrand) {
                    "BMW" -> resources.getStringArray(R.array.model_BMW)
                    "Audi" -> resources.getStringArray(R.array.model_Audi)
                    "Mercedes" -> resources.getStringArray(R.array.model_Mercedes)
                    "Škoda" -> resources.getStringArray(R.array.model_Škoda)
                    "Renault" -> resources.getStringArray(R.array.model_Renault)
                    "Volkswagen" -> resources.getStringArray(R.array.model_Volkswagen)
                    "Opel" -> resources.getStringArray(R.array.model_Opel)
                    "Ford" -> resources.getStringArray(R.array.model_Ford)
                    "Peugeot" -> resources.getStringArray(R.array.model_Peugeot)
                    "Honda" -> resources.getStringArray(R.array.model_Honda)
                    "Toyota" -> resources.getStringArray(R.array.model_Toyota)
                    "Fiat" -> resources.getStringArray(R.array.model_Fiat)
                    "Citroën" -> resources.getStringArray(R.array.model_Citroën)
                    "Ferrari" -> resources.getStringArray(R.array.model_Ferrari)
                    "Lamborghini" -> resources.getStringArray(R.array.model_Lamborghini)
                    "Porsche" -> resources.getStringArray(R.array.model_Porsche)
                    "Aston Martin" -> resources.getStringArray(R.array.model_Aston_Martin)
                    "Jaguar" -> resources.getStringArray(R.array.model_Jaguar)
                    "Land Rover" -> resources.getStringArray(R.array.model_Land_Rover)
                    "Chrysler" -> resources.getStringArray(R.array.model_Chrysler)
                    "Dodge" -> resources.getStringArray(R.array.model_Dodge)
                    "Buick" -> resources.getStringArray(R.array.model_Buick)
                    "Chevrolet" -> resources.getStringArray(R.array.model_Chevrolet)
                    "Nissan" -> resources.getStringArray(R.array.model_Nissan)
                    "Mitsubishi" -> resources.getStringArray(R.array.model_Mitsubishi)
                    "Kia" -> resources.getStringArray(R.array.model_Kia)
                    "Hyundai" -> resources.getStringArray(R.array.model_Hyundai)
                    "Subaru" -> resources.getStringArray(R.array.model_Subaru)
                    "Alfa Romeo" -> resources.getStringArray(R.array.model_Alfa_Romeo)
                    "Seat" -> resources.getStringArray(R.array.model_Seat)
                    "Mazda" -> resources.getStringArray(R.array.model_Mazda)
                    "Saab" -> resources.getStringArray(R.array.model_Saab)
                    "Tesla" -> resources.getStringArray(R.array.model_Tesla)
                    "Lincoln" -> resources.getStringArray(R.array.model_Lincoln)
                    "Cadillac" -> resources.getStringArray(R.array.model_Cadillac)
                    "Genesis" -> resources.getStringArray(R.array.model_Genesis)
                    "Mini" -> resources.getStringArray(R.array.model_Mini)
                    "Smart" -> resources.getStringArray(R.array.model_Smart)
                    "Rivian" -> resources.getStringArray(R.array.model_Rivian)
                    else -> emptyArray()
                }
                val newModelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modelsArray)
                newModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerModeli.adapter = newModelAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val btnSpremi = binding.btnSpremi
        val etTip = binding.etTip
        val etGodProizv = binding.etGodProizv
        val etReg = binding.etRegistracija
        val etKm = binding.etKilometri
        val etMotor = binding.etMotor
        val etSnaga = binding.etEnginePower
        val etCijena = binding.etCijena
        val rbProdaja = binding.rbSells
        val rbNajam = binding.rbRents
        val etCubCap = binding.etCubicCapacity

        val year = binding.datePicker.year
        val month = binding.datePicker.month
        val dayOfMonth = binding.datePicker.dayOfMonth

        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, dayOfMonth)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = outputFormat.format(selectedCalendar.time)
        val etColor = binding.etColor
        val etDriveType = binding.etDriveType
        val etEngPow = binding.etEnginePower
        val etCond = binding.etCondition



        btnSpremi.setOnClickListener{
            val marka = spinnerMarke.selectedItem.toString()
            val model = spinnerModeli.selectedItem.toString()
            val lokacija = spinnerLokacija.selectedItem.toString()
            val tip = etTip.text.toString()
            val godProizv = etGodProizv.text.toString()
            val reg = etReg.text.toString()
            val km = etKm.text.toString()
            val motor = etMotor.text.toString()
            val snaga = etSnaga.text.toString()
            val cijena = etCijena.text.toString()
            val prodaja = rbProdaja.isChecked
            val najam = rbNajam.isChecked
            val cubCapacity = etCubCap.text.toString()
            val registeredTo = formattedDate
            val color = etColor.text.toString()
            val driveType = etDriveType.text.toString()
            val engPower = etEngPow.text.toString()
            val condition = etCond.text.toString()

        if (marka.isNotBlank() &&
            model.isNotBlank() &&
            lokacija.isNotBlank() &&
            tip.isNotBlank() &&
            godProizv.isNotBlank() &&
            reg.isNotBlank() &&
            km.isNotBlank() &&
            motor.isNotBlank() &&
            snaga.isNotBlank() &&
            cijena.isNotBlank() &&
            cubCapacity.isNotBlank() &&
            registeredTo.isNotBlank() &&
            color.isNotBlank() &&
            driveType.isNotBlank() &&
            engPower.isNotBlank() &&
            condition.isNotBlank() &&
            (prodaja || najam)
        ) {
                val vehicle = VehicleDtoPost(
                    brand = spinnerMarke.selectedItem.toString(),
                    model = spinnerModeli.selectedItem.toString(),
                    type = tip,
                    productionYear = godProizv.toInt(),
                    registration = reg,
                    mileage = km.toInt(),
                    engine = etMotor.text.toString(),
                    enginePower = engPower.toDouble(),
                    transmissionType = spinnerGearBox.selectedItem.toString(),
                    state = rbProdaja.isChecked.toString().toIntOrNull() ?: 0,
                    price = cijena.toDouble(),
                    color = color,
                    cubicCapacity = cubCapacity.toDouble(),
                    registeredTo = registeredTo,
                    driveType = driveType,
                    condition = condition
                )

                vmVehicle.postVehicle(vehicle)
                vmVehicle.getVehicleIdByReg(reg)
                vmVehicle.setBasicInfoComplete(true)
            } else {
                Toast.makeText(requireContext(), getString(R.string.info_missing), Toast.LENGTH_SHORT).show()
            }
        }

        val error: String = "Pogreška kod dodavanja vozila"
        vmVehicle.postResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.car_added), Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }

                null -> TODO()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
