package com.example.carchive.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.carchive.R
import com.example.carchive.adapters.PicturesAdapter
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentAddPicturesBinding
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import java.io.File

class AddPicturesFragment : Fragment() {

    private var _binding: FragmentAddPicturesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PicturesAdapter
    private val uploadedPictures = mutableListOf<Pair<Uri, Int>>()
    private val newPictures: MutableList<Uri> = mutableListOf()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var vmVehicle: VehicleCatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPicturesBinding.inflate(inflater, container, false)

        vmVehicle = ViewModelProvider(requireActivity()).get(VehicleCatalogViewModel::class.java)

        setupRecyclerView()
        setupImagePicker()
        setupButtons()

        observePhotosResponse()
        observeUploadResponse()
        observeConnectedResponse()

        val vehicleId = vmVehicle.getVehicleId()
        vehicleId?.let { vmVehicle.getVehiclePhotos(it) }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = PicturesAdapter(mutableListOf()) { imageUri ->
            showDeleteWarningDialog(imageUri)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupImagePicker() {
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    newPictures.add(imageUri)
                    updateAdapterData()
                } else {
                    Toast.makeText(requireContext(), "Pogreška kod odabiranja fotografije", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupButtons() {
        binding.btnDodajSliku.setOnClickListener {
            openGallery()
        }

        binding.btnSpremiSlike.setOnClickListener {
            savePhotos()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun updateAdapterData() {
        val allPictures = mutableListOf<Uri>().apply {
            addAll(uploadedPictures.map { it.first })
            addAll(newPictures)
        }
        adapter.updateData(allPictures)
    }

    private fun showDeleteWarningDialog(imageUri: Uri) {
        val deletionWarning = LayoutInflater.from(context).inflate(R.layout.deleting_warning_photos, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(deletionWarning)
            .create()

        val btnPotvrdi = deletionWarning.findViewById<Button>(R.id.btnPotvrdi)
        val btnOtkazi = deletionWarning.findViewById<ImageButton>(R.id.btnOtkazi)
        val btnOdustani = deletionWarning.findViewById<Button>(R.id.btnOdustani)

        btnPotvrdi.setOnClickListener {
            deletePicture(imageUri)
            alertDialog.dismiss()
        }

        btnOtkazi.setOnClickListener { alertDialog.dismiss() }
        btnOdustani.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    private fun deletePicture(imageUri: Uri) {
        val uploadedIndex = uploadedPictures.indexOfFirst { it.first == imageUri }
        if (uploadedIndex != -1) {
            val photoId = uploadedPictures[uploadedIndex].second
            vmVehicle.deleteVehiclePhoto(photoId)
            uploadedPictures.removeAt(uploadedIndex)
        } else {
            newPictures.remove(imageUri)
        }
        updateAdapterData()
    }

    private fun savePhotos() {
        if (newPictures.isEmpty()) {
            Toast.makeText(requireContext(), "Niste dodali nove fotografije", Toast.LENGTH_SHORT).show()
            return
        }

        val vehicleId = vmVehicle.getVehicleId()
        if (vehicleId == null) {
            Toast.makeText(requireContext(), "Vozilo nije spremljeno", Toast.LENGTH_SHORT).show()
            return
        }

        newPictures.forEach { uri ->
            val file = getFileFromUri(uri)
            if (file != null) {
                vmVehicle.uploadAndConnectPhoto(vehicleId, file)
                uploadedPictures.add(uri to -1)
            } else {
                Toast.makeText(requireContext(), "Pogreška kod pretvaranja datoteke", Toast.LENGTH_SHORT).show()
            }
        }

        newPictures.clear()
        updateAdapterData()
        Toast.makeText(requireContext(), "Slike spremljene", Toast.LENGTH_SHORT).show()
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = createTempFile(requireContext())
        return try {
            inputStream?.copyTo(file.outputStream())
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun createTempFile(context: Context): File {
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        tempFile.createNewFile()
        return tempFile
    }

    private fun observePhotosResponse() {
        vmVehicle.photosResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    result.data?.let { photos ->
                        uploadedPictures.clear()
                        uploadedPictures.addAll(photos.map { Uri.parse(it.link) to it.id })
                        updateAdapterData()
                    } ?: Toast.makeText(requireContext(), "Neočekivan format datoteke", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Pogreška kod dohvaćanja fotografija", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeUploadResponse() {
        vmVehicle.uploadResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), result.data?.body() ?: "Fotografija uspješno poslana", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Pogreška kod slanja fotografije: ${result.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeConnectedResponse(){
        vmVehicle.connectedResponse.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    println("Slika povezana s vozilom.")
                }
                is Result.Error -> {
                    println("Greška kod povezivanja vozila i slike ${result.error}.")
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
