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
import com.example.carchive.databinding.FragmentBasicInfoBinding
import com.example.carchive.entities.Vehicle
import com.example.carchive.helpers.MockDataLoader
import kotlin.random.Random


class EditBasicInfoFragment : Fragment() {

    private var _binding: FragmentBasicInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasicInfoBinding.inflate(inflater, container, false)


        val args = arguments
        val marka = args?.getString("marka") ?: ""
        val model = args?.getString("model") ?: ""
        val tip = args?.getDouble("type") ?: 0.0
        val productionYear = args?.getString("productionYear") ?: ""
        val registration = args?.getString("registration") ?: ""
        val kilometers = args?.getInt("kilometers") ?: 0
        val location = args?.getString("location") ?: ""
        val motor = args?.getString("motor") ?: ""
        val enginePower = args?.getInt("enginePower") ?: 0
        val gearbox = args?.getString("gearbox") ?: ""
        val rentSell = args?.getBoolean("rentSell") ?: false
        val price = args?.getDouble("price") ?: 0.0

        populateSpinners(marka, model, location)

        binding.spMarka.setSelection(getIndex(binding.spMarka, marka))
        binding.spModel.setSelection(getIndex(binding.spModel, model))
        binding.etTip.setText(tip.toString())
        binding.etGodProizv.setText(productionYear)
        binding.etRegistracija.setText(registration)
        binding.etKilometri.setText(kilometers.toString())
        binding.spLokacija.setSelection(getIndex(binding.spLokacija, location))
        binding.etMotor.setText(motor)
        binding.etEnginePower.setText(enginePower.toString())
        binding.rbSells.isChecked = rentSell
        binding.rbRents.isChecked = !rentSell
        binding.etCijena.setText(price.toString())

        val btnSpremi = binding.btnSpremi
        val spinnerModeli = binding.spModel
        val spinnerMarke = binding.spMarka
        val spinnerLokacija = binding.spLokacija
        val etTip = binding.etTip
        val etGodProizv = binding.etGodProizv
        val etReg = binding.etRegistracija
        val etKm = binding.etKilometri
        val etMotor = binding.etMotor
        val etSnaga = binding.etEnginePower
        val etCijena = binding.etCijena
        val rbProdaja = binding.rbSells
        val rbNajam = binding.rbRents

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
                val rentSell = prodaja

                val vehicle = Vehicle(
                    id = Random.nextInt(1000, 9999),
                    brand = marka,
                    model = model,
                    type = tip.toDoubleOrNull() ?: 0.0,
                    productionYear = godProizv,
                    registration = reg,
                    kilometers = km.toIntOrNull() ?: 0,
                    location = lokacija,
                    motor = motor,
                    enginePower = snaga.toIntOrNull() ?: 0,
                    gearbox = Vehicle.GearboxType.MANUAL,
                    rentSell = rentSell,
                    price = cijena.toDoubleOrNull() ?: 0.0,
                    imageCar = ""
                )

                MockDataLoader.editCar(vehicle)
                val message = getString(R.string.car_edited)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            } else {
                val message = getString(R.string.info_missing)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()            }
        }

        return binding.root
    }

    private fun populateSpinners(marka: String, model: String, location: String) {
        // Populate "Lokacija" spinner
        val lokacije = resources.getStringArray(R.array.gradovi)
        val lokacijeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lokacije)
        binding.spLokacija.adapter = lokacijeAdapter
        binding.spLokacija.setSelection(getIndex(binding.spLokacija, location))

        // Populate "Marka" spinner
        val marke = resources.getStringArray(R.array.Marke)
        val markaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, marke)
        binding.spMarka.adapter = markaAdapter
        binding.spMarka.setSelection(getIndex(binding.spMarka, marka))

        updateModelSpinner(marka, model)

        binding.spMarka.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        binding.spModel.adapter = modelAdapter
        binding.spModel.setSelection(getIndex(binding.spModel, selectedModel))
    }

    private fun getIndex(spinner: Spinner, value: String): Int {
        return (0 until spinner.count).firstOrNull { spinner.getItemAtPosition(it) == value } ?: 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
