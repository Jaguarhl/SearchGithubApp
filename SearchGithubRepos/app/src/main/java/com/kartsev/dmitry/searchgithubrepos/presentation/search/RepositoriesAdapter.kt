package com.kartsev.dmitry.searchgithubrepos.presentation.search

import android.view.LayoutInflater
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.databinding.RepoItemBinding

class RepositoriesAdapter(
    val viewModel: SearchRepoViewModel
) : ListAdapter<RepoData, RepositoryViewHolder>(
    REPO_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, R.layout.repo_item, parent, false
        )

        return RepositoryViewHolder(binding as RepoItemBinding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, viewModel) }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoData>() {
            override fun areItemsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem.name == newItem.name
                    && oldItem.owner == newItem.owner

            override fun areContentsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem.description == newItem.description
                    && oldItem.fullName == newItem.fullName
        }
    }
}