package com.example.carchive.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.carchive.R
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentVehicleDetailsBinding
import com.example.carchive.viewmodels.VehicleCatalogViewModel

class VehicleDetailsFragment : Fragment() {
    private var _binding: FragmentVehicleDetailsBinding? = null
    private val vmVehicle: VehicleCatalogViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var vehicleId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnVehicleDetailsEdit.setOnClickListener{
            arguments?.let{ bundle ->

                binding.tvBrandModel.text = "${bundle.getString("marka", "Unknown")} - ${bundle.getString("model", "Unknown")}"
                binding.tvType.text = "Tip: ${bundle.getString("type", "Unknown")}"
                binding.tvProductionYear.text = "Godina proizvodnje: ${bundle.getString("productionYear", "Unknown")}"
                binding.tvRegistration.text = "Registracija: ${bundle.getString("registration", "Unknown")}"
                binding.tvKilometers.text = "Kilometraža: ${bundle.getInt("kilometers", 0)}"
                binding.tvLocation.text = "Lokacija: ${bundle.getString("location", "Unknown")}"
                binding.tvMotor.text = "Motor: ${bundle.getString("motor", "Unknown")}"
                binding.tvEnginePower.text = "Snaga motora: ${bundle.getDouble("enginePower", 0.0)} HP"
                binding.tvGearbox.text = "Mjenjač: ${bundle.getString("gearbox", "Unknown")}"
                binding.tvRentSell.text = "Status: ${if (bundle.getBoolean("rentSell", false)) "U prodaji" else "U najmu"}"
                binding.tvPrice.text = "Cijena: €${bundle.getDouble("price", 0.0)}"

                findNavController().navigate(R.id.action_vehicleDetailsFragment_to_editVehicleFragment, bundle)
            }
        }

        binding.btnVehicleDetailsDelete.setOnClickListener{
            showDeleteWarningDialog(vehicleId)
        }

        arguments?.let { bundle ->
            vehicleId = bundle.getInt("id", 0)
            binding.tvBrandModel.text = "${bundle.getString("marka", "Unknown")} - ${bundle.getString("model", "Unknown")}"
            binding.tvType.text = "Tip: ${bundle.getString("type", "Unknown")}"
            binding.tvProductionYear.text = "Godina proizvodnje: ${bundle.getString("productionYear", "Unknown")}"
            binding.tvRegistration.text = "Registracija: ${bundle.getString("registration", "Unknown")}"
            binding.tvKilometers.text = "Kilometraža: ${bundle.getInt("kilometers", 0)}"
            binding.tvLocation.text = "Lokacija: ${bundle.getString("location", "Unknown")}"
            binding.tvMotor.text = "Motor: ${bundle.getString("motor", "Unknown")}"
            binding.tvEnginePower.text = "Snaga motora: ${bundle.getDouble("enginePower", 0.0)} HP"
            binding.tvGearbox.text = "Mjenjač: ${bundle.getString("gearbox", "Unknown")}"
            binding.tvRentSell.text = "Status: ${if (bundle.getBoolean("rentSell", false)) "U prodaji" else "U najmu"}"
            binding.tvPrice.text = "Cijena: €${bundle.getDouble("price", 0.0)}"

            val imageCar = bundle.getString("imageCar")
            if (!imageCar.isNullOrEmpty()) {
                val resourceId = resources.getIdentifier(imageCar, "drawable", requireContext().packageName)
                if (resourceId != 0) {
                    binding.ivCarImage.setImageResource(resourceId)
                } else {
                    binding.ivCarImage.setImageResource(R.drawable.ic_katalog_vozila)
                }
            } else {
                binding.ivCarImage.setImageResource(R.drawable.ic_katalog_vozila)
            }
        }
    }

    private fun showDeleteWarningDialog(carId: Int) {
        val deletionWarning = LayoutInflater.from(context).inflate(R.layout.deleting_warning, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(deletionWarning)
            .create()

        val btnPotvrdi = deletionWarning.findViewById<Button>(R.id.btnPotvrdi)
        val btnOtkazi = deletionWarning.findViewById<ImageButton>(R.id.btnOtkazi)
        val btnOdustani = deletionWarning.findViewById<Button>(R.id.btnOdustani)

        val error: String = "Pogreška kod brisanja vozila"
        vmVehicle.deleteResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), getString(R.string.car_deleted), Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnPotvrdi.setOnClickListener {
            vmVehicle.deleteVehicle(carId)
            alertDialog.dismiss()
        }

        btnOtkazi.setOnClickListener {
            alertDialog.dismiss()
        }

        btnOdustani.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}