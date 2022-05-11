package com.sliide.technicaltaskandroid.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sliide.technicaltaskandroid.databinding.ItemUserListBinding

class UserListAdapter constructor(
    private val characterClicked: (UserListViewState) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserItemViewHolder>() {

    private var list = mutableListOf<UserListViewState>()

    fun updateListContents(
        newList: List<UserListViewState>
    ) {
        list = mutableListOf()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserItemViewHolder(
            binding,
            characterClicked
        )
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    class UserItemViewHolder constructor(
        private val binding: ItemUserListBinding,
        private val characterClicked: (UserListViewState) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            data: UserListViewState
        ) {
            binding.tvNameDescription.text = data.name
            binding.tvGenderDescription.text = data.gender
            binding.tvEmailDescription.text = data.email

            binding.cvUserListItem.setOnLongClickListener {
                characterClicked(data)
                true
            }
        }
    }
}