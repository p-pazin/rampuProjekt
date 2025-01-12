package com.example.carchive.fragments

import CarAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.data.network.Network
import com.example.carchive.data.repositories.VehicleRepository
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.entities.Vehicle
import com.example.carchive.helpers.MockDataLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CarCatalogFragment : Fragment() {

    private  val viewModel : CarCatalogViewModel by viewModels()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggleButton: ImageButton
    private val mockCars = MockDataLoader.getMockCarList()
    private var _binding: FragmentCarCatalogBinding? = null
    private val binding get() = _binding!!
    private val katalog = MockDataLoader.getMockCarList()


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
            mockCars,
            { anchorView, car ->
                showCarOptionsPopup(anchorView, car)
            },
            { anchorView, car ->
                findNavController().navigate(R.id.action_katalogVozilaFragment_to_basicInfoFragment)
            }
        )
        binding.recyclerViewCars.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehicles.collect { data ->
                adapter.updateItems(data)
            }
        }

        viewModel.fetchVehicles()


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
            Toast.makeText(requireContext(), "Uredi vozilo clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()

            val fragment = EditCarFragment()
            val bundle = Bundle()

            bundle.putInt("id", vehicle.id)
            bundle.putString("marka", vehicle.brand)
            bundle.putString("model", vehicle.model)
            bundle.putDouble("type", vehicle.type)
            bundle.putString("productionYear", vehicle.productionYear)
            bundle.putString("registration", vehicle.registration)
            bundle.putInt("kilometers", vehicle.kilometers)
            bundle.putString("location", vehicle.location)
            bundle.putString("motor", vehicle.motor)
            bundle.putInt("enginePower", vehicle.enginePower)
            bundle.putString("gearbox", vehicle.gearbox.externalName)
            bundle.putBoolean("rentSell", vehicle.rentSell)
            bundle.putDouble("price", vehicle.price)
            bundle.putString("imageCar", vehicle.imageCar)

            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
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

        // Potvrda brisanja
        btnPotvrdi.setOnClickListener {
            MockDataLoader.deleteCar(carId) // Poziv funkcije za brisanje automobila
            val message = getString(R.string.car_deleted)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()

            // Osvježava RecyclerView nakon brisanja
            binding.recyclerViewCars.adapter?.notifyDataSetChanged()
        }

        // Otkazivanje dijaloga
        btnOtkazi.setOnClickListener {
            alertDialog.dismiss()
        }

        // Otkazivanje dijaloga preko "Odustani" gumba
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
