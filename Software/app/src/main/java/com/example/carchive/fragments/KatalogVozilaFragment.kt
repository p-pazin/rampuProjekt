package com.example.carchive

import CarAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.databinding.KatalogVozilaBinding
import com.example.carchive.entities.Car
import com.example.carchive.fragments.DodajVoziloFragment
import com.example.carchive.helpers.MockDataLoader

class KatalogVozilaFragment : Fragment() {

    private val mockCars = MockDataLoader.getMockCarList()
    private var _binding: KatalogVozilaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KatalogVozilaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView and set the adapter
        binding.recyclerViewCars.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCars.adapter = CarAdapter(mockCars) { anchorView, car ->
            // Show PopupWindow when options button is clicked
            showCarOptionsPopup(anchorView, car)
        }

        binding.btnDodaj.setOnClickListener {
            // Navigate to DodajVoziloFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DodajVoziloFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    // Function to show custom PopupWindow with car options
    private fun showCarOptionsPopup(anchorView: View, car: Car) {
        val popupView = layoutInflater.inflate(R.layout.detail_actions_car, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Postavljanje listenera za razne opcije
        popupView.findViewById<View>(R.id.btnPrikazi).setOnClickListener {
            Toast.makeText(requireContext(), "Prikaži vozilo clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnObjavi).setOnClickListener {
            Toast.makeText(requireContext(), "Objavi vozilo clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnStvoriPonudu).setOnClickListener {
            Toast.makeText(requireContext(), "Stvori ponudu clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnProdaj).setOnClickListener {
            Toast.makeText(requireContext(), "Prodaj auto clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnIznajmi).setOnClickListener {
            Toast.makeText(requireContext(), "Iznajmi vozilo clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.btnUredi).setOnClickListener {
            Toast.makeText(requireContext(), "Uredi vozilo clicked", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DodajVoziloFragment())
                .addToBackStack(null)
                .commit()
        }

        // Listener za gumb "Obriši" koji otvara dijalog za potvrdu brisanja
        popupView.findViewById<View>(R.id.btnObrisi).setOnClickListener {
            popupWindow.dismiss()
            Log.d("Debug", "btnObrisi clicked")
            showDeleteWarningDialog(car.id) // Poziva funkciju za prikazivanje dijaloga za potvrdu brisanja
        }

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchorView, -50, 0)
    }

    // Funkcija za prikazivanje dijaloga za potvrdu brisanja automobila
    private fun showDeleteWarningDialog(carId: Int) {
        Log.d("Debug", "Opening delete dialog for carId: $carId")
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
            Toast.makeText(context, "Automobil je obrisan.", Toast.LENGTH_SHORT).show()
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
