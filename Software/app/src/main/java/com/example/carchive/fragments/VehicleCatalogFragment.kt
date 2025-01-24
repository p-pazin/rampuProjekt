package com.example.carchive.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.adapters.CarAdapter
import com.example.carchive.data.dto.VehiclePhotoDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.entities.Vehicle
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import kotlinx.coroutines.launch

class VehicleCatalogFragment : Fragment() {

    private  val viewModel : VehicleCatalogViewModel by viewModels()
    private var _binding: FragmentCarCatalogBinding? = null
    private val binding get() = _binding!!
    private val vmVehicle: VehicleCatalogViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarCatalogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnDodaj = binding.sidebarLogo.btnDodaj


        binding.sidebarLogo.drawerToggleButton.setOnClickListener(){
            (activity as? CarchiveActivity)?.toggleDrawer()
        }
        binding.recyclerViewCars.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CarAdapter(
            emptyList(),
            { anchorView, car ->
                showCarOptionsPopup(anchorView, car)
            },
            { _, vehicle ->
                val bundle = Bundle()

                bundle.putInt("id", vehicle.id)
                bundle.putString("marka", vehicle.brand)
                bundle.putString("model", vehicle.model)
                bundle.putString("type", vehicle.type)
                bundle.putString("productionYear", vehicle.productionYear)
                bundle.putString("registration", vehicle.registration)
                bundle.putInt("kilometers", vehicle.kilometers)
                bundle.putString("location", vehicle.location)
                bundle.putString("motor", vehicle.motor)
                bundle.putDouble("enginePower", vehicle.enginePower)
                bundle.putString("gearbox", vehicle.gearbox.externalName)
                bundle.putInt("rentSell", vehicle.rentSell)
                var usageString = ""
                if(vehicle.usage == 1){
                    usageString = "Prodaja"
                }
                else {
                    usageString = "Iznajmljivanje"
                }
                bundle.putString("usage", usageString)
                bundle.putDouble("price", vehicle.price)
                bundle.putString("imageCar", vehicle.imageCar)
                bundle.putDouble("cubicCapacity", vehicle.cubicCapacity)
                bundle.putString("color", vehicle.color)
                bundle.putString("driveType", vehicle.driveType)
                bundle.putString("condition", vehicle.condition)
                bundle.putString("registeredTo", vehicle.registeredTo)
                findNavController().navigate(R.id.action_katalogVozilaFragment_to_vehicleDetailsFragment, bundle)
            },{ vehicleId, callback ->
                viewLifecycleOwner.lifecycleScope.launch {
                    when (val result = viewModel.getVehiclePhotosCatalog(vehicleId)) {
                        is Result.Success -> {
                            val firstPhotoUrl = result.data.firstOrNull()?.link
                            callback(firstPhotoUrl)
                        }
                        is Result.Error -> {
                            callback(null)
                        }
                    }
                }
            }
        )
        binding.recyclerViewCars.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehicles.collect { data ->
                adapter.updateItems(data)
            }
        }

        viewModel.fetchVehiclesCatalog()


        btnDodaj.setOnClickListener {
            findNavController().navigate(R.id.action_katalogVozilaFragment_to_dodajVoziloFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? CarchiveActivity)?.setDrawerEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? CarchiveActivity)?.setDrawerEnabled(false)
    }

    private fun showCarOptionsPopup(anchorView: View, vehicle: Vehicle) {
        val popupView = layoutInflater.inflate(R.layout.detail_actions_car, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<View>(R.id.btnPrikazi).setOnClickListener {
            val bundle = Bundle()

            bundle.putInt("id", vehicle.id)
            bundle.putString("marka", vehicle.brand)
            bundle.putString("model", vehicle.model)
            bundle.putString("type", vehicle.type)
            bundle.putString("productionYear", vehicle.productionYear)
            bundle.putString("registration", vehicle.registration)
            bundle.putInt("kilometers", vehicle.kilometers)
            bundle.putString("location", vehicle.location)
            bundle.putString("motor", vehicle.motor)
            bundle.putDouble("enginePower", vehicle.enginePower)
            bundle.putString("gearbox", vehicle.gearbox.externalName)
            bundle.putInt("rentSell", vehicle.rentSell)
            var usageString = ""
            if(vehicle.usage == 1){
                usageString = "Prodaja"
            }
            else {
                usageString = "Iznajmljivanje"
            }
            bundle.putString("usage", usageString)
            bundle.putDouble("price", vehicle.price)
            bundle.putString("imageCar", vehicle.imageCar)
            bundle.putDouble("cubicCapacity", vehicle.cubicCapacity)
            bundle.putString("color", vehicle.color)
            bundle.putString("driveType", vehicle.driveType)
            bundle.putString("condition", vehicle.condition)
            bundle.putString("registeredTo", vehicle.registeredTo)

            findNavController().navigate(R.id.action_katalogVozilaFragment_to_vehicleDetailsFragment, bundle)
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnObjavi).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnStvoriPonudu).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnProdaj).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnIznajmi).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnUredi).setOnClickListener {
            popupWindow.dismiss()

            val fragment = EditVehicleFragment()
            val bundle = Bundle()

            bundle.putInt("id", vehicle.id)
            bundle.putString("marka", vehicle.brand)
            bundle.putString("model", vehicle.model)
            bundle.putString("type", vehicle.type)
            bundle.putString("productionYear", vehicle.productionYear)
            bundle.putString("registration", vehicle.registration)
            bundle.putInt("kilometers", vehicle.kilometers)
            bundle.putString("location", vehicle.location)
            bundle.putString("motor", vehicle.motor)
            bundle.putDouble("enginePower", vehicle.enginePower)
            bundle.putString("gearbox", vehicle.gearbox.externalName)
            bundle.putInt("rentSell", vehicle.rentSell)
            bundle.putDouble("price", vehicle.price)
            bundle.putString("imageCar", vehicle.imageCar)
            bundle.putDouble("cubicCapacity", vehicle.cubicCapacity)
            bundle.putString("color", vehicle.color)
            bundle.putString("driveType", vehicle.driveType)
            bundle.putString("condition", vehicle.condition)
            bundle.putString("registeredTo", vehicle.registeredTo)
            var usageString = ""
            if(vehicle.usage == 1){
                usageString = "Prodaja"
            }
            else {
                usageString = "Iznajmljivanje"
            }
            bundle.putString("usage", usageString)

            fragment.arguments = bundle

            findNavController().navigate(R.id.action_katalogVozilaFragment_to_editVehicleFragment, bundle)
        }


        popupView.findViewById<View>(R.id.btnObrisi).setOnClickListener {
            popupWindow.dismiss()
            showDeleteWarningDialog(vehicle.id)
        }

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchorView, -50, 0)
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
