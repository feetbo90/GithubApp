package com.my.githubapp.ui.view.follows

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.githubapp.adapter.GithubAdapter
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.result.MyResult
import com.my.githubapp.databinding.FragmentFollowsBinding
import com.my.githubapp.ui.view.details.DetailActivity
import com.my.githubapp.ui.view.main.MainActivity
import com.my.githubapp.ui.viewmodel.follows.FollowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowsFragment : Fragment() {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private val followsViewModel: FollowsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME, "")

        if (index == 1) {
            username?.let {
                val mIndex = 1
                setViewModel(it, mIndex)
            }
        } else {
            username?.let {
                val mIndex = 2
                setViewModel(it, mIndex)
            }
        }
    }

    private fun setViewModel(username: String, index: Int) {
        if (index == 1) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                launch {
                    followsViewModel.followers.collect { result ->
                        onFollowsResultReceived(result)
                    }
                }
                launch {
                    followsViewModel.isLoaded.collect { loaded ->
                        if (!loaded)
                            followsViewModel.getUserFollowers(username)
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                launch {
                    followsViewModel.following.collect { result ->
                        onFollowsResultReceived(result)
                    }
                }
                launch {
                    followsViewModel.isLoaded.collect { loaded ->
                        if (!loaded)
                            followsViewModel.getUserFollowing(username)
                    }
                }
            }
        }
    }

    private fun onFollowsResultReceived(result: MyResult<ArrayList<SimpleUser>>) {
        when (result) {
            is MyResult.Loading -> showLoading(true)
            is MyResult.Error -> {
                binding.status.visibility = View.VISIBLE
                binding.status.text = "An Error is Occurred"
                showLoading(false)
            }
            is MyResult.Success -> {
                showFollows(result.data)
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.loading.visibility = View.VISIBLE
        else binding.loading.visibility = View.GONE
    }

    private fun showFollows(users: ArrayList<SimpleUser>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = GithubAdapter(users)

            binding.users.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                GithubAdapter.OnItemClickCallback {
                override fun onItemClicked(data: SimpleUser) {
                    goToDetail(data)
                }

            })
        } else binding.status.visibility = View.VISIBLE
    }

    private fun goToDetail(user: SimpleUser) {
        Intent(activity, DetailActivity::class.java).apply {
            putExtra(MainActivity.PARCEL_GITHUB, user.login)
        }.also {
            startActivity(it)
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, username)
                }
            }
    }
}