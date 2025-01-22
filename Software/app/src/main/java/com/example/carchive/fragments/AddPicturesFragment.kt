package com.example.carchive.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.PicturesAdapter
import com.example.carchive.data.network.Result
import com.example.carchive.viewmodels.VehicleCatalogViewModel
import com.google.android.material.button.MaterialButton
import java.io.File

class AddPicturesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPictureButton: MaterialButton
    private lateinit var saveVehiclePhotos: MaterialButton
    private lateinit var adapter: PicturesAdapter
    private val pictures = mutableListOf<Uri>()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var vmVehicle: VehicleCatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_pictures, container, false)

        vmVehicle = ViewModelProvider(requireActivity()).get(VehicleCatalogViewModel::class.java)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = PicturesAdapter(pictures) { imageUri -> deletePicture(imageUri) }
        recyclerView.adapter = adapter

        addPictureButton = view.findViewById(R.id.btnDodajSliku)
        saveVehiclePhotos = view.findViewById(R.id.btnSpremiSlike)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    uploadPicture(imageUri)
                } else {
                    Toast.makeText(requireContext(), "Failed to select image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addPictureButton.setOnClickListener {
            openGallery()
        }

        saveVehiclePhotos.setOnClickListener {
            savePhotos()
        }

        observeUploadResponse()

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun uploadPicture(imageUri: Uri) {
        if (imageUri.scheme == "content") {
            try {
                val file = getFileFromUri(imageUri)
                if (file != null) {
                    pictures.add(imageUri)
                    adapter.notifyItemInserted(pictures.size - 1)
                } else {
                    Toast.makeText(requireContext(), "Failed to convert Uri to File", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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


    private fun deletePicture(imageUri: Uri) {
        val position = pictures.indexOf(imageUri)
        if (position != -1) {
            pictures.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun observeUploadResponse() {
        vmVehicle.uploadResponse.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    val message = result.data?.body()
                    Toast.makeText(requireContext(), message ?: "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                    val filePath = message
                    val vehicleId = vmVehicle.getVehicleId()

                    if (vehicleId != null && filePath != null) {
                        vmVehicle.connectVehicleToPhoto(vehicleId, filePath)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Failed to upload image: ${result.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun savePhotos() {
        if (pictures.isNotEmpty()) {
            val vehicleId = vmVehicle.getVehicleId()
            if (vehicleId == null) {
                Toast.makeText(requireContext(), "Vehicle ID not set", Toast.LENGTH_SHORT).show()
                return
            }

            for (uri in pictures) {
                val file = getFileFromUri(uri)
                if (file != null) {
                    vmVehicle.uploadPhoto(file)

                    vmVehicle.uploadResponse.observe(viewLifecycleOwner, { result ->
                        when (result) {
                            is Result.Success -> {
                                val filePath = result.data?.body()
                                if (filePath != null) {
                                    vmVehicle.connectVehicleToPhoto(vehicleId, filePath)
                                } else {
                                    Toast.makeText(requireContext(), "Failed to retrieve file path", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Result.Error -> {

                                Toast.makeText(requireContext(), "Failed to upload image: ${result.error.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(requireContext(), "Failed to convert Uri to File", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "No photos to upload", Toast.LENGTH_SHORT).show()
        }
    }


}



