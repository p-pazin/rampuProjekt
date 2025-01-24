package com.example.carchive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.CarchiveActivity
import com.example.carchive.databinding.UserListItemBinding
import com.example.carchive.R
import com.example.carchive.data.dto.UserDto
import com.example.carchive.databinding.DetailActionsUserBinding
import com.example.carchive.viewmodels.CompanyUserViewModel

class UsersFromCompanyAdapter(
    private var users: List<UserDto>,
    private val context: Context,
    private val onDeleteUser: (UserDto) -> Unit
) : RecyclerView.Adapter<UsersFromCompanyAdapter.UserViewHolder>() {

    class UserViewHolder(
        private val binding: UserListItemBinding,
        private val context: Context,
        private val onDeleteUser: (UserDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDto) {
            binding.tvUserName.text = "${user.firstName} ${user.lastName}"
            binding.tvUserEmail.text = user.email
            binding.tvUserId.text = "ID: ${user.id}"

            binding.imgBtnDetails.setOnClickListener {
                showPopup(binding.imgBtnDetails, user)
            }
        }

        private fun showPopup(view: View, user: UserDto) {
            val popupBinding = DetailActionsUserBinding.inflate(LayoutInflater.from(context))
            val popupWindow = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            popupBinding.btnUredi.setOnClickListener {
                popupWindow.dismiss()
                (context as? CarchiveActivity)?.let { activity ->
                    val companyUserViewModel: CompanyUserViewModel by activity.viewModels()
                    companyUserViewModel.setUser(user)

                    activity.findNavController(R.id.nav_host_fragment).navigate(R.id.action_usersFromCompanyFragment_to_editUserInfoFragment)
                }
            }

            popupBinding.btnObrisi.setOnClickListener {
                popupWindow.dismiss()
                showDeleteWarning(user)
            }

            popupWindow.showAsDropDown(view, 0, 0)
        }


        private fun showDeleteWarning(user: UserDto) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.user_delete_warning, null)
            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val btnPotvrdi = dialogView.findViewById<Button>(R.id.btnPotvrdi)
            val btnOdustani = dialogView.findViewById<Button>(R.id.btnOdustani)
            val btnOtkazi = dialogView.findViewById<ImageButton>(R.id.btnOtkazi)

            btnPotvrdi.setOnClickListener {
                onDeleteUser(user)
                alertDialog.dismiss()
            }

            btnOdustani.setOnClickListener { alertDialog.dismiss() }
            btnOtkazi.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, context, onDeleteUser)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<UserDto>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
