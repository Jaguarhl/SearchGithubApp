package com.kartsev.dmitry.searchgithubrepos.presentation.search

import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import androidx.recyclerview.widget.RecyclerView
import com.kartsev.dmitry.searchgithubrepos.databinding.ItemRepoBinding

/**
 * View Holder for a [RepoData] RecyclerView list item.
 */
class RepositoryViewHolder(private val binding: ItemRepoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: RepoData?, viewModel: SearchRepoViewModel) {
        if (repo != null) {
            binding.observable = repo
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}
