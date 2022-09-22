package com.my.githubapp.ui.view.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.my.githubapp.R
import com.my.githubapp.adapter.SectionPageAdapter
import com.my.githubapp.data.User
import com.my.githubapp.data.entity.GithubEntity
import com.my.githubapp.data.result.MyResult
import com.my.githubapp.databinding.ActivityDetailBinding
import com.my.githubapp.ui.view.main.MainActivity
import com.my.githubapp.ui.viewmodel.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailsViewModel by viewModels()
    private var person: String? = null
    private var userDetail: GithubEntity? = null
    private var isFavorite: Boolean = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        person = intent.extras?.get(MainActivity.PARCEL_GITHUB) as String
        lifecycleScope.launch {
            launch {
                detailViewModel.userDetail.collect { result ->
                    onDetailsReceived(result)
                }
            }

            launch {
                detailViewModel.isLoaded.collect { loaded ->
                    if (!loaded) detailViewModel.getDetails(person!!)
                }
            }

            launch {
                detailViewModel.isFavoriteGithub(person ?: "").collect { state ->
                    isFavoriteGithub(state)
                    isFavorite = state
                }
            }
        }

        binding.favorite.setOnClickListener {
            if (isFavorite) {
                userDetail?.let { detailViewModel.deleteFromFavorite(it) }
                isFavoriteGithub(false)
                Toast.makeText(this, "deleted from favorite", Toast.LENGTH_SHORT).show()
            } else {
                userDetail?.let { detailViewModel.saveAsFavorite(it) }
                isFavoriteGithub(true)
                Toast.makeText(this, "added to favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFavoriteGithub(favorite: Boolean) {
        if (favorite) {
            binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun onDetailsReceived(result: MyResult<User>) {
        when (result) {
            is MyResult.Loading -> showLoading(true)
            is MyResult.Error -> {
                errorOccurred()
                binding.detailNoInternet.visibility = View.VISIBLE
                binding.detailNoInternet.text = getString(R.string.error)
                showLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is MyResult.Success -> {
                parseUserDetail(result.data)
                setTabLayoutAdapter()
                result.data.let { github ->
                    val githubEntity = GithubEntity(
                        github.login,
                        github.avatarUrl,
                        true
                    )
                    userDetail = githubEntity
                }
                showLoading(false)
            }
        }
    }

    private fun errorOccurred() {
        binding.apply {
            detailTabs.visibility = View.INVISIBLE
            detailViewPager.visibility = View.INVISIBLE
        }
    }

    private fun setTabLayoutAdapter() {
        val viewPager: ViewPager2 = binding.detailViewPager
        val tabs: TabLayout = binding.detailTabs
        viewPager.adapter = SectionPageAdapter(this, person!!)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_DETAIL[position])
        }.attach()
    }

    private fun parseUserDetail(user: User) {
        binding.apply {
            detailUsername.setAndVisible(user.login)
            detailFollowersValue.setAndVisible(user.followers.toString())
            detailFollowingValue.setAndVisible(user.following.toString())
            detailRepoValue.setAndVisible(user.publicRepos.toString())
            detailName.setAndVisible(user.name)
            detailCompany.setAndVisible(user.company)
            detailLocation.setAndVisible(user.location)
            detailImage.visibility = View.VISIBLE
            detailFollowing.visibility = View.VISIBLE
            detailFollowers.visibility = View.VISIBLE
            detailRepo.visibility = View.VISIBLE
            detailTabs.visibility = View.VISIBLE
            detailViewPager.visibility = View.VISIBLE
            appCompatImageView.visibility = View.VISIBLE
            favorite.visibility = View.VISIBLE
            Glide
                .with(this@DetailActivity)
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_baseline_person)
                .into(detailImage)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                detailViewPager.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                detailViewPager.visibility = View.VISIBLE
            }
        }
    }

    private fun TextView.setAndVisible(text: String?) {
        if (!text.isNullOrBlank()) {
            this.text = text
            this.visibility = View.VISIBLE
        }
    }

    companion object {
        @StringRes
        private val TAB_DETAIL = intArrayOf(
            R.string.tab_2,
            R.string.tab_1
        )
    }
}