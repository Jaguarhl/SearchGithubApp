package com.kartsev.dmitry.searchgithubrepos.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.binding.FragmentDataBindingComponent
import com.kartsev.dmitry.searchgithubrepos.databinding.FragmentSearchRepoBinding
import com.kartsev.dmitry.searchgithubrepos.di.Injectable
import com.kartsev.dmitry.searchgithubrepos.presentation.adapter.RepositoriesAdapter
import com.kartsev.dmitry.searchgithubrepos.util.autoCleared
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_search_repo.*
import javax.inject.Inject

class SearchRepoFragment : Fragment(), HasSupportFragmentInjector, Injectable {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSearchRepoBinding>()

    var adapter by autoCleared<RepositoriesAdapter>()

    lateinit var searchRepoViewModel: SearchRepoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_repo, container,
            false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRepoViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchRepoViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = searchRepoViewModel

        initRecyclerList()
    }

    private fun initRecyclerList() {
        adapter = RepositoriesAdapter()
        with(fragmentSearchRepoResultsList) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapter
        }
    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector
}