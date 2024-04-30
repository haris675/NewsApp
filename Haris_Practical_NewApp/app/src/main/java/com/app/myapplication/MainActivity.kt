package com.app.myapplication

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.myapplication.databinding.ActivityMainBinding
import com.app.myapplication.fragment.FragmentFavourite
import com.app.myapplication.fragment.FragmentHome
import com.app.myapplication.utils.ConnectivityReceiver
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    lateinit var binding: ActivityMainBinding

    val fragment1: Fragment = FragmentHome()
    val fragment2: Fragment = FragmentFavourite()

    lateinit var fragmentManager: FragmentManager
    var currentFragment: Fragment? = null
    var doubleBackToExitPressedOnce = false

    lateinit var conReceiver: ConnectivityReceiver
    var networkChangeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = getSupportFragmentManager();

        setFragment(fragment1, "1", 0);

        binding.navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        conReceiver = ConnectivityReceiver()
        ConnectivityReceiver.connectivityReceiverListener = this
        networkChangeCount = 0
        registerReceiver(conReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun setFragment(fragment: Fragment, tag: String?, position: Int) {
        if (fragment.isAdded) {
            fragmentManager.beginTransaction().hide(currentFragment!!).show(fragment).commit()
        } else if (currentFragment != null) {
            fragmentManager.beginTransaction().hide(currentFragment!!)
                .add(R.id.frame_container, fragment, tag).commit()
        } else {
            fragmentManager.beginTransaction().add(R.id.frame_container, fragment).commit()
        }
        binding.navigation.getMenu().getItem(position).setChecked(true)
        currentFragment = fragment
    }

    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            val fragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> {
                    setFragment(fragment1, "1", 0);
                    return@OnItemSelectedListener true
                }
                R.id.navigation_favourite -> {
                    setFragment(fragment2, "2", 1);
                    return@OnItemSelectedListener true
                }
            }
            return@OnItemSelectedListener false
        }


    override fun onBackPressed() {
        if (currentFragment === fragment1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        } else {
            setFragment(fragment1, "1", 0)
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (networkChangeCount > 0) {
            if (isConnected) {
                Toast.makeText(this, "Internet is connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Internet is gone", Toast.LENGTH_SHORT).show()
            }
        }
        networkChangeCount++
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(conReceiver)
    }

}