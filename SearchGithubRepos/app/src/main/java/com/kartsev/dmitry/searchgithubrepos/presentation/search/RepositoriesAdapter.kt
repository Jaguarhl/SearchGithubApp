package com.kartsev.dmitry.searchgithubrepos.presentation.search

import android.view.LayoutInflater
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.databinding.RepoItemBinding
import com.kartsev.dmitry.searchgithubrepos.presentation.common.DataBoundListAdapter
import com.kartsev.dmitry.searchgithubrepos.util.DispatcherExecutor
import kotlinx.coroutines.Dispatchers

class RepositoriesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    val viewModel: SearchRepoViewModel
) : DataBoundListAdapter<RepoData, RepoItemBinding>(
    DispatcherExecutor(Dispatchers.IO), REPO_COMPARATOR
) {
    override fun createBinding(parent: ViewGroup): RepoItemBinding {

        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.repo_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: RepoItemBinding, item: RepoData) {
        binding.observable = item
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoData>() {
            override fun areItemsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem.owner == newItem.owner
                    && oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: RepoData, newItem: RepoData): Boolean =
                oldItem.description == newItem.description
                    && oldItem.stars == newItem.stars
        }
    }
}