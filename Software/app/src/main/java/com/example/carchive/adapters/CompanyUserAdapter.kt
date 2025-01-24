package com.example.carchive.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.data.dto.UserDto

class CompanyUserAdapter(
    private var users: List<UserDto>,
    private val onUserClick: (UserDto) -> Unit
) : RecyclerView.Adapter<CompanyUserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userName: TextView = view.findViewById(R.id.tv_user_name)
        private val userEmail: TextView = view.findViewById(R.id.tv_user_email)
        private val userId: TextView = view.findViewById(R.id.tv_user_id)

        fun bind(user: UserDto) {
            userName.text = "${user.firstName} ${user.lastName}"
            userEmail.text = user.email
            userId.text = "ID: ${user.id}"

            itemView.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(userView)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun updateUsers(list: List<UserDto>) {
        users = list
        notifyDataSetChanged()
    }
}
