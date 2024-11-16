package com.example.carchive.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carchive.R
import com.example.carchive.databinding.OpceInformacijeBinding
import com.example.carchive.entities.Car
import com.example.carchive.helpers.MockDataLoader
import kotlin.random.Random

class OpceInformacijeFragment : Fragment() {

    private var _binding: OpceInformacijeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OpceInformacijeBinding.inflate(inflater, container, false)

        val spinnerLokacija = binding.spLokacija
        val lokacije = resources.getStringArray(R.array.lokacije)

        val lokacijeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lokacije)
        lokacijeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLokacija.adapter = lokacijeAdapter

        // Spinner za marke
        val spinnerMarke = binding.spMarka
        val marke = resources.getStringArray(R.array.Marke)

        // Adapter za Spinner marke
        val markaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, marke)
        markaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarke.adapter = markaAdapter

        // Spinner za modele (početno prazan)
        val spinnerModeli = binding.spModel
        var modelsArray = emptyArray<String>()
        val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modelsArray)
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerModeli.adapter = modelAdapter

        // Dodavanje onItemSelectedListener za spinner marke
        spinnerMarke.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Prema odabranoj marki, popuni modele
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

                val car = Car(
                    id = Random.nextInt(1000, 9999),
                    marka = marka,
                    model = model,
                    type = tip.toDoubleOrNull() ?: 0.0,
                    productionYear = godProizv,
                    registration = reg,
                    kilometers = km.toIntOrNull() ?: 0,
                    location = lokacija,
                    motor = motor,
                    enginePower = snaga.toIntOrNull() ?: 0,
                    gearbox = "manual",
                    rentSell = rentSell,
                    price = cijena.toDoubleOrNull() ?: 0.0,
                    imageCar = ""
                )

                MockDataLoader.addCar(car)
                Toast.makeText(requireContext(), "Automobil dodan u katalog.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Sva polja su obavezna!", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
