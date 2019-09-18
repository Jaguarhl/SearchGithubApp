package com.kartsev.dmitry.searchgithubrepos.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.binding.FragmentDataBindingComponent
import com.kartsev.dmitry.searchgithubrepos.databinding.FragmentRepoDetailsBinding
import com.kartsev.dmitry.searchgithubrepos.di.Injectable
import com.kartsev.dmitry.searchgithubrepos.presentation.search.SearchRepoFragment.Companion.REPO_ID
import com.kartsev.dmitry.searchgithubrepos.presentation.search.SearchRepoFragment.Companion.REPO_OWNER
import com.kartsev.dmitry.searchgithubrepos.util.autoCleared
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_repo_details.*
import javax.inject.Inject

class RepoDetailsFragment : Fragment(), HasSupportFragmentInjector, Injectable {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentRepoDetailsBinding>()
    private lateinit var repoDetailsViewModel: RepoDetailsViewModel

    private val params by navArgs<RepoDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_repo_details, container,
            false, dataBindingComponent
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repoDetailsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RepoDetailsViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@RepoDetailsFragment
            viewModel = repoDetailsViewModel
        }

        arguments?.apply {
            val repoId = getInt(REPO_ID)
            val repoOwner = getString(REPO_OWNER) ?: ""

            repoDetailsViewModel.initializeByDetails(repoId, repoOwner)
        }

        initLiveDataObservers()
        initListeners()
    }

    private fun initListeners() {
        fragmentRepoDetailsBtnReturn.setOnClickListener {
//            NavHostFragment.findNavController(this)
//                .popBackStack(R.id.action_searchRepoFragment_to_repoDetailsFragment, true)
        }
    }

    private fun initLiveDataObservers() {
        repoDetailsViewModel.repoDetailsStateLiveData.observe(this, Observer {
            when (it) {
                is Running -> fragmentRepoDetailsProgressLayout.visibility = View.VISIBLE
                is Successful -> fragmentRepoDetailsProgressLayout.visibility = View.GONE
                is Failed -> Snackbar.make(rootView, it.message, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun navController() = NavHostFragment.findNavController(this)

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector
}