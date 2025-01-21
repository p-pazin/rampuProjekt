package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.data.dto.ContractDto

class ContractsAdapter(
    var contractsData : List<ContractDto>,
    private val onContractClick:(ContractDto) -> Unit) : RecyclerView.Adapter<ContractsAdapter.ContractViewHolder>() {

        inner class ContractViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val contractName: TextView
        private val ID: TextView
        private val contractType: TextView
        private val contractContact: TextView
        private val contractDateOfCreation: TextView
        private val contractSigned: TextView
        private val contractSignedIcon: ImageView

        init {
            contractName = view.findViewById(R.id.tv_contract_name)
            ID = view.findViewById(R.id.tv_contract_id)
            contractType = view.findViewById(R.id.tv_contract_type)
            contractContact = view.findViewById(R.id.tv_contract_contact_name)
            contractDateOfCreation = view.findViewById(R.id.tv_contract_date_created)
            contractSigned = view.findViewById(R.id.tv_contract_signed)
            contractSignedIcon = view.findViewById(R.id.iv_contract_signed)
        }
        fun bind(contract : ContractDto) {
            contractName.text = contract.title
            ID.text = "ID: " + contract.id
            if(contract.type == 1) {
                contractType.text = "Kupoprodajni"
            }
            if(contract.type == 2) {
                contractType.text = "Najmodavni"
            }
            contractContact.text = contract.contactName
            contractDateOfCreation.text = contract.dateOfCreation
            if(contract.signed == 1) {
                contractSignedIcon.setImageResource(R.drawable.ic_aktivan_kontakt)
                contractSigned.text = "Potpisan"
            }
            else {
                contractSignedIcon.setImageResource(R.drawable.ic_neaktivan_kontakt)
                contractSigned.text = "Nije potpisan"
            }

            itemView.setOnClickListener(){
                onContractClick(contract)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractsAdapter.ContractViewHolder {
        val contactView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contract_list_item, parent, false)
        return ContractViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return contractsData.size
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        holder.bind(contractsData[position])
    }

    fun updateItems(list: List<ContractDto>){
        contractsData = list
        notifyDataSetChanged()
    }
}