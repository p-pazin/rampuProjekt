package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.entities.Vehicle

class InvoiceAdapter (
    private var invoices: List<InvoiceDto>
    ) : RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

        inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvId: TextView = itemView.findViewById(R.id.tv_invoice_Id)
            private val tvDate: TextView = itemView.findViewById(R.id.tv_date_invoice)
            private val tvPayment: TextView = itemView.findViewById(R.id.tv_invoice_payment_method)
            private val tvPrice: TextView = itemView.findViewById(R.id.tv_invoice_price)
            private val tvVat: TextView = itemView.findViewById(R.id.tv_vat)
            private val tvMileage: TextView = itemView.findViewById(R.id.tv_mileage)

            fun bind(invoice: InvoiceDto) {
                tvId.text = "ID: ${invoice.id?.toString()}"
                tvDate.text = "${invoice.dateOfCreation}"
                tvPayment.text = "Način plaćanja: ${invoice.paymentMethod?.toString()}"
                tvPrice.text = "${invoice.totalCost?.toString()} €"
                tvVat.text = "PDV: ${invoice.vat?.toString()}"
                tvMileage.text = "Kilometraža: ${invoice.mileage.toString()}"
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(invoices[position])
    }
    fun updateItems(newInvoices: List<InvoiceDto>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = invoices.size
            override fun getNewListSize() = newInvoices.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return invoices[oldItemPosition].id == newInvoices[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return invoices[oldItemPosition] == newInvoices[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        invoices = newInvoices
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount(): Int = invoices.size
}