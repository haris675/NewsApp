package com.app.myapplication

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import com.app.myapplication.databinding.ActivityNewsDetailBinding
import com.app.myapplication.model.ArticlesItem
import com.app.myapplication.utils.Extension.errorSnackBar
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class NewsDetailActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityNewsDetailBinding
    lateinit var newItem: ArticlesItem
    private var isFavourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getParcelableExtra<ArticlesItem>("data") != null) {
            newItem = intent.getParcelableExtra<ArticlesItem>("data")!!
            setDataToView()
            findFavourite()
        }

        binding.btnFavourite.setOnClickListener(this)
    }

    private fun findFavourite() {
        lifecycleScope.launch {
            val favourite = App.database.favouriteDao().search(newItem.publishedAt!!);
            if (!favourite.isNullOrEmpty()) {
                isFavourite = true
                binding.btnFavourite.setText("Remove from Favourite")
            } else {
                isFavourite = false
                binding.btnFavourite.setText("Mark as Favourite")
            }
        }
    }

    private fun setDataToView() {
        with(binding) {
            imgNew.load(newItem.urlToImage)
            txtTitle.text = newItem.title
            txtDescription.text = newItem.description + "\n" + newItem.content
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnFavourite -> {
                insertItem()
            }
        }
    }

    private fun insertItem() {
        lifecycleScope.launch {
            if (isFavourite) {
                removeFromFavourite()
                return@launch
            }
            App.database.favouriteDao().insertUser(newItem)
            binding.root.errorSnackBar("Mark as Favourite")
            findFavourite()
            EventBus.getDefault().post(newItem.publishedAt)
        }
    }

    private fun removeFromFavourite() {
        lifecycleScope.launch {
            App.database.favouriteDao().deleteById(newItem.publishedAt!!)
            binding.root.errorSnackBar("Removed from Favourite")
            findFavourite()
            EventBus.getDefault().post(newItem.publishedAt)
        }
    }

}