package com.kartsev.dmitry.searchgithubrepos.presentation.adapter

import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RepositoriesAdapter : ListAdapter<RepoData, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepositoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as RepositoryViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoData>() {
            override fun areItemsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem == newItem
        }
    }
}