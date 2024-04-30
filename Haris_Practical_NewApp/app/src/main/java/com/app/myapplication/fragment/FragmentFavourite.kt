package com.app.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myapplication.NewsDetailActivity
import com.app.myapplication.databinding.FragmentHomeBinding
import com.app.myapplication.fragment.adapter.HomeAdapter
import com.app.myapplication.fragment.viewmodel.FavouriteViewModel
import com.app.myapplication.listener.NewsClickListener
import com.app.myapplication.model.ArticlesItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FragmentFavourite : Fragment(), NewsClickListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var favouriteAdapter: HomeAdapter
    val viewModel by viewModels<FavouriteViewModel>()

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

        favouriteAdapter = HomeAdapter(ArrayList(), this)

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = favouriteAdapter
        }

        viewModel.getAllFavouriteNews()
        observeNewsList()
        EventBus.getDefault().register(this)
    }

    private fun observeNewsList() {
        viewModel.stateListData.observe(this)
        {
            if (it.isNullOrEmpty()) {
                showErrorMessage("No Favourite News")
            } else {
                showNewsList(it)
            }
        }
    }

    private fun showErrorMessage(error: String) {
        binding.txtNoData.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE
        binding.txtNoData.text = error
    }

    private fun showNewsList(arrayList: ArrayList<ArticlesItem>) {
        binding.rvNews.visibility = View.VISIBLE
        binding.txtNoData.visibility = View.GONE
        favouriteAdapter.setData(arrayList)
    }

    override fun onNewsClicked(item: ArticlesItem) {
        startActivity(
            Intent(requireActivity(), NewsDetailActivity::class.java)
                .putExtra("data", item)
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateItem(publishAt: String) {
        viewModel.getAllFavouriteNews()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e("on hidded : ", "on hidden called ${hidden}")
        viewModel.getAllFavouriteNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}