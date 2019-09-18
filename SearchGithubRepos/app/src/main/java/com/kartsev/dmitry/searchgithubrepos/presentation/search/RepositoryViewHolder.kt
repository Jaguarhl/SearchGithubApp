package com.kartsev.dmitry.searchgithubrepos.presentation.search

import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import androidx.recyclerview.widget.RecyclerView
import com.kartsev.dmitry.searchgithubrepos.databinding.RepoItemBinding
import com.kartsev.dmitry.searchgithubrepos.presentation.search.SearchRepoViewModel

/**
 * View Holder for a [RepoData] RecyclerView list item.
 */
class RepositoryViewHolder(private val binding: RepoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: RepoData?, viewModel: SearchRepoViewModel) {
        if (repo != null) {
            binding.observable = repo
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}
