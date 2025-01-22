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
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import com.example.carchive.databinding.FragmentAddPicturesBinding
import java.io.File

class AddPicturesFragment : Fragment() {

    private var _binding: FragmentAddPicturesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PicturesAdapter
    private val pictures = mutableListOf<Uri>()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var vmVehicle: VehicleCatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPicturesBinding.inflate(inflater, container, false)

        vmVehicle = ViewModelProvider(requireActivity()).get(VehicleCatalogViewModel::class.java)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = PicturesAdapter(pictures) { imageUri ->
            showDeleteWarningDialog(imageUri)
        }
        binding.recyclerView.adapter = adapter

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    uploadPicture(imageUri)
                } else {
                    Toast.makeText(requireContext(), "Pogreška kod odabiranja fotografije", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDodajSliku.setOnClickListener {
            openGallery()
        }

        binding.btnSpremiSlike.setOnClickListener {
            savePhotos()
        }

        observePhotosResponse()
        observeUploadResponse()

        vmVehicle.newPictures.observe(viewLifecycleOwner) { newPics ->
            pictures.clear()
            pictures.addAll(newPics)
            adapter.notifyDataSetChanged()
        }

        val vehicleId = vmVehicle.getVehicleId()
        if (vehicleId != null) {
            vmVehicle.getVehiclePhotos(vehicleId)
        }

        return binding.root
    }

    private fun uploadPicture(imageUri: Uri) {
        vmVehicle.addNewPicture(imageUri)

        if (!pictures.contains(imageUri)) {
            pictures.add(imageUri)
            adapter.notifyItemInserted(pictures.size - 1)

            val file = getFileFromUri(imageUri)
            if (file != null) {
                vmVehicle.uploadPhoto(file)
            } else {
                Toast.makeText(requireContext(), "Pogreška kod pretvaranja datoteke", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePhotos() {
        val newPictures = vmVehicle.newPictures.value ?: return

        if (newPictures.isNotEmpty()) {
            val vehicleId = vmVehicle.getVehicleId()
            if (vehicleId == null) {
                Toast.makeText(requireContext(), "Vozilo nije spremljeno", Toast.LENGTH_SHORT).show()
                return
            }

            newPictures.forEach { uri ->
                val filePath = uri.toString()
                vmVehicle.connectVehicleToPhoto(vehicleId, filePath)
            }

            vmVehicle.clearNewPictures()
            Toast.makeText(requireContext(), "Slika spremljena", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Niste dodali nove fotografije", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
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

        btnOtkazi.setOnClickListener {
            alertDialog.dismiss()
        }

        btnOdustani.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun deletePicture(imageUri: Uri) {
        val position = pictures.indexOf(imageUri)
        if (position != -1) {
            pictures.removeAt(position)
            vmVehicle.newPictures.value?.remove(imageUri)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = createTempFile(requireContext())
        try {
            inputStream?.copyTo(file.outputStream())
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
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
                    val photos = result.data
                    if (photos != null) {
                        val photoUris = photos.map { Uri.parse(it.link) }
                        pictures.clear()
                        pictures.addAll(photoUris)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Neočekivan format datoteke", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Pogreška kod dohvaćanja fotografija: ${result.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeUploadResponse() {
        vmVehicle.uploadResponse.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    val message = result.data?.body()
                    Toast.makeText(requireContext(), message ?: "Fotografija uspješno poslana", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Pogreška kod slanja fotografije: ${result.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
