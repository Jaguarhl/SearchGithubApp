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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kartsev.dmitry.searchgithubrepos.MainNavDirections
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.binding.FragmentDataBindingComponent
import com.kartsev.dmitry.searchgithubrepos.databinding.FragmentRepoDetailsBinding
import com.kartsev.dmitry.searchgithubrepos.di.Injectable
import com.kartsev.dmitry.searchgithubrepos.util.autoCleared
import kotlinx.android.synthetic.main.fragment_repo_details.*
import javax.inject.Inject

class RepoDetailsFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentRepoDetailsBinding>()
    private lateinit var repoDetailsViewModel: RepoDetailsViewModel
    private val args by navArgs<RepoDetailsFragmentArgs>()

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
            lifecycleOwner = viewLifecycleOwner
            viewModel = repoDetailsViewModel
        }

        args.apply {
            val repoId = id
            val repoOwner = owner

            repoDetailsViewModel.initializeByDetails(repoId, repoOwner)
        }

        initLiveDataObservers()
        initListeners()
    }

    private fun initListeners() {
        fragmentRepoDetailsBtnReturn.setOnClickListener {
//            navController().navigate(MainNavDirections.globalToSearchRepoFragment())
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

    private fun navController() = findNavController()
}