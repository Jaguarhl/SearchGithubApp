package com.kartsev.dmitry.searchgithubrepos.presentation.adapter

import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.data.database.RepoData
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * View Holder for a [RepoData] RecyclerView list item.
 */
class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val repoName: TextView = view.findViewById(R.id.textRepoName)
    private val repoStars: TextView = view.findViewById(R.id.repoStars)
    private val repoDescriptionLabel: TextView = view.findViewById(R.id.textRepoDescriptionLabel)
    private val repoDescription: TextView = view.findViewById(R.id.textRepoDescription)
    private val repoLanguage: TextView = view.findViewById(R.id.textRepoLanguage)
    private val repoLanguageLabel: TextView = view.findViewById(R.id.textRepoLanguageLabel)
    private val repoForks: TextView = view.findViewById(R.id.repoForks)

    private var repo: RepoData? = null

    init {
        view.setOnClickListener {
            //            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
        }
    }

    fun bind(repo: RepoData?) {
        if (repo == null) {
            val resources = itemView.resources
            repoName.text = resources.getString(R.string.loading)
            repoLanguage.visibility = View.GONE
            repoStars.text = resources.getString(R.string.unknown)
            repoForks.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: RepoData) {
        this.repo = repo
        repoName.text = repo.fullName

        // if the description is missing, hide the TextView

        repoStars.text = repo.stars.toString()
        repoForks.text = repo.forks.toString()

        (if (repo.language.isNullOrEmpty()) View.VISIBLE else View.GONE).also {
            repoLanguage.visibility = it
            repoLanguageLabel.visibility = it
        }
        repoLanguage.text = repo.language

        (if (repo.description.isNullOrEmpty()) View.VISIBLE else View.GONE).also {
            repoDescription.visibility = it
            repoDescriptionLabel.visibility = it
        }
        repoDescription.text = repo.description
    }

    companion object {
        fun create(parent: ViewGroup): RepositoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_item, parent, false)
            return RepositoryViewHolder(view)
        }
    }
}
