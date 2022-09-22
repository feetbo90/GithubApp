package com.my.githubapp.ui.view.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.githubapp.R
import com.my.githubapp.adapter.GithubAdapter
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.entity.GithubEntity
import com.my.githubapp.databinding.ActivityFavoritesBinding
import com.my.githubapp.ui.view.details.DetailActivity
import com.my.githubapp.ui.view.main.MainActivity
import com.my.githubapp.ui.viewmodel.favorites.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val favoriteViewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(getString(R.string.favorite))

        lifecycleScope.launchWhenStarted {
            launch {
                favoriteViewModel.favorite.collect {
                    if (it.isNotEmpty()) showFavoriteGithub(it)
                    else showMessages()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showFavoriteGithub(users: List<GithubEntity>) {
        val listUsers = ArrayList<SimpleUser>()
        Log.d("Hasil Db ", listUsers.size.toString() )
        users.forEach { user ->
            val data = SimpleUser(
                user.avatarUrl,
                user.id
            )

            listUsers.add(data)
        }

        val listUserAdapter = GithubAdapter(listUsers)

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = listUserAdapter
            visibility = View.VISIBLE
            setHasFixedSize(true)
        }

        binding.tvMessage.visibility = View.GONE

        listUserAdapter.setOnItemClickCallback(object :
            GithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SimpleUser) {
                goToDetail(data)
            }
        })
    }

    private fun goToDetail(user: SimpleUser) {
        Intent(this@FavoritesActivity, DetailActivity::class.java).apply {
            putExtra(MainActivity.PARCEL_GITHUB, user.login)
        }.also {
            startActivity(it)
        }
    }

    private fun showMessages() {
        binding.tvMessage.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }
}