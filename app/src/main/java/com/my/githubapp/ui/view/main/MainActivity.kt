package com.my.githubapp.ui.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.githubapp.R
import com.my.githubapp.adapter.GithubAdapter
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.result.MyResult
import com.my.githubapp.databinding.ActivityMainBinding
import com.my.githubapp.ui.view.details.DetailActivity
import com.my.githubapp.ui.view.favorites.FavoritesActivity
import com.my.githubapp.ui.view.settings.SettingActivity
import com.my.githubapp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        context = this
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.users.collect{
                        result -> showRecycle(result)
                    }
                }
                launch {
                    mainViewModel.themeSetting.collect { state ->
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }


    private fun showRecycle(result: MyResult<ArrayList<SimpleUser>>) {
        when(result) {
            is MyResult.Loading -> showLoading(true)
            is MyResult.Success -> {
                val githubAdapter = GithubAdapter(result.data)

                binding.myUsers.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = githubAdapter
                    setHasFixedSize(true)
                }

                githubAdapter.setOnItemClickCallback(object :
                    GithubAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: SimpleUser) {
                        Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra(PARCEL_GITHUB, data.login)
                        }.also {
                            startActivity(it)
                        }
                    }
                })
                showLoading(false)
            }
            is MyResult.Error -> {
                binding.message.visibility = View.VISIBLE
                binding.message.text = "An Error is Occurred"
                Toast.makeText(this@MainActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.searchUser(query ?: "")
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this@MainActivity, FavoritesActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.setting -> {
                Intent(this@MainActivity, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.myLoading.visibility = View.VISIBLE
            binding.myUsers.visibility = View.GONE
        } else {
            binding.myLoading.visibility = View.GONE
            binding.myUsers.visibility = View.VISIBLE
        }
    }

    companion object {
        const val PARCEL_GITHUB = "parcel_github"
    }
}