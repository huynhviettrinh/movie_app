package com.example.appmovieandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.appmovieandroid.databinding.ActivityMainBinding
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.common.MoreFeature
import com.example.appmovieandroid.models.view_model.FragmentViewModel
import com.example.appmovieandroid.models.MovieCategory
import com.example.appmovieandroid.models.view_model.ConstViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private val scopeMain = CoroutineScope(Dispatchers.Main)
    private val fragmentViewModel: FragmentViewModel by viewModels()
    private val viewModels: ConstViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        var a: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

//        binding.animationView.setAnimation("netflix-logo-swoop.json")
        setStoreViewModel()
        callApiStoreListMovieFavoriteViewModel()
        callCategoryMovieApiData()
        callApiDataMovieBanner()
        navController = findNavController(R.id.fragmentContainerView)
        binding.bottomBarNav.setupWithNavController(navController)

        //TODO Chuyen mang hinh chính
//        scopeMain.launch {
//            delay(4000L) // Fake Thời giang
//            binding.animationView.visibility = View.GONE
//        }
    }


    private fun callCategoryMovieApiData() {
        CompanionObject.getMovieData { listCategory: List<MovieCategory> ->
            fragmentViewModel.setMovieCategory(listCategory)
        }
    }

    private fun callApiDataMovieBanner() {
        CompanionObject.getMovieBanner {
            fragmentViewModel.setMovieBanner(it)
        }
    }

    private fun callApiStoreListMovieFavoriteViewModel() {
        firebaseAuth.currentUser?.let { MoreFeature.uid = it.uid }
        MoreFeature.listMovieFavoriteByUid { listMovieFavorite ->
            viewModels.setMovieFavorite(listMovieFavorite)
        }
    }

    private fun setStoreViewModel() {
        firebaseAuth.currentUser?.let { viewModels.setUidUser(it.uid) }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}