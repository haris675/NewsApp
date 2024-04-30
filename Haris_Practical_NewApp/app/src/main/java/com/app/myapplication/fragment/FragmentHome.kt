package com.app.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myapplication.MainActivity
import com.app.myapplication.NewsDetailActivity
import com.app.myapplication.R
import com.app.myapplication.databinding.FragmentHomeBinding
import com.app.myapplication.fragment.adapter.HomeAdapter
import com.app.myapplication.fragment.viewmodel.HomeViewModel
import com.app.myapplication.listener.NewsClickListener
import com.app.myapplication.model.ArticlesItem
import com.app.myapplication.utils.AppState
import com.app.myapplication.utils.Extension.errorSnackBar
import com.app.myapplication.utils.Extension.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentHome : Fragment(), NewsClickListener {

    lateinit var binding: FragmentHomeBinding
    val viewModel by viewModels<HomeViewModel>()
    lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter(ArrayList(), this)

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = homeAdapter
        }

        lifecycleScope.launch {
            delay(250)
            getNewsData()
        }
        observeNewsData()
    }

    private fun getNewsData() {
        if (requireActivity().isNetworkConnected()) {
            viewModel.getNewsApi()
        } else {
            binding.root.errorSnackBar(resources.getString(R.string.no_internet))
            showErrorMessage(resources.getString(R.string.no_internet))
        }
    }

    private fun observeNewsData() {
        viewModel.stateListData.observe(this) {
            when (it) {
                is AppState.Loading -> {
                    loading(true)
                }

                is AppState.Error -> {
                    loading(false)
                    binding.txtNoData.text = it.error
                    binding.txtNoData.visibility = View.VISIBLE
                    binding.rvNews.visibility = View.GONE
                }

                is AppState.Success -> {
                    loading(false)

                    if (it.model.status != "ok") {
                        showErrorMessage("Something went wrong")
                        return@observe
                    }
                    if (it.model.articles == null) {
                        showErrorMessage("no news found")
                        return@observe
                    }
                    if (it.model.articles.isEmpty()) {
                        showErrorMessage("No news found")
                        return@observe
                    }

                    homeAdapter.setData(it.model.articles)
                    binding.txtNoData.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showErrorMessage(error: String) {
        binding.txtNoData.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE
        binding.txtNoData.text = error
    }

    private fun loading(vale: Boolean) {
        (requireActivity() as MainActivity).isLoading(vale)
    }

    override fun onNewsClicked(item: ArticlesItem) {
        startActivity(
            Intent(requireActivity(), NewsDetailActivity::class.java)
                .putExtra("data", item)
        )
    }

}