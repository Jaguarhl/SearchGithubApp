package com.kartsev.dmitry.searchgithubrepos.presentation.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kartsev.dmitry.searchgithubrepos.R
import com.kartsev.dmitry.searchgithubrepos.binding.FragmentDataBindingComponent
import com.kartsev.dmitry.searchgithubrepos.databinding.FragmentSearchRepoBinding
import com.kartsev.dmitry.searchgithubrepos.di.Injectable
import com.kartsev.dmitry.searchgithubrepos.util.autoCleared
import com.kartsev.dmitry.searchgithubrepos.util.hideKeyboard
import dagger.android.DispatchingAndroidInjector
import kotlinx.android.synthetic.main.fragment_search_repo.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class SearchRepoFragment : Fragment(), Injectable {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentSearchRepoBinding>()
    private var listAdapter by autoCleared<RepositoriesAdapter>()
    lateinit var searchRepoViewModel: SearchRepoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_repo, container,
            false, dataBindingComponent
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchRepoViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SearchRepoViewModel::class.java)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = searchRepoViewModel
        }

        initRecyclerList()
        setupScrollListener()
        initLiveDataObservers()
        val query = savedInstanceState?.getString(LAST_QUERY_STRING) ?: ""
        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragmentSearchRepoInput?.let {
            outState.putString(LAST_QUERY_STRING, it.text.toString())
        }
    }

    private fun initSearch(query: String) {
        with(fragmentSearchRepoInput) {
            setText(query)

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    runSearch()
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    runSearch()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun EditText.runSearch() {
        searchRepoViewModel.setQuery(text.trim().toString().toLowerCase(Locale.getDefault()))
        activity?.hideKeyboard()
    }

    private fun initLiveDataObservers() {
        searchRepoViewModel.searchStateLiveData.observe(this, Observer {
            when (it) {
                is Running -> updateViews(true, it.firstRequest)
                is Success -> updateViews(false, it.firstRequest)
            }
        })

        searchRepoViewModel.searchUIEvents.observe(this, Observer {
            when (it) {
                is Failed -> Snackbar.make(rootView, it.message, Snackbar.LENGTH_LONG).show()
                is ShowDetailsAction -> navController()
                    .navigate(SearchRepoFragmentDirections.showRepoDetails(it.id, it.owner))
            }
        })

        searchRepoViewModel.searchResultLiveData.observe(this, Observer {
            Timber.d("Submit to adapter ${it.size} items.")
            listAdapter.submitList(it)
        })
    }

    private fun updateViews(visible: Boolean, firstRequest: Boolean) {
        val flag = if (visible) View.VISIBLE else View.GONE

        flag.also {
            if (firstRequest) {
                fragmentSearchRepoProgressLayout.visibility = it
                fragmentSearchRepoResultsList.scrollToPosition(0)
            } else {
                fragmentSearchRepoProgressMoreLayout.visibility = it
            }
        }
    }

    private fun initRecyclerList() {
        listAdapter = RepositoriesAdapter(
            searchRepoViewModel
        )
        with(fragmentSearchRepoResultsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    private fun setupScrollListener() {
        fragmentSearchRepoResultsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) return

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == listAdapter.itemCount - 1) {
                    searchRepoViewModel.loadNextPage()
                }
            }
        })
    }

    private fun navController() = findNavController()

    companion object {
        const val LAST_QUERY_STRING = "LAST_QUERY_STRING"
    }
}