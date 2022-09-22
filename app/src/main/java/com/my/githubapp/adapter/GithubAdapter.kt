package com.my.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.githubapp.R
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.databinding.GithubCardBinding
import java.util.ArrayList

class GithubAdapter(private val listUser: ArrayList<SimpleUser>) : RecyclerView.Adapter<GithubAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = GithubCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val users = listUser[position]
        holder.binding.apply {
            usernameText.text = users.login
            Glide
                .with(holder.itemView.context)
                .load(users.avatarUrl)
                .placeholder(R.drawable.ic_baseline_person)
                .into(imageProfile)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: GithubCardBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: SimpleUser)
    }
}
