package com.example.carchive.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.adapters.ExpandableAdapter
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.network.Result
import com.example.carchive.databinding.FragmentContractDetailsBinding
import com.example.carchive.entities.ExpandableItem
import com.example.carchive.viewmodels.ContractsViewModel

class ContractDetailsFragment : Fragment() {

    private val viewModel : ContractsViewModel by viewModels()
    private lateinit var tvTitle : TextView
    private lateinit var tvPlace: TextView
    private lateinit var tvDate : TextView
    private lateinit var tvPrice : TextView
    private lateinit var tvRegistration : TextView
    private lateinit var tvBrand : TextView
    private lateinit var tvModel : TextView
    private lateinit var tvContactName : TextView
    private lateinit var btnOpenPdf : Button
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableAdapter
    private var _binding: FragmentContractDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var contractId = arguments?.getInt("contract_id")
        var contractType = arguments?.getInt("contract_type")

        if(contractId == null || contractType == null) {
            return binding.root
        }

        viewModel.fetchContractById(contractId, contractType)

        _binding = FragmentContractDetailsBinding.inflate(inflater, container, false)

        binding.navBackButton.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        recyclerView = binding.rvContractDetails

        val itemList = mutableListOf<ExpandableItem>()

        tvTitle = binding.tvContractDetailsTitle
        tvPlace = binding.tvContractDetailsPlace
        tvDate = binding.tvContractDetailsDateOfCreation
        tvPrice = binding.tvContractDetailsPrice
        tvRegistration = binding.tvContractDetailsRegistration
        tvBrand = binding.tvContractDetailsVehicleBrand
        tvModel = binding.tvContractDetailsVehicleModel
        tvContactName = binding.tvContractDetailsContactName
        btnOpenPdf = binding.btnContractDetailsDownload
        btnEdit = binding.btnContractDetailsEdit
        btnDelete = binding.btnContractDetailsDelete

        adapter = ExpandableAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.contractSale.observe(viewLifecycleOwner, { contract ->
                tvTitle.text = "Naslov: ${contract?.title}"
                tvPlace.text = "Mjesto: ${contract?.place}"
                tvDate.text = "Datum kreiranja: ${contract?.dateOfCreation}"
                tvPrice.text = "Cijena: ${contract?.price}"
                tvContactName.text = "Kupac: ${contract?.contactName}"

                if(contract?.signed == 1) {
                    btnEdit.visibility = View.GONE
                } else if(contract?.signed == 0) {
                    btnEdit.visibility = View.VISIBLE
                }

                if(contract?.vehicles != null && contract.vehicles.isNotEmpty()) {
                    tvRegistration.visibility = View.GONE
                    tvBrand.visibility = View.GONE
                    tvModel.visibility = View.GONE
                    itemList.clear()
                    for (vehicle in contract.vehicles) {
                        itemList.add(
                            ExpandableItem(
                                (contract.vehicles.indexOf(vehicle) + 1).toString(),
                                "${vehicle.brand} ${vehicle.model}",
                                vehicle.registration
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    tvRegistration.text = "Registracija vozila: ${contract?.vehicle?.registration}"
                    tvBrand.text = "Marka vozila: ${contract?.vehicle?.brand}"
                    tvModel.text = "Model vozila: ${contract?.vehicle?.model}"
                }

                btnOpenPdf.setOnClickListener {
                    if(contract != null) {
                        viewModel.createPdfAndOpen(requireContext(), contract)
                    }
                }

                btnEdit.setOnClickListener {
                    if(contract != null) {
                        val bundle = Bundle().apply {
                            putSerializable("contract_sale_key", contract)
                        }
                        findNavController().navigate(R.id.action_contractDetailsFragment_to_editContractSaleFragment, bundle)
                    }
                }

                btnDelete.setOnClickListener {
                    if(contract != null) {
                        showDeleteWarningDialog(contract.id)
                    }
                }
        })

        viewModel.contractRent.observe(viewLifecycleOwner, { contract ->
                tvTitle.text = "Naslov: ${contract?.title}"
                tvPlace.text = "Mjesto: ${contract?.place}"
                tvDate.text = "Datum kreiranja: ${contract?.dateOfCreation}"
                tvPrice.text = "Cijena: ${contract?.price}"
                tvRegistration.text = "Registracija vozila: ${contract?.registration}"
                tvBrand.text = "Marka vozila: ${contract?.brand}"
                tvModel.text = "Model vozila: ${contract?.model}"
                tvContactName.text = "Kupac: ${contract?.firstNameContact + " " + contract?.lastNameContact}"

                if(contract?.signed == 1) {
                    btnEdit.visibility = View.GONE
                } else if(contract?.signed == 0) {
                    btnEdit.visibility = View.VISIBLE
                }

                btnOpenPdf.setOnClickListener {
                    if(contract != null) {
                        viewModel.createPdfAndOpen(requireContext(), contract)
                    }
                }

                btnEdit.setOnClickListener {
                    if(contract != null) {
                        val bundle = Bundle().apply {
                            putSerializable("contract_rent_key", contract)
                        }
                        findNavController().navigate(R.id.action_contractDetailsFragment_to_editContractRentFragment, bundle)
                    }
                }

                btnDelete.setOnClickListener {
                    if(contract != null) {
                       showDeleteWarningDialog(contract.id)
                    }
                }
        })

        return binding.root
    }

    private fun showDeleteWarningDialog(contractId: Int) {
        val deletionWarning = LayoutInflater.from(context).inflate(R.layout.deleting_warning, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(deletionWarning)
            .create()

        val btnPotvrdi = deletionWarning.findViewById<Button>(R.id.btnPotvrdi)
        val btnOtkazi = deletionWarning.findViewById<ImageButton>(R.id.btnOtkazi)
        val btnOdustani = deletionWarning.findViewById<Button>(R.id.btnOdustani)
        val tvWarning = deletionWarning.findViewById<TextView>(R.id.tvWarning)

        tvWarning.text = "Jeste li sugurni da želite obrisati ugovor?"

        btnPotvrdi.setOnClickListener {
            viewModel.deleteContract(contractId)

            viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
                when(result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), getString(R.string.ugovorObrisan), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_contractDetailsFragment_to_contractsFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.greskaKodBrisanjaUgovora), Toast.LENGTH_SHORT).show()
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

}