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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.entities.Car
import com.example.carchive.helpers.MockDataLoader

class CarCatalogFragment : Fragment() {

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
        binding.recyclerViewCars.adapter = CarAdapter(mockCars) { anchorView, car ->
            showCarOptionsPopup(anchorView, car)
        }

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

    private fun showCarOptionsPopup(anchorView: View, car: Car) {
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

            bundle.putInt("id", car.id)
            bundle.putString("marka", car.marka)
            bundle.putString("model", car.model)
            bundle.putDouble("type", car.type)
            bundle.putString("productionYear", car.productionYear)
            bundle.putString("registration", car.registration)
            bundle.putInt("kilometers", car.kilometers)
            bundle.putString("location", car.location)
            bundle.putString("motor", car.motor)
            bundle.putInt("enginePower", car.enginePower)
            bundle.putString("gearbox", car.gearbox)
            bundle.putBoolean("rentSell", car.rentSell)
            bundle.putDouble("price", car.price)
            bundle.putString("imageCar", car.imageCar)

            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }


        popupView.findViewById<View>(R.id.btnObrisi).setOnClickListener {
            popupWindow.dismiss()
            showDeleteWarningDialog(car.id)
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
