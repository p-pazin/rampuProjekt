package com.example.carchive.fragments

import com.example.carchive.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentBasicInfoBinding
import com.example.carchive.databinding.FragmentEditBasicInfoBinding
import com.example.carchive.entities.Vehicle
import com.example.carchive.helpers.MockDataLoader
import kotlin.random.Random


class EditBasicInfoFragment : Fragment() {

    private var _binding: FragmentEditBasicInfoBinding? = null
    private val binding get() = _binding!!
    private val vmVehicle: VehicleCatalogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBasicInfoBinding.inflate(inflater, container, false)


        val args = arguments
        val vehicleId = args!!.getInt("id")
        val marka = args?.getString("marka") ?: ""
        val model = args?.getString("model") ?: ""
        val tip = args?.getString("type") ?: 0.0
        val productionYear = args?.getString("productionYear") ?: ""
        val registration = args?.getString("registration") ?: ""
        val kilometers = args?.getInt("kilometers") ?: 0
        val location = args?.getString("location") ?: ""
        val motor = args?.getString("motor") ?: ""
        val enginePower = args?.getDouble("enginePower") ?: 0
        val gearbox = args?.getString("gearbox") ?: ""
        val rentSell = args?.getBoolean("rentSell") ?: false
        val price = args?.getDouble("price") ?: 0.0
        val cubCapacity = args?.getDouble("cubicCapacity") ?: 0
        val registeredTo = args?.getString("registeredTo") ?: ""
        val color = args?.getString("color") ?: ""
        val driveType = args?.getString("driveType") ?: ""
        val condition = args?.getString("condition") ?: ""

        populateSpinners(marka, model, location, gearbox)

        binding.editSpMarka.setSelection(getIndex(binding.editSpMarka, marka))
        binding.editSpModel.setSelection(getIndex(binding.editSpModel, model))
        binding.editEtTip.setText(tip.toString())
        binding.editEtGodProizv.setText(productionYear)
        binding.editEtRegistracija.setText(registration)
        binding.editEtKilometri.setText(kilometers.toString())
        binding.editSpLokacija.setSelection(getIndex(binding.editSpLokacija, location))
        binding.editEtMotor.setText(motor)
        binding.editEtEnginePower.setText(enginePower.toString())
        binding.editRbSells.isChecked = rentSell
        binding.editRbRents.isChecked = !rentSell
        binding.editEtCijena.setText(price.toString())
        binding.editEtRegisteredTo.setText(registeredTo)
        binding.editEtColor.setText(color)
        binding.editEtDriveType.setText(driveType)
        binding.editEtCubicCapacity.setText(cubCapacity.toString())
        binding.editEtCondition.setText(condition)

        val btnSpremi = binding.editBtnSpremi
        val spinnerModeli = binding.editSpModel
        val spinnerMarke = binding.editSpMarka
        val spinnerLokacija = binding.editSpLokacija
        val etTip = binding.editEtTip
        val etGodProizv = binding.editEtGodProizv
        val etReg = binding.editEtRegistracija
        val etKm = binding.editEtKilometri
        val etMotor = binding.editEtMotor
        val etSnaga = binding.editEtEnginePower
        val etCijena = binding.editEtCijena
        val rbProdaja = binding.editRbSells
        val rbNajam = binding.editRbRents
        val etRegTo = binding.editEtRegisteredTo
        val etColor = binding.editEtColor
        val etDriveType = binding.editEtDriveType
        val etCubCap = binding.editEtCubicCapacity
        val etCondition = binding.editEtCondition


        btnSpremi.setOnClickListener {
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
            val regTo = etRegTo.text.toString()
            val color = etColor.text.toString()
            val driveType = etDriveType.text.toString()
            val cubicCapacity = etCubCap.text.toString()
            val condition = etCondition.text.toString()

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
                (prodaja || najam)
            ) {
                val vehicle = VehicleDto(
                    id = vehicleId,
                    brand = marka,
                    model = model,
                    type = etTip.text.toString(),
                    productionYear = etGodProizv.text.toString().toIntOrNull() ?: 0,
                    registration = reg,
                    mileage = etKm.text.toString().toIntOrNull() ?: 0,
                    engine = etMotor.text.toString(),
                    enginePower = enginePower.toDouble(),
                    transmissionType = Vehicle.GearboxType.MANUAL.toString(),
                    state = rbProdaja.isChecked.toString().toIntOrNull() ?: 0,
                    price = etCijena.text.toString().toDoubleOrNull() ?: 0.0,
                    color = color,
                    cubicCapacity = cubicCapacity.toDouble(),
                    driveType = driveType,
                    condition = condition,
                    registeredTo = regTo
                )

                vmVehicle.putVehicle(vehicleId, vehicle)
            } else {
                val message = getString(R.string.info_missing)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()            }
        }

        val error: String = "Pogreška kod uređivanja vozila"
        vmVehicle.putResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.car_edited), Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun populateSpinners(marka: String, model: String, location: String, gearBox: String) {
        val lokacije = resources.getStringArray(R.array.gradovi)
        val lokacijeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lokacije)
        binding.editSpLokacija.adapter = lokacijeAdapter
        binding.editSpLokacija.setSelection(getIndex(binding.editSpLokacija, location))

        val marke = resources.getStringArray(R.array.Marke)
        val markaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, marke)
        binding.editSpMarka.adapter = markaAdapter
        binding.editSpMarka.setSelection(getIndex(binding.editSpMarka, marka))

        val gearboxOptions = resources.getStringArray(R.array.GearBox)
        val gearBoxAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, gearboxOptions)
        binding.editSpGearbox.adapter = gearBoxAdapter
        binding.editSpGearbox.setSelection(getIndex(binding.editSpGearbox, gearBox))

        updateModelSpinner(marka, model)

        binding.editSpMarka.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMarka = marke[position]
                updateModelSpinner(selectedMarka, model)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateModelSpinner(selectedMarka: String, selectedModel: String) {
        val modelsArray = when (selectedMarka) {
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

        val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, modelsArray)
        binding.editSpModel.adapter = modelAdapter
        binding.editSpModel.setSelection(getIndex(binding.editSpModel, selectedModel))
    }

    private fun getIndex(spinner: Spinner, value: String): Int {
        return (0 until spinner.count).firstOrNull { spinner.getItemAtPosition(it) == value } ?: 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
