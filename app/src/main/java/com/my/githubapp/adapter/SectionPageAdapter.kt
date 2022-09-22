package com.my.githubapp.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.my.githubapp.ui.view.follows.FollowsFragment

class SectionPageAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun createFragment(position: Int): Fragment {
        return FollowsFragment.newInstance(position + 1, username)
    }

}