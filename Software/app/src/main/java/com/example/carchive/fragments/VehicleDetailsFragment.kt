package com.example.carchive.fragments

import VehiclePhotosAdapter
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
import com.example.carchive.data.dto.VehiclePhotoDto
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

        binding.btnVehicleDetailsCreateOffer.setOnClickListener {
            arguments?.let{ bundle ->
                binding.tvRegistration.text = "Registracija: ${bundle.getString("registration", "Unknown")}"
                bundle.putInt("id", vehicleId)

                findNavController().navigate(R.id.action_vehicleDetailsFragment_to_addOfferFragment, bundle)
            }
        }

        binding.btnVehicleDetailsEdit.setOnClickListener{
            arguments?.let{ bundle ->

                binding.tvBrandModel.text = "${bundle.getString("marka", "Unknown")} - ${bundle.getString("model", "Unknown")}"
                binding.tvType.text = "Tip: ${bundle.getString("type", "Unknown")}"
                binding.tvProductionYear.text = "Godina proizvodnje: ${bundle.getString("productionYear", "Unknown")}"
                binding.tvRegistration.text = "Registracija: ${bundle.getString("registration", "Unknown")}"
                binding.tvKilometers.text = "Kilometraža: ${bundle.getInt("kilometers", 0)}"
                binding.tvMotor.text = "Motor: ${bundle.getString("motor", "Unknown")}"
                binding.tvEnginePower.text = "Snaga motora: ${bundle.getDouble("enginePower", 0.0)} HP"
                binding.tvGearbox.text = "Mjenjač: ${bundle.getString("gearbox", "Unknown")}"
                binding.tvRentSell.text = "Upotreba: ${(bundle.getString("usage", "Prodaja"))}"
                binding.tvPrice.text = "Cijena: €${bundle.getDouble("price", 0.0)}"

                findNavController().navigate(
                    R.id.action_vehicleDetailsFragment_to_editVehicleFragment,
                    bundle
                )
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
            binding.tvMotor.text = "Motor: ${bundle.getString("motor", "Unknown")}"
            binding.tvEnginePower.text = "Snaga motora: ${bundle.getDouble("enginePower", 0.0)} HP"
            binding.tvGearbox.text = "Mjenjač: ${bundle.getString("gearbox", "Unknown")}"
            binding.tvRentSell.text = "Upotreba: ${(bundle.getString("usage", "Prodaja"))}"
            binding.tvPrice.text = "Cijena: €${bundle.getDouble("price", 0.0)}"

            vmVehicle.getVehiclePhotos(vehicleId)

            observeVehiclePhotos()
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


        btnPotvrdi.setOnClickListener {
            vmVehicle.deleteVehicle(carId)


            vmVehicle.deleteResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), getString(R.string.car_deleted), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_vehicleDetailsFragment_to_vehicleCatalogFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.delete_error_car), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_vehicleDetailsFragment_to_vehicleCatalogFragment)
                    }
                }
            }
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

    private fun observeVehiclePhotos() {
        vmVehicle.photosResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    setupImageRecyclerView(result.data)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error loading images: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupImageRecyclerView(imageList: List<VehiclePhotoDto>) {
        val adapter = VehiclePhotosAdapter(imageList)
        binding.rvVehicleImages.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}